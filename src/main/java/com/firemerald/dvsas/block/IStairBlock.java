package com.firemerald.dvsas.block;

import org.apache.commons.lang3.tuple.Pair;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IStairBlock extends IVerticalBlock, SimpleWaterloggedBlock
{
	public static abstract class PartialState
	{
		public abstract boolean isHorizontal();
		
		public abstract BlockState apply(BlockState state);
		
		public static class HorizontalState extends PartialState
		{
			public final Half half;
			public final Direction facing;
			public final StairsShape shape;
			
			public HorizontalState(Half half, Direction facing, StairsShape shape)
			{
				this.half = half;
				this.facing = facing;
				this.shape = shape;
			}
			
			@Override
			public boolean isHorizontal()
			{
				return true;
			}

			@Override
			public BlockState apply(BlockState state)
			{
				return state.setValue(StairBlock.HALF, half).setValue(StairBlock.FACING, facing).setValue(StairBlock.SHAPE, shape);
			}
			
			@Override
			public String toString()
			{
				return "HorizontalState[" + half + "," + facing + "," + shape + "]";
			}
		}
		
		public static class VerticalState extends PartialState
		{
			public final VerticalStairBlock.EnumPlacing placing;
			public final VerticalStairBlock.EnumShape shape;
			
			public VerticalState(VerticalStairBlock.EnumPlacing placing, VerticalStairBlock.EnumShape shape)
			{
				this.placing = placing;
				this.shape = shape;
			}
			
			@Override
			public boolean isHorizontal()
			{
				return false;
			}

			@Override
			public BlockState apply(BlockState state)
			{
				return state.setValue(VerticalStairBlock.PLACING, placing).setValue(VerticalStairBlock.SHAPE, shape);
			}
			
			@Override
			public String toString()
			{
				return "VerticalState[" + placing + "," + shape + "]";
			}
		}
	}
	
	public static final PartialState[][] FULL_TO_PARTIAL = new PartialState[12][9];
	@SuppressWarnings("unchecked")
	public static final Pair<EnumPlacing, EnumShape>[][][] HORIZONTAL_TO_FULL = new Pair[2][4][5];
	@SuppressWarnings("unchecked")
	public static final Pair<EnumPlacing, EnumShape>[][] VERTICAL_TO_FULL = new Pair[4][17];
	public static final boolean INIT = internalBuildStateMaps();
	
	public static boolean internalBuildStateMaps()
	{
		internalBuildHorizontalStates(Direction.NORTH);
		internalBuildHorizontalStates(Direction.EAST);
		internalBuildHorizontalStates(Direction.SOUTH);
		internalBuildHorizontalStates(Direction.WEST);
		internalBuildVerticalStates(VerticalStairBlock.EnumPlacing.NORTH_EAST);
		internalBuildVerticalStates(VerticalStairBlock.EnumPlacing.EAST_SOUTH);
		internalBuildVerticalStates(VerticalStairBlock.EnumPlacing.SOUTH_WEST);
		internalBuildVerticalStates(VerticalStairBlock.EnumPlacing.WEST_NORTH);
		/* DEBUG
		for (int i = 0; i < 12; ++i)
		{
			System.out.println(EnumPlacing.values()[i]);
			PartialState[] array = FULL_TO_PARTIAL[i];
			for (int j = 0; j < 9; ++j)
			{
				System.out.println("\t" + EnumShape.values()[j] + ": " + array[j]);
			}
		}
		*/
		return true;
	}
	
	public static void internalBuildHorizontalStates(Direction direction)
	{
		EnumPlacing placing = EnumPlacing.forFacing(direction.getOpposite(), Direction.UP);
		internalSetHorizontalStateMap(Half.BOTTOM, direction, StairsShape.STRAIGHT, placing, EnumShape.STRAIGHT);
		internalSetHorizontalStateMap(Half.BOTTOM, direction, StairsShape.INNER_LEFT, placing, EnumShape.INSIDE_LEFT);
		internalSetHorizontalStateMap(Half.BOTTOM, direction, StairsShape.OUTER_LEFT, placing, EnumShape.OUTSIDE_HORIZONTAL_LEFT);
		internalSetHorizontalStateMap(Half.BOTTOM, direction, StairsShape.INNER_RIGHT, placing, EnumShape.INSIDE_RIGHT);
		internalSetHorizontalStateMap(Half.BOTTOM, direction, StairsShape.OUTER_RIGHT, placing, EnumShape.OUTSIDE_HORIZONTAL_RIGHT);
		placing = EnumPlacing.forFacing(direction.getOpposite(), Direction.DOWN);
		internalSetHorizontalStateMap(Half.TOP, direction, StairsShape.STRAIGHT, placing, EnumShape.STRAIGHT);
		internalSetHorizontalStateMap(Half.TOP, direction, StairsShape.INNER_LEFT, placing, EnumShape.INSIDE_RIGHT);
		internalSetHorizontalStateMap(Half.TOP, direction, StairsShape.OUTER_LEFT, placing, EnumShape.OUTSIDE_HORIZONTAL_RIGHT);
		internalSetHorizontalStateMap(Half.TOP, direction, StairsShape.INNER_RIGHT, placing, EnumShape.INSIDE_LEFT);
		internalSetHorizontalStateMap(Half.TOP, direction, StairsShape.OUTER_RIGHT, placing, EnumShape.OUTSIDE_HORIZONTAL_LEFT);
	}
	
	public static void internalBuildHorizontalStates(Direction direction, Direction up, Half half)
	{
		EnumPlacing placing = EnumPlacing.forFacing(direction.getOpposite(), up);
		internalSetHorizontalStateMap(half, direction, StairsShape.STRAIGHT, placing, EnumShape.STRAIGHT);
		internalSetHorizontalStateMap(half, direction, StairsShape.INNER_LEFT, placing, EnumShape.INSIDE_LEFT);
		internalSetHorizontalStateMap(half, direction, StairsShape.OUTER_LEFT, placing, EnumShape.OUTSIDE_HORIZONTAL_LEFT);
		internalSetHorizontalStateMap(half, direction, StairsShape.INNER_RIGHT, placing, EnumShape.INSIDE_RIGHT);
		internalSetHorizontalStateMap(half, direction, StairsShape.OUTER_RIGHT, placing, EnumShape.OUTSIDE_HORIZONTAL_RIGHT);
	}
	
	public static void internalBuildVerticalStates(VerticalStairBlock.EnumPlacing direction)
	{
		EnumPlacing placing = EnumPlacing.forFacing(direction.counterClockWiseFront, direction.clockWiseFront);
		internalSetVerticalStateMap(direction, VerticalStairBlock.EnumShape.STRAIGHT, placing, EnumShape.STRAIGHT);
		internalSetVerticalStateMap(direction, VerticalStairBlock.EnumShape.INNER_UP, placing, EnumShape.INSIDE_LEFT);
		internalSetVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_UP, placing, EnumShape.OUTSIDE_RIGHT);
		internalSetVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_FLAT_UP_CCW, placing, EnumShape.OUTSIDE_HORIZONTAL_RIGHT);
		internalSetVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_FLAT_UP_CW, placing, EnumShape.OUTSIDE_VERTICAL_RIGHT);
		internalSetVerticalStateMap(direction, VerticalStairBlock.EnumShape.INNER_DOWN, placing, EnumShape.INSIDE_RIGHT);
		internalSetVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_DOWN, placing, EnumShape.OUTSIDE_LEFT);
		internalSetVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_FLAT_DOWN_CCW, placing, EnumShape.OUTSIDE_HORIZONTAL_LEFT);
		internalSetVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_FLAT_DOWN_CW, placing, EnumShape.OUTSIDE_VERTICAL_LEFT);
		placing = EnumPlacing.forFacing(Direction.UP, direction.counterClockWiseFront);
		internalSetVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_UP_FROM_CCW, placing, EnumShape.OUTSIDE_RIGHT);
		internalSetVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_FLAT_UP_FROM_CCW, placing, EnumShape.OUTSIDE_VERTICAL_RIGHT);
		placing = EnumPlacing.forFacing(Direction.UP, direction.clockWiseFront);
		internalSetVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_UP_FROM_CW, placing, EnumShape.OUTSIDE_LEFT);
		internalSetVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_FLAT_UP_FROM_CW, placing, EnumShape.OUTSIDE_VERTICAL_LEFT);
		placing = EnumPlacing.forFacing(Direction.DOWN, direction.counterClockWiseFront);
		internalSetVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_DOWN_FROM_CCW, placing, EnumShape.OUTSIDE_LEFT);
		internalSetVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_FLAT_DOWN_FROM_CCW, placing, EnumShape.OUTSIDE_VERTICAL_LEFT);
		placing = EnumPlacing.forFacing(Direction.DOWN, direction.clockWiseFront);
		internalSetVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_DOWN_FROM_CW, placing, EnumShape.OUTSIDE_RIGHT);
		internalSetVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_FLAT_DOWN_FROM_CW, placing, EnumShape.OUTSIDE_VERTICAL_RIGHT);
	}
	
	public static void internalSetPartialStateMap(EnumPlacing placing, EnumShape shape, PartialState state)
	{
		FULL_TO_PARTIAL[placing.ordinal()][shape.ordinal()] = state;
	}
	
	public static void internalSetHorizontalStateMap(Half half, Direction facing, StairsShape shape, EnumPlacing fullPlacing, EnumShape fullShape)
	{
		internalSetPartialStateMap(fullPlacing, fullShape, new PartialState.HorizontalState(half, facing, shape));
		HORIZONTAL_TO_FULL[half.ordinal()][facing.get2DDataValue()][shape.ordinal()] = Pair.of(fullPlacing, fullShape);
	}
	
	public static void internalSetVerticalStateMap(VerticalStairBlock.EnumPlacing placing, VerticalStairBlock.EnumShape shape, EnumPlacing fullPlacing, EnumShape fullShape)
	{
		internalSetPartialStateMap(fullPlacing, fullShape, new PartialState.VerticalState(placing, shape));
		VERTICAL_TO_FULL[placing.ordinal()][shape.ordinal()] = Pair.of(fullPlacing, fullShape);
	}
	
	public static PartialState getPartialState(EnumPlacing placing, EnumShape shape)
	{
		return FULL_TO_PARTIAL[placing.ordinal()][shape.ordinal()];
	}
	
	public static Pair<EnumPlacing, EnumShape> getFullState(Half half, Direction facing, StairsShape shape)
	{
		return HORIZONTAL_TO_FULL[half.ordinal()][facing.get2DDataValue()][shape.ordinal()];
	}
	
	public static Pair<EnumPlacing, EnumShape> getFullState(VerticalStairBlock.EnumPlacing placing, VerticalStairBlock.EnumShape shape)
	{
		return VERTICAL_TO_FULL[placing.ordinal()][shape.ordinal()];
	}
	
	public static EnumPlacing getPlacing(BlockState blockState)
	{
		if (blockState.getBlock() instanceof StairBlock)
		{
			if (blockState.getValue(StairBlock.HALF) == Half.BOTTOM) switch (blockState.getValue(StairBlock.FACING))
			{
			case SOUTH: return EnumPlacing.UP_NORTH;
			case WEST: return EnumPlacing.UP_EAST;
			case NORTH: return EnumPlacing.UP_SOUTH;
			case EAST: return EnumPlacing.UP_WEST;
			default: return null;
			}
			else switch (blockState.getValue(StairBlock.FACING))
			{
			case SOUTH: return EnumPlacing.DOWN_NORTH;
			case WEST: return EnumPlacing.DOWN_EAST;
			case NORTH: return EnumPlacing.DOWN_SOUTH;
			case EAST: return EnumPlacing.DOWN_WEST;
			default: return null;
			}
		}
		else if (blockState.getBlock() instanceof VerticalStairBlock)
		{
			VerticalStairBlock.EnumShape shape = blockState.getValue(VerticalStairBlock.SHAPE);
			switch (blockState.getValue(VerticalStairBlock.PLACING))
			{
			case NORTH_EAST:
			{
				if (shape.isCounterClockwise)
				{
					if (shape.isUp) return EnumPlacing.UP_NORTH;
					else if (shape.isDown) return EnumPlacing.DOWN_NORTH;
					else return null;
				}
				else if (shape.isClockwise)
				{
					if (shape.isUp) return EnumPlacing.UP_EAST;
					else if (shape.isDown) return EnumPlacing.DOWN_EAST;
					else return null;
				}
				else return EnumPlacing.NORTH_EAST;
			}
			case EAST_SOUTH:
			{	
				if (shape.isCounterClockwise)
				{
					if (shape.isUp) return EnumPlacing.UP_EAST;
					else if (shape.isDown) return EnumPlacing.DOWN_EAST;
					else return null;
				}
				else if (shape.isClockwise)
				{
					if (shape.isUp) return EnumPlacing.UP_SOUTH;
					else if (shape.isDown) return EnumPlacing.DOWN_SOUTH;
					else return null;
				}
				else return EnumPlacing.EAST_SOUTH;
			}
			case SOUTH_WEST:
			{
				if (shape.isCounterClockwise)
				{
					if (shape.isUp) return EnumPlacing.UP_SOUTH;
					else if (shape.isDown) return EnumPlacing.DOWN_SOUTH;
					else return null;
				}
				else if (shape.isClockwise)
				{
					if (shape.isUp) return EnumPlacing.UP_WEST;
					else if (shape.isDown) return EnumPlacing.DOWN_WEST;
					else return null;
				}
				else return EnumPlacing.SOUTH_WEST;
			}
			case WEST_NORTH:
			{
				if (shape.isCounterClockwise)
				{
					if (shape.isUp) return EnumPlacing.UP_WEST;
					else if (shape.isDown) return EnumPlacing.DOWN_WEST;
					else return null;
				}
				else if (shape.isClockwise)
				{
					if (shape.isUp) return EnumPlacing.UP_NORTH;
					else if (shape.isDown) return EnumPlacing.DOWN_NORTH;
					else return null;
				}
				else return EnumPlacing.WEST_NORTH;
			}
			default: return null;
			}
		}
		else return null;
	}
	
	public static interface IVanillStairBlock extends IStairBlock
	{
		public void setStairs(VerticalStairBlock stairs);

		public BlockState getModelStateImpl();
	}
	
	public default Pair<EnumPlacing, EnumShape> getFullState(BlockState blockState)
	{
		//System.out.println(blockState);
		//if (blockState.getBlock() instanceof StairBlock) System.out.println(getFullState(blockState.getValue(StairBlock.HALF), blockState.getValue(StairBlock.FACING), blockState.getValue(StairBlock.SHAPE)));
		//else System.out.println(getFullState(blockState.getValue(VerticalStairBlock.PLACING), blockState.getValue(VerticalStairBlock.SHAPE)));
		if (blockState.getBlock() instanceof StairBlock) return getFullState(blockState.getValue(StairBlock.HALF), blockState.getValue(StairBlock.FACING), blockState.getValue(StairBlock.SHAPE));
		else return getFullState(blockState.getValue(VerticalStairBlock.PLACING), blockState.getValue(VerticalStairBlock.SHAPE));
	}
	
	public default BlockState getBlockState(EnumPlacing placing, EnumShape shape, BlockState currentState, FluidState fluidState)
	{
		PartialState partial = getPartialState(placing, shape);
		return partial.apply(partial.isHorizontal() ? getDefaultHorizontalState(currentState, fluidState) : getDefaultVerticalState(currentState, fluidState));
	}
	
	@Override
	public default BlockState rotateImpl(BlockState blockState, Rotation rotation) //TODO
	{
		Pair<EnumPlacing, EnumShape> state = getFullState(blockState);
		EnumPlacing placing = state.getLeft();
		Direction newFront = rotation.rotate(placing.front);
		Direction newTop = rotation.rotate(placing.top);
		for (EnumPlacing newPlacing : EnumPlacing.values())
		{
			if (newPlacing.front == newFront && newPlacing.top == newTop) return getBlockState(newPlacing, state.getRight(), blockState, getFluidStateImpl(blockState));
			else if (newPlacing.front == newTop && newPlacing.top == newFront)  return getBlockState(newPlacing,
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
			, blockState, getFluidStateImpl(blockState));
		}
		return blockState;
	}

	@Override
	public default BlockState mirrorImpl(BlockState blockState, Mirror mirror)
	{
		Pair<EnumPlacing, EnumShape> state = getFullState(blockState);
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
		, blockState, getFluidStateImpl(blockState));
	}
	
	public default BlockState updateShapeImpl(BlockState state, Direction direction, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos)
	{
        EnumPlacing placing = getPlacing(state);
        return placing == null ? state : getBlockState(placing, getShape(placing, level, pos), state, level.getFluidState(pos));
	}

	@Override
	public default BlockState getStateForPlacementImpl(BlockPlaceContext context)
	{
		BlockPos blockPos = context.getClickedPos();
		BlockState blockState = context.getLevel().getBlockState(blockPos);
        FluidState fluidState = context.getLevel().getFluidState(blockPos);
        EnumPlacing placing = getPlacing(context);
        return getBlockState(placing, getShape(placing, context.getLevel(), context.getClickedPos()), blockState, fluidState);
	}
	
	public default EnumShape getShape(EnumPlacing placing, BlockGetter level, BlockPos pos)
	{
		//prioritize back, bottom, front and left, right
		BlockState behind = level.getBlockState(pos.relative(placing.back));
		EnumPlacing behindPlace = getPlacing(behind);
		if (behindPlace != null)
		{
			if (behindPlace.top == placing.top)
			{
				if (behindPlace.right == placing.front) //outside left
				{
					BlockState below = level.getBlockState(pos.relative(placing.bottom));
					EnumPlacing belowPlace = getPlacing(below);
					if (belowPlace != null)
					{
						if ((belowPlace.right == placing.top && belowPlace.front == placing.front) || (belowPlace.left == placing.top && belowPlace.top == placing.front)) return EnumShape.OUTSIDE_LEFT;
					}
					return EnumShape.OUTSIDE_HORIZONTAL_LEFT;
				}
				else if (behindPlace.left == placing.front) //outside right
				{
					BlockState below = level.getBlockState(pos.relative(placing.bottom));
					EnumPlacing belowPlace = getPlacing(below);
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
					EnumPlacing belowPlace = getPlacing(below);
					if (belowPlace != null)
					{
						if ((belowPlace.right == placing.top && belowPlace.front == placing.front) || (belowPlace.left == placing.top && belowPlace.top == placing.front)) return EnumShape.OUTSIDE_LEFT;
					}
					return EnumShape.OUTSIDE_HORIZONTAL_LEFT;
				}
				else if (behindPlace.right == placing.front) //outside right
				{
					BlockState below = level.getBlockState(pos.relative(placing.bottom));
					EnumPlacing belowPlace = getPlacing(below);
					if (belowPlace != null)
					{
						if ((belowPlace.left == placing.top && belowPlace.front == placing.front) || (belowPlace.right == placing.top && belowPlace.top == placing.front)) return EnumShape.OUTSIDE_RIGHT;
					}
					return EnumShape.OUTSIDE_HORIZONTAL_RIGHT;
				}
			}
		}
		BlockState below = level.getBlockState(pos.relative(placing.bottom));
		EnumPlacing belowPlace = getPlacing(below);
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
		EnumPlacing frontPlace = getPlacing(front);
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
		EnumPlacing abovePlace = getPlacing(above);
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

    public static enum EnumPlacing
    {
        UP_NORTH(Direction.UP, Direction.NORTH),
        UP_EAST(Direction.UP, Direction.EAST),
        UP_SOUTH(Direction.UP, Direction.SOUTH),
        UP_WEST(Direction.UP, Direction.WEST),
        DOWN_NORTH(Direction.DOWN, Direction.NORTH),
        DOWN_EAST(Direction.DOWN, Direction.EAST),
        DOWN_SOUTH(Direction.DOWN, Direction.SOUTH),
        DOWN_WEST(Direction.DOWN, Direction.WEST),
        NORTH_EAST(Direction.NORTH, Direction.EAST),
        EAST_SOUTH(Direction.EAST, Direction.SOUTH),
        SOUTH_WEST(Direction.SOUTH, Direction.WEST),
        WEST_NORTH(Direction.WEST, Direction.NORTH);

        public final Direction top, bottom, front, back, left, right;

        private EnumPlacing(Direction top, Direction front)
        {
            this.top = top;
            this.bottom = top.getOpposite();
            this.front = front;
            this.back = front.getOpposite();
    		Vec3i rightV = front.getNormal().cross(top.getNormal());
    		this.right = Direction.fromNormal(rightV.getX(),rightV.getY(), rightV.getZ());
    		this.left = right.getOpposite();
        }
        
        public static EnumPlacing forFacing(Direction dir1, Direction dir2)
        {
        	for (EnumPlacing placing : EnumPlacing.values()) if ((placing.front == dir1 && placing.top == dir2) || (placing.front == dir2 && placing.top == dir1)) return placing;
        	return null;
        }
    }

    public static enum EnumShape
    {
        STRAIGHT, //normal shape (flat on 2 planes)
        INSIDE_RIGHT, //flat on all planes
        OUTSIDE_RIGHT, //no flat
        OUTSIDE_HORIZONTAL_RIGHT, //flat on XZ plane or flat on right plane
        OUTSIDE_VERTICAL_RIGHT, //other flat
        INSIDE_LEFT, //flat on all planes
        OUTSIDE_LEFT, //no flat
        OUTSIDE_HORIZONTAL_LEFT, //flat on XZ plane or flat on right plane
        OUTSIDE_VERTICAL_LEFT; //other flat
    }
}