package com.firemerald.additionalplacements.block.interfaces;

import java.util.List;
import java.util.function.Function;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.StairStateHelper;
import com.firemerald.additionalplacements.block.VerticalStairBlock;
import com.firemerald.additionalplacements.block.StairStateHelper.EnumPlacing;
import com.firemerald.additionalplacements.block.StairStateHelper.EnumShape;
import com.firemerald.additionalplacements.block.StairStateHelper.PartialState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public interface IStairBlock<T extends Block> extends IPlacementBlock<T>
{
	public static interface IVanillaStairBlock extends IStairBlock<VerticalStairBlock>, IVanillaBlock<VerticalStairBlock>
	{
		public BlockState getModelStateImpl();
	}

	public default BlockState getBlockState(EnumPlacing placing, EnumShape shape, BlockState currentState)
	{
		PartialState partial = StairStateHelper.getPartialState(placing, shape);
		return partial.apply(partial.isHorizontal() ? getDefaultVanillaState(currentState) : getDefaultAdditionalState(currentState));
	}

	@Override
	public default BlockState transform(BlockState blockState, Function<Direction, Direction> transform)
	{
		Pair<EnumPlacing, EnumShape> state = StairStateHelper.getFullState(blockState);
		EnumPlacing placing = state.getLeft();
		Direction newFront = transform.apply(placing.front);
		Direction newTop = transform.apply(placing.top);
		boolean mirrorShape = newFront != placing.front ^ newTop != placing.top ^ transform.apply(placing.left) != placing.left;
		EnumPlacing newPlacing = placing;
		for (EnumPlacing newerPlacing : EnumPlacing.values())
		{
			if (newerPlacing.front == newFront && newerPlacing.top == newTop)
			{
				newPlacing = newerPlacing;
				break;
			}
			else if (newerPlacing.front == newTop && newerPlacing.top == newFront)
			{
				newPlacing = newerPlacing;
				mirrorShape = !mirrorShape;
				break;
			}
		}
		return getBlockState(newPlacing, !mirrorShape ? state.getRight() :
			switch (state.getRight())
			{
			case INSIDE_LEFT -> EnumShape.INSIDE_RIGHT;
			case INSIDE_RIGHT -> EnumShape.INSIDE_LEFT;
			case OUTSIDE_LEFT -> EnumShape.OUTSIDE_RIGHT;
			case OUTSIDE_RIGHT -> EnumShape.OUTSIDE_LEFT;
			case OUTSIDE_HORIZONTAL_LEFT -> EnumShape.OUTSIDE_HORIZONTAL_RIGHT;
			case OUTSIDE_HORIZONTAL_RIGHT -> EnumShape.OUTSIDE_HORIZONTAL_LEFT;
			case OUTSIDE_VERTICAL_LEFT -> EnumShape.OUTSIDE_VERTICAL_RIGHT;
			case OUTSIDE_VERTICAL_RIGHT -> EnumShape.OUTSIDE_VERTICAL_LEFT;
			default -> state.getRight();
			}
		, blockState);
	}

	@Override
	public default BlockState updateShapeImpl(BlockState state, Direction direction, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos)
	{
        EnumPlacing placing = StairStateHelper.getPlacing(state);
        return placing == null ? state : getBlockState(placing, getShape(placing, level, pos), state);
	}

	@Override
	public default BlockState getStateForPlacementImpl(BlockPlaceContext context, BlockState blockState)
	{
        EnumPlacing placing = getPlacing(context);
        return getBlockState(placing, getShape(placing, context.getLevel(), context.getClickedPos()), blockState);
	}

	public default EnumShape getShape(EnumPlacing placing, BlockGetter level, BlockPos pos)
	{
		//prioritize back, bottom, front and left, right
		BlockState behind = level.getBlockState(pos.relative(placing.back));
		EnumPlacing behindPlace = StairStateHelper.getPlacing(behind);
		if (behindPlace != null)
		{
			if (behindPlace.top == placing.top)
			{
				if (behindPlace.right == placing.front) //outside left
				{
					BlockState below = level.getBlockState(pos.relative(placing.bottom));
					EnumPlacing belowPlace = StairStateHelper.getPlacing(below);
					if (belowPlace != null)
					{
						if ((belowPlace.right == placing.top && belowPlace.front == placing.front) || (belowPlace.left == placing.top && belowPlace.top == placing.front)) return EnumShape.OUTSIDE_LEFT;
						if ((belowPlace.left == placing.top && belowPlace.front == placing.front) || (belowPlace.right == placing.top && belowPlace.top == placing.front)) return EnumShape.OUTSIDE_TWIST_RIGHT; 
					}
					return EnumShape.OUTSIDE_HORIZONTAL_LEFT;
				}
				else if (behindPlace.left == placing.front) //outside right
				{
					BlockState below = level.getBlockState(pos.relative(placing.bottom));
					EnumPlacing belowPlace = StairStateHelper.getPlacing(below);
					if (belowPlace != null)
					{
						if ((belowPlace.left == placing.top && belowPlace.front == placing.front) || (belowPlace.right == placing.top && belowPlace.top == placing.front)) return EnumShape.OUTSIDE_RIGHT;
						if ((belowPlace.right == placing.top && belowPlace.front == placing.front) || (belowPlace.left == placing.top && belowPlace.top == placing.front)) return EnumShape.OUTSIDE_TWIST_LEFT;
					}
					return EnumShape.OUTSIDE_HORIZONTAL_RIGHT;
				}
			}
			else if (behindPlace.front == placing.top)
			{
				if (behindPlace.left == placing.front) //outside left
				{
					BlockState below = level.getBlockState(pos.relative(placing.bottom));
					EnumPlacing belowPlace = StairStateHelper.getPlacing(below);
					if (belowPlace != null)
					{
						if ((belowPlace.right == placing.top && belowPlace.front == placing.front) || (belowPlace.left == placing.top && belowPlace.top == placing.front)) return EnumShape.OUTSIDE_LEFT;
						if ((belowPlace.left == placing.top && belowPlace.front == placing.front) || (belowPlace.right == placing.top && belowPlace.top == placing.front)) return EnumShape.OUTSIDE_TWIST_RIGHT;
					}
					return EnumShape.OUTSIDE_HORIZONTAL_LEFT;
				}
				else if (behindPlace.right == placing.front) //outside right
				{
					BlockState below = level.getBlockState(pos.relative(placing.bottom));
					EnumPlacing belowPlace = StairStateHelper.getPlacing(below);
					if (belowPlace != null)
					{
						if ((belowPlace.left == placing.top && belowPlace.front == placing.front) || (belowPlace.right == placing.top && belowPlace.top == placing.front)) return EnumShape.OUTSIDE_RIGHT;
						if ((belowPlace.right == placing.top && belowPlace.front == placing.front) || (belowPlace.left == placing.top && belowPlace.top == placing.front)) return EnumShape.OUTSIDE_TWIST_LEFT;
					}
					return EnumShape.OUTSIDE_HORIZONTAL_RIGHT;
				}
			}
		}
		BlockState below = level.getBlockState(pos.relative(placing.bottom));
		EnumPlacing belowPlace = StairStateHelper.getPlacing(below);
		if (belowPlace != null)
		{
			if (belowPlace.left == placing.top)
			{
				if (belowPlace.top == placing.front) return EnumShape.OUTSIDE_VERTICAL_LEFT;
				else if (belowPlace.front == placing.front) return EnumShape.OUTSIDE_VERTICAL_RIGHT;
			}
			else if (belowPlace.right == placing.top)
			{
				if (belowPlace.front == placing.front) return EnumShape.OUTSIDE_VERTICAL_LEFT;
				else if (belowPlace.top == placing.front) return EnumShape.OUTSIDE_VERTICAL_RIGHT;
			}
		}
		BlockState front = level.getBlockState(pos.relative(placing.front));
		EnumPlacing frontPlace = StairStateHelper.getPlacing(front);
		if (frontPlace != null)
		{
			if (frontPlace.top == placing.top)
			{
				if (frontPlace.right == placing.front) return EnumShape.INSIDE_LEFT;
				else if (frontPlace.left == placing.front) return EnumShape.INSIDE_RIGHT;
			}
			else if (frontPlace.front == placing.top)
			{
				if (frontPlace.left == placing.front) return EnumShape.INSIDE_LEFT;
				else if (frontPlace.right == placing.front) return EnumShape.INSIDE_RIGHT;
			}
		}
		BlockState above = level.getBlockState(pos.relative(placing.top));
		EnumPlacing abovePlace = StairStateHelper.getPlacing(above);
		if (abovePlace != null)
		{
			if (abovePlace.left == placing.top)
			{
				if (abovePlace.top == placing.front) return EnumShape.INSIDE_LEFT;
				else if (abovePlace.front == placing.front) return EnumShape.INSIDE_RIGHT;
			}
			else if (abovePlace.right == placing.top)
			{
				if (abovePlace.front == placing.front) return EnumShape.INSIDE_LEFT;
				else if (abovePlace.top == placing.front) return EnumShape.INSIDE_RIGHT;
			}
		}
		return EnumShape.STRAIGHT;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public default void renderPlacementHighlight(PoseStack pose, VertexConsumer vertexConsumer, Player player, BlockHitResult result, float partial)
	{
		Matrix4f poseMat = pose.last().pose();
		Matrix3f normMat = pose.last().normal();
		vertexConsumer.vertex(poseMat, -.5f, -.5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .5f, -.5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .5f, -.5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .5f, .5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .5f, .5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.5f, .5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.5f, .5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.5f, -.5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.25f, -.25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .25f, .25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.25f, .25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .25f, -.25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.5f, -.25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.25f, -.25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.25f, -.25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.25f, -.5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .5f, -.25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .25f, -.25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .25f, -.25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .25f, -.5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.5f, .25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.25f, .25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.25f, .25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.25f, .5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .5f, .25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .25f, .25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .25f, .25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .25f, .5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
	}

    @Override
	public default boolean disablePlacement()
	{
		return AdditionalPlacementsMod.COMMON_CONFIG.disableAutomaticStairPlacement.get();
	}

	public default EnumPlacing getPlacing(BlockPlaceContext context)
	{
		double hitX = context.getClickLocation().x - context.getClickedPos().getX() - .5;
		double hitY = context.getClickLocation().y - context.getClickedPos().getY() - .5;
		double hitZ = context.getClickLocation().z - context.getClickedPos().getZ() - .5;
        switch (context.getClickedFace())
        {
		case DOWN:
			if (hitX < -.25f)
			{
				if (hitZ < -.25f) return EnumPlacing.EAST_SOUTH;
				else if (hitZ > .25f) return EnumPlacing.NORTH_EAST;
			}
			else if (hitX > .25f)
			{
				if (hitZ < -.25f) return EnumPlacing.SOUTH_WEST;
				else if (hitZ > .25f) return EnumPlacing.WEST_NORTH;
			}
			if (hitX > hitZ)
			{
				if (hitX > -hitZ) return EnumPlacing.DOWN_WEST;
				else return EnumPlacing.DOWN_SOUTH;
			}
			else
			{
				if (hitZ > -hitX) return EnumPlacing.DOWN_NORTH;
				else return EnumPlacing.DOWN_EAST;
			}
		case UP:
			if (hitX < -.25f)
			{
				if (hitZ < -.25f) return EnumPlacing.EAST_SOUTH;
				else if (hitZ > .25f) return EnumPlacing.NORTH_EAST;
			}
			else if (hitX > .25f)
			{
				if (hitZ < -.25f) return EnumPlacing.SOUTH_WEST;
				else if (hitZ > .25f) return EnumPlacing.WEST_NORTH;
			}
			if (hitX > hitZ)
			{
				if (hitX > -hitZ) return EnumPlacing.UP_WEST;
				else return EnumPlacing.UP_SOUTH;
			}
			else
			{
				if (hitZ > -hitX) return EnumPlacing.UP_NORTH;
				else return EnumPlacing.UP_EAST;
			}
		case NORTH:
			if (hitX < -.25f)
			{
				if (hitY < -.25f) return EnumPlacing.UP_EAST;
				else if (hitY > .25f) return EnumPlacing.DOWN_EAST;
			}
			else if (hitX > .25f)
			{
				if (hitY < -.25f) return EnumPlacing.UP_WEST;
				else if (hitY > .25f) return EnumPlacing.DOWN_WEST;
			}
			if (hitX > hitY)
			{
				if (hitX > -hitY) return EnumPlacing.WEST_NORTH;
				else return EnumPlacing.UP_NORTH;
			}
			else
			{
				if (hitY > -hitX) return EnumPlacing.DOWN_NORTH;
				else return EnumPlacing.NORTH_EAST;
			}
		case SOUTH:
			if (hitX < -.25f)
			{
				if (hitY < -.25f) return EnumPlacing.UP_EAST;
				else if (hitY > .25f) return EnumPlacing.DOWN_EAST;
			}
			else if (hitX > .25f)
			{
				if (hitY < -.25f) return EnumPlacing.UP_WEST;
				else if (hitY > .25f) return EnumPlacing.DOWN_WEST;
			}
			if (hitX > hitY)
			{
				if (hitX > -hitY) return EnumPlacing.SOUTH_WEST;
				else return EnumPlacing.UP_SOUTH;
			}
			else
			{
				if (hitY > -hitX) return EnumPlacing.DOWN_SOUTH;
				else return EnumPlacing.EAST_SOUTH;
			}
		case WEST:
			if (hitY < -.25f)
			{
				if (hitZ < -.25f) return EnumPlacing.UP_SOUTH;
				else if (hitZ > .25f) return EnumPlacing.UP_NORTH;
			}
			else if (hitY > .25f)
			{
				if (hitZ < -.25f) return EnumPlacing.DOWN_SOUTH;
				else if (hitZ > .25f) return EnumPlacing.DOWN_NORTH;
			}
			if (hitZ > hitY)
			{
				if (hitZ > -hitY) return EnumPlacing.WEST_NORTH;
				else return EnumPlacing.UP_WEST;
			}
			else
			{
				if (hitY > -hitZ) return EnumPlacing.DOWN_WEST;
				else return EnumPlacing.SOUTH_WEST;
			}
		case EAST:
			if (hitY < -.25f)
			{
				if (hitZ < -.25f) return EnumPlacing.UP_SOUTH;
				else if (hitZ > .25f) return EnumPlacing.UP_NORTH;
			}
			else if (hitY > .25f)
			{
				if (hitZ < -.25f) return EnumPlacing.DOWN_SOUTH;
				else if (hitZ > .25f) return EnumPlacing.DOWN_NORTH;
			}
			if (hitZ > hitY)
			{
				if (hitZ > -hitY) return EnumPlacing.NORTH_EAST;
				else return EnumPlacing.UP_EAST;
			}
			else
			{
				if (hitY > -hitZ) return EnumPlacing.DOWN_EAST;
				else return EnumPlacing.EAST_SOUTH;
			}
        }
        return EnumPlacing.UP_NORTH;
	}

    @Override
	public default void addPlacementTooltip(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(Component.translatable("tooltip.additionalplacements.vertical_placement"));
	}
}