package com.firemerald.additionalplacements.block;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3i;

public class StairStateHelper
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
				return state.setValue(StairsBlock.HALF, half).setValue(StairsBlock.FACING, facing).setValue(StairsBlock.SHAPE, shape);
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

	private static final PartialState[][] FULL_TO_PARTIAL = new PartialState[12][9];
	@SuppressWarnings("unchecked")
	private static final Pair<EnumPlacing, EnumShape>[][][] HORIZONTAL_TO_FULL = new Pair[2][4][5];
	@SuppressWarnings("unchecked")
	private static final Pair<EnumPlacing, EnumShape>[][] VERTICAL_TO_FULL = new Pair[4][17];

	static
	{
		buildStateMaps();
	}

	private static void buildStateMaps()
	{
		buildHorizontalStates(Direction.NORTH);
		buildHorizontalStates(Direction.EAST);
		buildHorizontalStates(Direction.SOUTH);
		buildHorizontalStates(Direction.WEST);
		buildVerticalStates(VerticalStairBlock.EnumPlacing.NORTH_EAST);
		buildVerticalStates(VerticalStairBlock.EnumPlacing.EAST_SOUTH);
		buildVerticalStates(VerticalStairBlock.EnumPlacing.SOUTH_WEST);
		buildVerticalStates(VerticalStairBlock.EnumPlacing.WEST_NORTH);
	}

	private static void buildHorizontalStates(Direction direction)
	{
		EnumPlacing placing = EnumPlacing.forFacing(direction.getOpposite(), Direction.UP);
		setHorizontalStateMap(Half.BOTTOM, direction, StairsShape.STRAIGHT, placing, EnumShape.STRAIGHT);
		setHorizontalStateMap(Half.BOTTOM, direction, StairsShape.INNER_LEFT, placing, EnumShape.INSIDE_LEFT);
		setHorizontalStateMap(Half.BOTTOM, direction, StairsShape.OUTER_LEFT, placing, EnumShape.OUTSIDE_HORIZONTAL_LEFT);
		setHorizontalStateMap(Half.BOTTOM, direction, StairsShape.INNER_RIGHT, placing, EnumShape.INSIDE_RIGHT);
		setHorizontalStateMap(Half.BOTTOM, direction, StairsShape.OUTER_RIGHT, placing, EnumShape.OUTSIDE_HORIZONTAL_RIGHT);
		placing = EnumPlacing.forFacing(direction.getOpposite(), Direction.DOWN);
		setHorizontalStateMap(Half.TOP, direction, StairsShape.STRAIGHT, placing, EnumShape.STRAIGHT);
		setHorizontalStateMap(Half.TOP, direction, StairsShape.INNER_LEFT, placing, EnumShape.INSIDE_RIGHT);
		setHorizontalStateMap(Half.TOP, direction, StairsShape.OUTER_LEFT, placing, EnumShape.OUTSIDE_HORIZONTAL_RIGHT);
		setHorizontalStateMap(Half.TOP, direction, StairsShape.INNER_RIGHT, placing, EnumShape.INSIDE_LEFT);
		setHorizontalStateMap(Half.TOP, direction, StairsShape.OUTER_RIGHT, placing, EnumShape.OUTSIDE_HORIZONTAL_LEFT);
	}

	private static void buildVerticalStates(VerticalStairBlock.EnumPlacing direction)
	{
		EnumPlacing placing = EnumPlacing.forFacing(direction.counterClockWiseFront, direction.clockWiseFront);
		setVerticalStateMap(direction, VerticalStairBlock.EnumShape.STRAIGHT, placing, EnumShape.STRAIGHT);
		setVerticalStateMap(direction, VerticalStairBlock.EnumShape.INNER_UP, placing, EnumShape.INSIDE_RIGHT);
		setVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_UP, placing, EnumShape.OUTSIDE_RIGHT);
		setVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_FLAT_UP_CCW, placing, EnumShape.OUTSIDE_HORIZONTAL_RIGHT);
		setVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_FLAT_UP_CW, placing, EnumShape.OUTSIDE_VERTICAL_RIGHT);
		setVerticalStateMap(direction, VerticalStairBlock.EnumShape.INNER_DOWN, placing, EnumShape.INSIDE_LEFT);
		setVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_DOWN, placing, EnumShape.OUTSIDE_LEFT);
		setVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_FLAT_DOWN_CCW, placing, EnumShape.OUTSIDE_HORIZONTAL_LEFT);
		setVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_FLAT_DOWN_CW, placing, EnumShape.OUTSIDE_VERTICAL_LEFT);
		placing = EnumPlacing.forFacing(Direction.UP, direction.counterClockWiseFront);
		setVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_UP_FROM_CCW, placing, EnumShape.OUTSIDE_RIGHT);
		setVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_FLAT_UP_FROM_CCW, placing, EnumShape.OUTSIDE_VERTICAL_RIGHT);
		placing = EnumPlacing.forFacing(Direction.UP, direction.clockWiseFront);
		setVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_UP_FROM_CW, placing, EnumShape.OUTSIDE_LEFT);
		setVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_FLAT_UP_FROM_CW, placing, EnumShape.OUTSIDE_VERTICAL_LEFT);
		placing = EnumPlacing.forFacing(Direction.DOWN, direction.counterClockWiseFront);
		setVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_DOWN_FROM_CCW, placing, EnumShape.OUTSIDE_LEFT);
		setVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_FLAT_DOWN_FROM_CCW, placing, EnumShape.OUTSIDE_VERTICAL_LEFT);
		placing = EnumPlacing.forFacing(Direction.DOWN, direction.clockWiseFront);
		setVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_DOWN_FROM_CW, placing, EnumShape.OUTSIDE_RIGHT);
		setVerticalStateMap(direction, VerticalStairBlock.EnumShape.OUTER_FLAT_DOWN_FROM_CW, placing, EnumShape.OUTSIDE_VERTICAL_RIGHT);
	}

	private static void setPartialStateMap(EnumPlacing placing, EnumShape shape, PartialState state)
	{
		FULL_TO_PARTIAL[placing.ordinal()][shape.ordinal()] = state;
	}

	private static void setHorizontalStateMap(Half half, Direction facing, StairsShape shape, EnumPlacing fullPlacing, EnumShape fullShape)
	{
		setPartialStateMap(fullPlacing, fullShape, new PartialState.HorizontalState(half, facing, shape));
		HORIZONTAL_TO_FULL[half.ordinal()][facing.get2DDataValue()][shape.ordinal()] = Pair.of(fullPlacing, fullShape);
	}

	private static void setVerticalStateMap(VerticalStairBlock.EnumPlacing placing, VerticalStairBlock.EnumShape shape, EnumPlacing fullPlacing, EnumShape fullShape)
	{
		setPartialStateMap(fullPlacing, fullShape, new PartialState.VerticalState(placing, shape));
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
		if (blockState.getBlock() instanceof StairsBlock)
		{
			if (blockState.getValue(StairsBlock.HALF) == Half.BOTTOM) switch (blockState.getValue(StairsBlock.FACING))
			{
			case SOUTH: return EnumPlacing.UP_NORTH;
			case WEST: return EnumPlacing.UP_EAST;
			case NORTH: return EnumPlacing.UP_SOUTH;
			case EAST: return EnumPlacing.UP_WEST;
			default: return null;
			}
			else switch (blockState.getValue(StairsBlock.FACING))
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

	public static Pair<EnumPlacing, EnumShape> getFullState(BlockState blockState)
	{
		if (blockState.getBlock() instanceof StairsBlock) return getFullState(blockState.getValue(StairsBlock.HALF), blockState.getValue(StairsBlock.FACING), blockState.getValue(StairsBlock.SHAPE));
		else return getFullState(blockState.getValue(VerticalStairBlock.PLACING), blockState.getValue(VerticalStairBlock.SHAPE));
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
    		Vector3i rightV = front.getNormal().cross(top.getNormal());
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
