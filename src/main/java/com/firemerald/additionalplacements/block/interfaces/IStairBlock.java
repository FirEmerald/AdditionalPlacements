package com.firemerald.additionalplacements.block.interfaces;

import java.util.List;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.firemerald.additionalplacements.block.stairs.AdditionalStairBlock;
import com.firemerald.additionalplacements.block.stairs.StairConnectionsType;
import com.firemerald.additionalplacements.block.stairs.common.CommonStairShape;
import com.firemerald.additionalplacements.block.stairs.common.CommonStairShapeState;
import com.firemerald.additionalplacements.block.stairs.vanilla.VanillaStairShapeState;
import com.firemerald.additionalplacements.client.BlockHighlightHelper;
import com.firemerald.additionalplacements.generation.APGenerationTypes;
import com.firemerald.additionalplacements.generation.GenerationType;
import com.firemerald.additionalplacements.util.*;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
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
					.setValue(StairsBlock.FACING, vanillaShapeState.facing)
					.setValue(StairsBlock.HALF, vanillaShapeState.half)
					.setValue(StairsBlock.SHAPE, vanillaShapeState.shape);
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
    default BlockState updateShapeImpl(BlockState state, Direction direction, BlockState otherState, IWorld level, BlockPos pos, BlockPos otherPos)
	{
		ComplexFacing facing = getShapeState(state).facing;
		return getBlockState(facing, getShape(facing, level, pos), state);
	}

	@Override
    default BlockState getStateForPlacementImpl(BlockItemUseContext context, BlockState blockState)
	{
		ComplexFacing facing = getFacing(context);
		return getBlockState(facing, getShape(facing, context.getLevel(), context.getClickedPos()), blockState);
	}
	
	default CommonStairShape getShape(ComplexFacing facing, IWorld level, BlockPos pos) {
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
    default void renderPlacementPreview(MatrixStack pose, IVertexBuilder vertexConsumer, PlayerEntity player, BlockRayTraceResult result, float partial, float r, float g, float b, float a) {
		if (!this.connectionsType().allowFlipped) return;
		ComplexFacing facing = getFacing(result);
		//z is up
		//y is forward
		//x is right
		pose.pushPose();
		Matrix4f mat = new Matrix4f(new float[] {
				facing.right.getStepX(), facing.forward.getStepX(), facing.up.getStepX(), 0,
				facing.right.getStepY(), facing.forward.getStepY(), facing.up.getStepY(), 0,
				facing.right.getStepZ(), facing.forward.getStepZ(), facing.up.getStepZ(), 0,
				0, 0, 0, 1 
				});
		pose.last().pose().multiply(mat);
		pose.last().normal().mul(new Matrix3f(mat));
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
    default void renderPlacementHighlight(MatrixStack pose, IVertexBuilder vertexConsumer, PlayerEntity player, BlockRayTraceResult result, float partial, float r, float g, float b, float a)
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

	default ComplexFacing getFacing(BlockRayTraceResult hitResult)
	{
		return getFacing(hitResult.getDirection(), hitResult.getLocation(), hitResult.getBlockPos());
	}

	default ComplexFacing getFacing(BlockItemUseContext context)
	{
		return getFacing(context.getClickedFace(), context.getClickLocation(), context.getClickedPos());
	}

	default ComplexFacing getFacing(Direction out, Vector3d hitPos, Vector3i blockPos)
	{
		return getFacing(out, 
				(float) (hitPos.x - blockPos.getX() - .5), 
				(float) (hitPos.y - blockPos.getY() - .5), 
				(float) (hitPos.z - blockPos.getZ() - .5));
	}

	default ComplexFacing getFacing(Direction out, float hitX, float hitY, float hitZ)
	{
		switch (out.getAxis()) {
		case X:
			return getFacingFromSide(hitZ, hitY, Direction.SOUTH, Direction.UP, out);
		case Y:
			return getFacingFromSide(hitX, hitZ, Direction.EAST, Direction.SOUTH, out);
		case Z:
			return getFacingFromSide(hitX, hitY, Direction.EAST, Direction.UP, out);
		default: 
			return ComplexFacing.SOUTH_UP;
		}
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
	@OnlyIn(Dist.CLIENT)
    default void addPlacementTooltip(ItemStack stack, @Nullable IBlockReader level, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent("tooltip.additionalplacements.vertical_placement"));
		tooltip.add(new TranslationTextComponent(connectionsType().tooltip));
	}

	@Override
	public default boolean paneConnectOverride(BlockState ourState, Direction.Axis paneAxis, Direction connectDir) {
		CommonStairShapeState shapeState = getShapeState(ourState);
		if (connectDir == shapeState.facing.backward) { //connects front
			switch (shapeState.shape.paneFront) {
                case NONE:
                    return false;
				case BOTH:
					return true;
                case HORIZONTAL:
					return paneAxis == shapeState.facing.left.getAxis();
                case VERTICAL:
					return paneAxis == shapeState.facing.up.getAxis();
            }
		} else if (connectDir == shapeState.facing.down) { //connects top
			switch (shapeState.shape.paneTop) {
				case NONE:
					return false;
				case BOTH:
					return true;
				case HORIZONTAL:
					return paneAxis == shapeState.facing.left.getAxis();
				case VERTICAL:
					return paneAxis == shapeState.facing.forward.getAxis();
			}
		} else if (connectDir == shapeState.facing.left) { //connects right
			switch (shapeState.shape.paneRight) {
				case NONE:
					return false;
				case BOTH:
					return true;
				case HORIZONTAL:
					return paneAxis == shapeState.facing.forward.getAxis();
				case VERTICAL:
					return paneAxis == shapeState.facing.up.getAxis();
			}
		} else if (connectDir == shapeState.facing.right) { //connects left
			switch (shapeState.shape.paneLeft) {
				case NONE:
					return false;
				case BOTH:
					return true;
				case HORIZONTAL:
					return paneAxis == shapeState.facing.forward.getAxis();
				case VERTICAL:
					return paneAxis == shapeState.facing.up.getAxis();
			}
		} else return true; //back and bottom always connect
		return false; //should not reach
	}
}