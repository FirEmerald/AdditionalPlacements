package com.firemerald.additionalplacements.block.interfaces;

import java.util.List;
import java.util.function.Function;

import javax.annotation.Nullable;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import com.firemerald.additionalplacements.block.stairs.AdditionalStairBlock;
import com.firemerald.additionalplacements.block.stairs.StairConnectionsType;
import com.firemerald.additionalplacements.block.stairs.common.CommonStairShape;
import com.firemerald.additionalplacements.block.stairs.common.CommonStairShapeState;
import com.firemerald.additionalplacements.block.stairs.vanilla.VanillaStairShapeState;
import com.firemerald.additionalplacements.client.BlockHighlightHelper;
import com.firemerald.additionalplacements.generation.APGenerationTypes;
import com.firemerald.additionalplacements.generation.GenerationType;
import com.firemerald.additionalplacements.util.ComplexFacing;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IStairBlock<T extends Block> extends IPlacementBlock<T>, IPaneConnectable
{
	interface IVanillaStairBlock extends IStairBlock<AdditionalStairBlock>, IVanillaBlock<AdditionalStairBlock>
	{
		BlockState getModelStateImpl();
	}

	default BlockState getBlockState(ComplexFacing facing, CommonStairShape shape, BlockState currentState) {
		return getBlockState(CommonStairShapeState.of(facing, shape), currentState);
	}

	default BlockState getBlockState(CommonStairShapeState shapeState, BlockState currentState)
	{
		VanillaStairShapeState vanillaShapeState = shapeState.vanilla();
		if (vanillaShapeState != null)
			return getDefaultVanillaState(currentState)
					.setValue(StairBlock.FACING, vanillaShapeState.facing)
					.setValue(StairBlock.HALF, vanillaShapeState.half)
					.setValue(StairBlock.SHAPE, vanillaShapeState.shape);
		else
			return getBlockStateInternal(shapeState, currentState);
	}

	BlockState getBlockStateInternal(CommonStairShapeState shapeState, BlockState currentState);

	@Override
    default BlockState transform(BlockState blockState, Function<Direction, Direction> transform)
	{
		CommonStairShapeState state = this.getShapeState(blockState);
		ComplexFacing oldFacing = state.facing;
		ComplexFacing newFacing = ComplexFacing.forFacing(transform.apply(oldFacing.forward), transform.apply(oldFacing.up));
		return getBlockState(newFacing, state.shape, blockState);
	}

	static ComplexFacing getFacingOrNull(BlockState blockState) {
		return blockState.getBlock() instanceof IStairBlock ?  ((IStairBlock<?>) blockState.getBlock()).getShapeState(blockState).facing : null;
	}

	CommonStairShapeState getShapeState(BlockState blockState);

	StairConnectionsType connectionsType();

	@Override
    default BlockState updateShapeImpl(BlockState state, Direction direction, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos)
	{
		ComplexFacing facing = getShapeState(state).facing;
		return getBlockState(facing, getShape(facing, level, pos), state);
	}

	@Override
    default BlockState getStateForPlacementImpl(BlockPlaceContext context, BlockState blockState)
	{
		ComplexFacing facing = getFacing(context);
		return getBlockState(facing, getShape(facing, context.getLevel(), context.getClickedPos()), blockState);
	}

	default CommonStairShape getShape(ComplexFacing facing, BlockGetter level, BlockPos pos) {
		StairConnectionsType connectionsType = connectionsType();
		boolean allowVertical = connectionsType.allowVertical;
		boolean allowMixed = connectionsType.allowMixed;
		//prioritize left, right and back, bottom, front, top
		boolean connectedLeft = false, connectedRight = false;
		{ //checking left for connection
			BlockState left = level.getBlockState(pos.relative(facing.left));
			ComplexFacing leftFacing = getFacingOrNull(left);
			if (leftFacing != null) {
				if (leftFacing == facing || (allowMixed && leftFacing.flipped() == facing)) connectedLeft = true;
			}
		}
		{ //checking right for connection
			BlockState right = level.getBlockState(pos.relative(facing.right));
			ComplexFacing rightFacing = getFacingOrNull(right);
			if (rightFacing != null) {
				if (rightFacing == facing || (allowMixed && rightFacing.flipped() == facing)) connectedRight = true;
			}
		}
		if (connectedLeft && connectedRight) return CommonStairShape.STRAIGHT;
		{ //checking behind for outer corner
			BlockState behind = level.getBlockState(pos.relative(facing.backward));
			ComplexFacing behindFacing = getFacingOrNull(behind);
			if (behindFacing != null) {
				if (allowMixed && behindFacing.up != facing.up) behindFacing = behindFacing.flipped();
				if (behindFacing.up == facing.up) {
					if (allowMixed) { //checking below for outer corner
						BlockState below = level.getBlockState(pos.relative(facing.down));
						ComplexFacing belowFacing = getFacingOrNull(below);
						if (belowFacing != null) {
							if (belowFacing.forward != facing.forward) belowFacing = belowFacing.flipped();
							if (belowFacing.forward == facing.forward) {
								if (belowFacing.up == behindFacing.forward) {
									if (belowFacing.up == facing.left) {
										if (!connectedLeft) return CommonStairShape.OUTER_BOTH_LEFT;
									}
									else if (belowFacing.up == facing.right) {
										if (!connectedRight) return CommonStairShape.OUTER_BOTH_RIGHT;
									}
								} else if (!connectedLeft && !connectedRight && belowFacing.down == behindFacing.forward) {
									if (belowFacing.down == facing.left) return CommonStairShape.OUTER_BACK_RIGHT_BOTTOM_LEFT;
									else if (belowFacing.down == facing.right) return CommonStairShape.OUTER_BACK_LEFT_BOTTOM_RIGHT;
								}
							}
						}
					}
					if (behindFacing.forward == facing.left) {
						if (!connectedLeft) return CommonStairShape.OUTER_BACK_LEFT;
					}
					else if (behindFacing.forward == facing.right) {
						if (!connectedRight) return CommonStairShape.OUTER_BACK_RIGHT;
					}
				}
			}
		}
		if (allowVertical) { //checking below for outer corner
			BlockState below = level.getBlockState(pos.relative(facing.down));
			ComplexFacing belowFacing = getFacingOrNull(below);
			if (belowFacing != null) {
				if (allowMixed && belowFacing.forward != facing.forward) belowFacing = belowFacing.flipped();
				if (belowFacing.forward == facing.forward) {
					if (belowFacing.up == facing.left) {
						if (!connectedLeft) return CommonStairShape.OUTER_BOTTOM_LEFT;
					}
					else if (belowFacing.up == facing.right) {
						if (!connectedRight) return CommonStairShape.OUTER_BOTTOM_RIGHT;
					}
				}
			}
		}
		{ //checking front for inner corner
			BlockState inFront = level.getBlockState(pos.relative(facing.forward));
			ComplexFacing inFrontFacing = getFacingOrNull(inFront);
			if (inFrontFacing != null) {
				if (allowMixed && inFrontFacing.up != facing.up) inFrontFacing = inFrontFacing.flipped();
				if (inFrontFacing.up == facing.up) {
					if (allowMixed) { //checking top for inner corner
						BlockState above = level.getBlockState(pos.relative(facing.up));
						ComplexFacing aboveFacing = getFacingOrNull(above);
						if (aboveFacing != null) {
							if (aboveFacing.forward != facing.forward) aboveFacing = aboveFacing.flipped();
							if (aboveFacing.forward == facing.forward && aboveFacing.up == inFrontFacing.forward) {
								if (aboveFacing.up == facing.left) return CommonStairShape.INNER_BOTH_LEFT;
								else if (aboveFacing.up == facing.right) return CommonStairShape.INNER_BOTH_RIGHT;
							}
						}
					}
					if (inFrontFacing.forward == facing.left) return CommonStairShape.INNER_FRONT_LEFT;
					else if (inFrontFacing.forward == facing.right) return CommonStairShape.INNER_FRONT_RIGHT;
				}
			}
		}
		if (allowVertical) { //checking top for inner corner
			BlockState above = level.getBlockState(pos.relative(facing.up));
			ComplexFacing aboveFacing = getFacingOrNull(above);
			if (aboveFacing != null) {
				if (allowMixed && aboveFacing.forward != facing.forward) aboveFacing = aboveFacing.flipped();
				if (aboveFacing.forward == facing.forward) {
					if (aboveFacing.up == facing.left) return CommonStairShape.INNER_TOP_LEFT;
					else if (aboveFacing.up == facing.right) return CommonStairShape.INNER_TOP_RIGHT;
				}
			}
		}
		return CommonStairShape.STRAIGHT;
	}

	float ARROW_OFFSET = -0.4375f;
	float ARROW_OUTER = 0.375f;
	float ARROW_INNER = 0.125f;

	@Override
	@OnlyIn(Dist.CLIENT)
    default void renderPlacementPreview(PoseStack pose, VertexConsumer vertexConsumer, Player player, BlockHitResult result, float partial, float r, float g, float b, float a) {
		if (!this.connectionsType().allowFlipped) return;
		ComplexFacing facing = getFacing(result.getDirection(),
				(float) (result.getLocation().x - result.getBlockPos().getX() - .5),
				(float) (result.getLocation().y - result.getBlockPos().getY() - .5),
				(float) (result.getLocation().z - result.getBlockPos().getZ() - .5));
		//z is up
		//y is forward
		//x is right
		pose.pushPose();
		pose.mulPoseMatrix(new Matrix4f(
				facing.right  .getStepX(), facing.right  .getStepY(), facing.right  .getStepZ(), 0,
				facing.forward.getStepX(), facing.forward.getStepY(), facing.forward.getStepZ(), 0,
				facing.up     .getStepX(), facing.up     .getStepY(), facing.up     .getStepZ(), 0,
				0, 0, 0, 1
				));
		Matrix4f poseMat = pose.last().pose();
		Matrix3f normMat = pose.last().normal();
		BlockHighlightHelper.lineLoop(vertexConsumer, poseMat, normMat, ARROW_OFFSET, r, g, b, a,
				 0          ,  ARROW_OUTER,
				 ARROW_OUTER,  0          ,
				 ARROW_INNER,  0          ,
				 ARROW_INNER, -ARROW_OUTER,
				-ARROW_INNER, -ARROW_OUTER,
				-ARROW_INNER,  0          ,
				-ARROW_OUTER,  0          );
		pose.popPose();
	}

	float OUTER_EDGE = .5f;
	float INNER_EDGE = .25f;

	@Override
	@OnlyIn(Dist.CLIENT)
    default void renderPlacementHighlight(PoseStack pose, VertexConsumer vertexConsumer, Player player, BlockHitResult result, float partial, float r, float g, float b, float a)
	{
		Matrix4f poseMat = pose.last().pose();
		Matrix3f normMat = pose.last().normal();

		//outer box
		BlockHighlightHelper.lineCenteredSquare(vertexConsumer, poseMat, normMat, -OUTER_EDGE, r, g, b, a,
				OUTER_EDGE);

		if (this.connectionsType().allowFlipped) {
			//inner edges
			BlockHighlightHelper.lineCenteredGrid(vertexConsumer, poseMat, normMat, -OUTER_EDGE, r, g, b, a,
					INNER_EDGE, OUTER_EDGE);

			//middle cross
			BlockHighlightHelper.lineCenteredCross(vertexConsumer, poseMat, normMat, -OUTER_EDGE, r, g, b, a,
					OUTER_EDGE);
		} else {
			//corners
			BlockHighlightHelper.lineOctal(vertexConsumer, poseMat, normMat, -OUTER_EDGE, r, g, b, a,
					INNER_EDGE, INNER_EDGE,
					OUTER_EDGE, INNER_EDGE);

			//middle cross
			BlockHighlightHelper.lineCenteredCross(vertexConsumer, poseMat, normMat, -OUTER_EDGE, r, g, b, a,
					INNER_EDGE);
		}
	}

	@Override
    default GenerationType<?, ?> getGenerationType() {
		return APGenerationTypes.stairs();
	}

	default ComplexFacing getFacing(BlockHitResult hitResult)
	{
		return getFacing(hitResult.getDirection(), hitResult.getLocation(), hitResult.getBlockPos());
	}

	default ComplexFacing getFacing(BlockPlaceContext context)
	{
		return getFacing(context.getClickedFace(), context.getClickLocation(), context.getClickedPos());
	}

	default ComplexFacing getFacing(Direction out, Vec3 hitPos, Vec3i blockPos)
	{
		return getFacing(out,
				(float) (hitPos.x - blockPos.getX() - .5),
				(float) (hitPos.y - blockPos.getY() - .5),
				(float) (hitPos.z - blockPos.getZ() - .5));
	}

	default ComplexFacing getFacing(Direction out, float hitX, float hitY, float hitZ)
	{
        return switch (out.getAxis()) {
            case X -> getFacingFromSide(hitZ, hitY, Direction.SOUTH, Direction.UP, out);
            case Y -> getFacingFromSide(hitX, hitZ, Direction.EAST, Direction.SOUTH, out);
            case Z -> getFacingFromSide(hitX, hitY, Direction.EAST, Direction.UP, out);
        };
	}

	default ComplexFacing getFacingFromSide(float localX, float localY, Direction localRight, Direction localUp, Direction localOut) {
		if (localY > localX) { //top-left half
			if (localY > -localX) { //top quarter
				return getFacingFromQuarter(localX, localY, localRight, localUp, localOut);
			} else { //left quarter
				return getFacingFromQuarter(localY, -localX, localUp, localRight.getOpposite(), localOut);
			}
		} else { //bottom-right half
			if (localY > -localX) { //right quarter
				return getFacingFromQuarter(-localY, localX, localUp.getOpposite(), localRight, localOut);
			} else { //bottom quarter
				return getFacingFromQuarter(-localX, -localY, localRight.getOpposite(), localUp.getOpposite(), localOut);
			}
		}
	}

	default ComplexFacing getFacingFromQuarter(float localX, float localY, Direction localRight, Direction localUp, Direction localOut) {
		Direction forward, up;
		if (localY > INNER_EDGE) { //top half
			up = localUp.getOpposite();
			if (localX < -INNER_EDGE) { //top-left corner
				forward = localRight;
			} else if (localX > INNER_EDGE) { //top-right corner
				forward = localRight.getOpposite();
			} else { //top edge
				forward = localOut;
			}
		} else { //back corner
			up = localOut;
			forward = localUp.getOpposite();
		}
		return ComplexFacing.forFacing(forward, up);
	}

    @Override
    default void addPlacementTooltip(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(Component.translatable("tooltip.additionalplacements.vertical_placement"));
		tooltip.add(Component.translatable(connectionsType().tooltip));
	}

	@Override
	public default boolean paneConnectOverride(BlockState ourState, Direction.Axis paneAxis, Direction connectDir) {
		CommonStairShapeState shapeState = getShapeState(ourState);
		if (connectDir == shapeState.facing.backward) { //connects front
			return switch (shapeState.shape.paneFront) {
				case NONE -> false;
				case BOTH -> true;
				case HORIZONTAL -> paneAxis == shapeState.facing.left.getAxis();
				case VERTICAL -> paneAxis == shapeState.facing.up.getAxis();
			};
		} else if (connectDir == shapeState.facing.down) { //connects top
			return switch (shapeState.shape.paneTop) {
				case NONE -> false;
				case BOTH -> true;
				case HORIZONTAL -> paneAxis == shapeState.facing.left.getAxis();
				case VERTICAL -> paneAxis == shapeState.facing.forward.getAxis();
			};
		} else if (connectDir == shapeState.facing.left) { //connects right
			return switch (shapeState.shape.paneRight) {
				case NONE -> false;
				case BOTH -> true;
				case HORIZONTAL -> paneAxis == shapeState.facing.forward.getAxis();
				case VERTICAL -> paneAxis == shapeState.facing.up.getAxis();
			};
		} else if (connectDir == shapeState.facing.right) { //connects left
			return switch (shapeState.shape.paneLeft) {
				case NONE -> false;
				case BOTH -> true;
				case HORIZONTAL -> paneAxis == shapeState.facing.forward.getAxis();
				case VERTICAL -> paneAxis == shapeState.facing.up.getAxis();
			};
		} else return true; //back and bottom always connect
	}
}