package com.firemerald.additionalplacements.block.interfaces;

import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.StairStateHelper;
import com.firemerald.additionalplacements.block.VerticalStairBlock;
import com.firemerald.additionalplacements.block.StairStateHelper.EnumPlacing;
import com.firemerald.additionalplacements.block.StairStateHelper.EnumShape;
import com.firemerald.additionalplacements.block.StairStateHelper.PartialState;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
	
	public default EnumShape getOpposite(EnumShape shape)
	{
		switch (shape)
		{
		case INSIDE_LEFT: return EnumShape.INSIDE_RIGHT;
		case INSIDE_RIGHT: return EnumShape.INSIDE_LEFT;
		case OUTSIDE_LEFT: return EnumShape.OUTSIDE_RIGHT;
		case OUTSIDE_RIGHT: return EnumShape.OUTSIDE_LEFT;
		case OUTSIDE_HORIZONTAL_LEFT: return EnumShape.OUTSIDE_HORIZONTAL_RIGHT;
		case OUTSIDE_HORIZONTAL_RIGHT: return EnumShape.OUTSIDE_HORIZONTAL_LEFT;
		case OUTSIDE_VERTICAL_LEFT: return EnumShape.OUTSIDE_VERTICAL_RIGHT;
		case OUTSIDE_VERTICAL_RIGHT: return EnumShape.OUTSIDE_VERTICAL_LEFT;
		default: return shape;
		}
	}

	@Override
	public default BlockState rotateImpl(BlockState blockState, Rotation rotation)
	{
		Pair<EnumPlacing, EnumShape> state = StairStateHelper.getFullState(blockState);
		EnumPlacing placing = state.getLeft();
		Direction newFront = rotation.rotate(placing.front);
		Direction newTop = rotation.rotate(placing.top);
		for (EnumPlacing newPlacing : EnumPlacing.values())
		{
			if (newPlacing.front == newFront && newPlacing.top == newTop) return getBlockState(newPlacing, state.getRight(), blockState);
			else if (newPlacing.front == newTop && newPlacing.top == newFront)
			{
				return getBlockState(newPlacing, getOpposite(state.getRight()), blockState);
			}
		}
		return blockState;
	}

	@Override
	public default BlockState mirrorImpl(BlockState blockState, Mirror mirror)
	{
		Pair<EnumPlacing, EnumShape> state = StairStateHelper.getFullState(blockState);
		EnumPlacing placing = state.getLeft();
		Direction newFront = mirror.mirror(placing.front);
		Direction newTop = mirror.mirror(placing.top);
		boolean mirrorShape = newFront != placing.front ^ newTop != placing.top ^ mirror.mirror(placing.left) != placing.left;
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
		return getBlockState(newPlacing, !mirrorShape ? state.getRight() : getOpposite(state.getRight()), blockState);
	}

	@Override
	public default BlockState updateShapeImpl(BlockState state, Direction direction, BlockState otherState, IWorld level, BlockPos pos, BlockPos otherPos)
	{
        EnumPlacing placing = StairStateHelper.getPlacing(state);
        return placing == null ? state : getBlockState(placing, getShape(placing, level, pos), state);
	}

	@Override
	public default BlockState getStateForPlacementImpl(BlockItemUseContext context, BlockState blockState)
	{
        EnumPlacing placing = getPlacing(context);
        return getBlockState(placing, getShape(placing, context.getLevel(), context.getClickedPos()), blockState);
	}

	public default EnumShape getShape(EnumPlacing placing, IBlockReader level, BlockPos pos)
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
	@OnlyIn(Dist.CLIENT)
	public default void renderPlacementHighlight(MatrixStack pose, IVertexBuilder vertexConsumer, PlayerEntity player, BlockRayTraceResult result, float partial)
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
		return this instanceof StairsBlock && AdditionalPlacementsMod.COMMON_CONFIG.disableAutomaticStairPlacement.get();
	}

	public default EnumPlacing getPlacing(BlockItemUseContext context)
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
	public default void addPlacementTooltip(ItemStack stack, @Nullable IBlockReader level, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent("tooltip.additionalplacements.vertical_placement"));
	}
}