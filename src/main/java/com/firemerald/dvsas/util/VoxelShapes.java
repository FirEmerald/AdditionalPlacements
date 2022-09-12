package com.firemerald.dvsas.util;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxelShapes
{
	public static final VoxelShape
	BLOCK = Block.box(0, 0, 0, 16, 16, 16), //NNN+PNN+NPN+PPN+NNP+PNP+NPP+PPP
	//side(s) of the block
	SLAB_WEST =  Block.box(0, 0, 0, 8, 16, 16), //NNN+NPN+NNP+NPP
	SLAB_EAST =  Block.box(8, 0, 0, 16, 16, 16), //PNN+PPN+PNP+PPP
	SLAB_DOWN =  Block.box(0, 0, 0, 16, 8, 16), //NNN+PNN+NNP+PNP
	SLAB_UP =    Block.box(0, 8, 0, 16, 16, 16), //NPN+PPN+NPP+PPP
	SLAB_NORTH = Block.box(0, 0, 0, 16, 16, 8), //NNN+PNN+NPN+PPN
	SLAB_SOUTH = Block.box(0, 0, 8, 16, 16, 16), //NNP+PNP+NPP+PPP

	PILLAR_WEST_DOWN = Block.box(0, 0, 0, 8, 8, 16), //NNN+NNP
	PILLAR_EAST_DOWN = Block.box(8, 0, 0, 16, 8, 16), //PNN+PNP
	PILLAR_WEST_UP = Block.box(0, 8, 0, 8, 16, 16), //NPN+NPP
	PILLAR_EAST_UP = Block.box(8, 8, 0, 16, 16, 16), //PPN+PPP

	PILLAR_WEST_NORTH = Block.box(0, 0, 0, 8, 16, 8), //NNN+NPN
	PILLAR_EAST_NORTH = Block.box(8, 0, 0, 16, 16, 8), //PNN+PPN
	PILLAR_WEST_SOUTH = Block.box(0, 0, 8, 8, 16, 16), //NNP+NPP
	PILLAR_EAST_SOUTH = Block.box(8, 0, 8, 16, 16, 16), //PNP+PPP

	PILLAR_DOWN_NORTH = Block.box(0, 0, 0, 16, 8, 8), //NNN+PNN
	PILLAR_UP_NORTH = Block.box(0, 8, 0, 16, 16, 8), //NPN+PPN
	PILLAR_DOWN_SOUTH = Block.box(0, 0, 8, 16, 8, 16), //NNP+PNP
	PILLAR_UP_SOUTH = Block.box(0, 8, 8, 16, 16, 16), //NPP+PPP

	CORNER_WEST_DOWN_NORTH = Block.box(0, 0, 0, 8, 8, 8), //NNN
	CORNER_EAST_DOWN_NORTH = Block.box(8, 0, 0, 16, 8, 8), //PNN
	CORNER_WEST_UP_NORTH = Block.box(0, 8, 0, 8, 16, 8), //NPN
	CORNER_EAST_UP_NORTH = Block.box(8, 8, 0, 16, 16, 8), //PPN
	CORNER_WEST_DOWN_SOUTH = Block.box(0, 0, 8, 8, 8, 16), //NNP
	CORNER_EAST_DOWN_SOUTH = Block.box(8, 0, 8, 16, 8, 16), //PNP
	CORNER_WEST_UP_SOUTH = Block.box(0, 8, 8, 8, 16, 16), //NPP
	CORNER_EAST_UP_SOUTH = Block.box(8, 8, 8, 16, 16, 16), //PPP

	//facing
	STAIRS_UP_SOUTH = Shapes.or(SLAB_DOWN, PILLAR_UP_NORTH), //NNN+PNN+NNP+PNP+NPN+PPN
	STAIRS_UP_NORTH = Shapes.or(SLAB_DOWN, PILLAR_UP_SOUTH), //NNN+PNN+NNP+PNP+NPP+PPP
	STAIRS_DOWN_SOUTH = Shapes.or(SLAB_UP, PILLAR_DOWN_NORTH), //NPN+PPN+NPP+PPP+NNN+PNN
	STAIRS_DOWN_NORTH = Shapes.or(SLAB_UP, PILLAR_DOWN_SOUTH), //NPN+PPN+NPP+PPP+NNP+PNP
	STAIRS_UP_EAST = Shapes.or(SLAB_DOWN, PILLAR_WEST_UP), //NNN+PNN+NNP+PNP+NPN+NPP
	STAIRS_UP_WEST = Shapes.or(SLAB_DOWN, PILLAR_EAST_UP), //NNN+PNN+NNP+PNP+PPN+PPP
	STAIRS_DOWN_EAST = Shapes.or(SLAB_UP, PILLAR_WEST_DOWN), //NPN+PPN+NPP+PPP+NNN+NNP
	STAIRS_DOWN_WEST = Shapes.or(SLAB_UP, PILLAR_EAST_DOWN), //NPN+PPN+NPP+PPP+PNN+PNP
	STAIRS_EAST_SOUTH = Shapes.or(SLAB_WEST, PILLAR_EAST_NORTH), //NNN+NPN+NNP+NPP+PNN+PPN
	STAIRS_EAST_NORTH = Shapes.or(SLAB_WEST, PILLAR_EAST_SOUTH), //NNN+NPN+NNP+NPP+PNP+PPP
	STAIRS_WEST_SOUTH = Shapes.or(SLAB_EAST, PILLAR_WEST_NORTH), //PNN+PPN+PNP+PPP+NNN+NPN
	STAIRS_WEST_NORTH = Shapes.or(SLAB_EAST, PILLAR_WEST_SOUTH), //PNN+PPN+PNP+PPP+NNP+NPP
	//12
	STAIRS_INNER_EAST_UP_SOUTH = Shapes.join(BLOCK, CORNER_EAST_UP_SOUTH, BooleanOp.ONLY_FIRST), //NNN+PNN+NPN+PPN+NNP+PNP+NPP
	STAIRS_INNER_WEST_UP_SOUTH = Shapes.join(BLOCK, CORNER_WEST_UP_SOUTH, BooleanOp.ONLY_FIRST), //NNN+PNN+NPN+PPN+NNP+PNP+PPP
	STAIRS_INNER_EAST_DOWN_SOUTH = Shapes.join(BLOCK, CORNER_EAST_DOWN_SOUTH, BooleanOp.ONLY_FIRST), //NNN+PNN+NPN+PPN+NNP+NPP+PPP
	STAIRS_INNER_WEST_DOWN_SOUTH = Shapes.join(BLOCK, CORNER_WEST_DOWN_SOUTH, BooleanOp.ONLY_FIRST), //NNN+PNN+NPN+PPN+PNP+NPP+PPP
	STAIRS_INNER_EAST_UP_NORTH = Shapes.join(BLOCK, CORNER_EAST_UP_NORTH, BooleanOp.ONLY_FIRST), //NNN+PNN+NPN+NNP+PNP+NPP+PPP
	STAIRS_INNER_WEST_UP_NORTH = Shapes.join(BLOCK, CORNER_WEST_UP_NORTH, BooleanOp.ONLY_FIRST), //NNN+PNN+PPN+NNP+PNP+NPP+PPP
	STAIRS_INNER_EAST_DOWN_NORTH = Shapes.join(BLOCK, CORNER_EAST_DOWN_NORTH, BooleanOp.ONLY_FIRST), //NNN+NPN+PPN+NNP+PNP+NPP+PPP
	STAIRS_INNER_WEST_DOWN_NORTH = Shapes.join(BLOCK, CORNER_WEST_DOWN_NORTH, BooleanOp.ONLY_FIRST), //PNN+NPN+PPN+NNP+PNP+NPP+PPP
	//20
	STAIRS_OUTER_EAST_UP_SOUTH = Shapes.or(PILLAR_DOWN_NORTH, CORNER_WEST_UP_NORTH, CORNER_WEST_DOWN_SOUTH), //NNN+PNN+NPN+NNP
	STAIRS_OUTER_WEST_UP_SOUTH = Shapes.or(PILLAR_DOWN_NORTH, CORNER_EAST_UP_NORTH, CORNER_EAST_DOWN_SOUTH), //NNN+PNN+PPN+PNP
	STAIRS_OUTER_EAST_DOWN_SOUTH = Shapes.or(PILLAR_UP_NORTH, CORNER_WEST_DOWN_NORTH, CORNER_WEST_UP_SOUTH), //NNN+NPN+PPN+NPP
	STAIRS_OUTER_WEST_DOWN_SOUTH = Shapes.or(PILLAR_UP_NORTH, CORNER_EAST_DOWN_NORTH, CORNER_EAST_UP_SOUTH), //PNN+NPN+PPN+PPP
	STAIRS_OUTER_EAST_UP_NORTH = Shapes.or(PILLAR_DOWN_SOUTH, CORNER_WEST_UP_SOUTH, CORNER_WEST_DOWN_NORTH), //NNN+NNP+PNP+NPP
	STAIRS_OUTER_WEST_UP_NORTH = Shapes.or(PILLAR_DOWN_SOUTH, CORNER_EAST_UP_SOUTH, CORNER_EAST_DOWN_NORTH), //PNN+NNP+PNP+PPP
	STAIRS_OUTER_EAST_DOWN_NORTH = Shapes.or(PILLAR_UP_SOUTH, CORNER_WEST_DOWN_SOUTH, CORNER_WEST_UP_NORTH), //NPN+NNP+NPP+PPP
	STAIRS_OUTER_WEST_DOWN_NORTH = Shapes.or(PILLAR_UP_SOUTH, CORNER_EAST_DOWN_SOUTH, CORNER_EAST_UP_NORTH), //PPN+PNP+NPP+PPP
	//28
	STAIRS_FLAT_EAST_UP_SOUTH = Shapes.or(SLAB_WEST, CORNER_EAST_DOWN_NORTH),
	STAIRS_FLAT_EAST_DOWN_SOUTH = Shapes.or(SLAB_WEST, CORNER_EAST_UP_NORTH),
	STAIRS_FLAT_EAST_UP_NORTH = Shapes.or(SLAB_WEST, CORNER_EAST_DOWN_SOUTH),
	STAIRS_FLAT_EAST_DOWN_NORTH = Shapes.or(SLAB_WEST, CORNER_EAST_UP_SOUTH),
	STAIRS_FLAT_WEST_UP_SOUTH = Shapes.or(SLAB_EAST, CORNER_WEST_DOWN_NORTH),
	STAIRS_FLAT_WEST_DOWN_SOUTH = Shapes.or(SLAB_EAST, CORNER_WEST_UP_NORTH),
	STAIRS_FLAT_WEST_UP_NORTH = Shapes.or(SLAB_EAST, CORNER_WEST_DOWN_SOUTH),
	STAIRS_FLAT_WEST_DOWN_NORTH = Shapes.or(SLAB_EAST, CORNER_WEST_UP_SOUTH),
	//36
	STAIRS_FLAT_UP_EAST_SOUTH = Shapes.or(SLAB_DOWN, CORNER_WEST_UP_NORTH),
	STAIRS_FLAT_UP_WEST_SOUTH = Shapes.or(SLAB_DOWN, CORNER_EAST_UP_NORTH),
	STAIRS_FLAT_UP_EAST_NORTH = Shapes.or(SLAB_DOWN, CORNER_WEST_UP_SOUTH),
	STAIRS_FLAT_UP_WEST_NORTH = Shapes.or(SLAB_DOWN, CORNER_EAST_UP_SOUTH),
	STAIRS_FLAT_DOWN_EAST_SOUTH = Shapes.or(SLAB_UP, CORNER_WEST_DOWN_NORTH),
	STAIRS_FLAT_DOWN_WEST_SOUTH = Shapes.or(SLAB_UP, CORNER_EAST_DOWN_NORTH),
	STAIRS_FLAT_DOWN_EAST_NORTH = Shapes.or(SLAB_UP, CORNER_WEST_DOWN_SOUTH),
	STAIRS_FLAT_DOWN_WEST_NORTH = Shapes.or(SLAB_UP, CORNER_EAST_DOWN_SOUTH),
	//44
	STAIRS_FLAT_SOUTH_EAST_UP = Shapes.or(SLAB_NORTH, CORNER_WEST_DOWN_SOUTH),
	STAIRS_FLAT_SOUTH_WEST_UP = Shapes.or(SLAB_NORTH, CORNER_EAST_DOWN_SOUTH),
	STAIRS_FLAT_SOUTH_EAST_DOWN = Shapes.or(SLAB_NORTH, CORNER_WEST_UP_SOUTH),
	STAIRS_FLAT_SOUTH_WEST_DOWN = Shapes.or(SLAB_NORTH, CORNER_EAST_UP_SOUTH),
	STAIRS_FLAT_NORTH_EAST_UP = Shapes.or(SLAB_SOUTH, CORNER_WEST_DOWN_NORTH),
	STAIRS_FLAT_NORTH_WEST_UP = Shapes.or(SLAB_SOUTH, CORNER_EAST_DOWN_NORTH),
	STAIRS_FLAT_NORTH_EAST_DOWN = Shapes.or(SLAB_SOUTH, CORNER_WEST_UP_NORTH),
	STAIRS_FLAT_NORTH_WEST_DOWN = Shapes.or(SLAB_SOUTH, CORNER_EAST_UP_NORTH);
	//52
	private static final VoxelShape[] SLABS_BY_SIDE = new VoxelShape[] {
			SLAB_DOWN,
			SLAB_UP,
			SLAB_NORTH,
			SLAB_SOUTH,
			SLAB_WEST,
			SLAB_EAST
	};

	private static final VoxelShape[][] PILLARS_BY_SIDE = new VoxelShape[6][4];
	private static final VoxelShape[][][] CORNERS_BY_SIDE = new VoxelShape[6][4][2];
	private static final VoxelShape[][] STRAIGHT_STAIRS_BY_FACING = new VoxelShape[6][4];
	private static final VoxelShape[][][] INNER_STAIRS_BY_FACING = new VoxelShape[6][4][2];
	private static final VoxelShape[][][] OUTER_STAIRS_BY_FACING = new VoxelShape[6][4][2];
	private static final VoxelShape[][][] OUTER_FLAT_STAIRS_BY_TOP_AND_FACING = new VoxelShape[6][4][2];

	static
	{
		setPillar(Direction.WEST, Direction.DOWN, PILLAR_WEST_DOWN);
		setPillar(Direction.EAST, Direction.DOWN, PILLAR_EAST_DOWN);
		setPillar(Direction.WEST, Direction.UP, PILLAR_WEST_UP);
		setPillar(Direction.EAST, Direction.UP, PILLAR_EAST_UP);
		setPillar(Direction.WEST, Direction.NORTH, PILLAR_WEST_NORTH);
		setPillar(Direction.EAST, Direction.NORTH, PILLAR_EAST_NORTH);
		setPillar(Direction.WEST, Direction.SOUTH, PILLAR_WEST_SOUTH);
		setPillar(Direction.EAST, Direction.SOUTH, PILLAR_EAST_SOUTH);
		setPillar(Direction.DOWN, Direction.NORTH, PILLAR_DOWN_NORTH);
		setPillar(Direction.UP, Direction.NORTH, PILLAR_UP_NORTH);
		setPillar(Direction.DOWN, Direction.SOUTH, PILLAR_DOWN_SOUTH);
		setPillar(Direction.UP, Direction.SOUTH, PILLAR_UP_SOUTH);
		setCorner(Direction.WEST, Direction.DOWN, Direction.NORTH, CORNER_WEST_DOWN_NORTH);
		setCorner(Direction.EAST, Direction.DOWN, Direction.NORTH, CORNER_EAST_DOWN_NORTH);
		setCorner(Direction.WEST, Direction.UP, Direction.NORTH, CORNER_WEST_UP_NORTH);
		setCorner(Direction.EAST, Direction.UP, Direction.NORTH, CORNER_EAST_UP_NORTH);
		setCorner(Direction.WEST, Direction.DOWN, Direction.SOUTH, CORNER_WEST_DOWN_SOUTH);
		setCorner(Direction.EAST, Direction.DOWN, Direction.SOUTH, CORNER_EAST_DOWN_SOUTH);
		setCorner(Direction.WEST, Direction.UP, Direction.SOUTH, CORNER_WEST_UP_SOUTH);
		setCorner(Direction.EAST, Direction.UP, Direction.SOUTH, CORNER_EAST_UP_SOUTH);
		setStraightStairs(Direction.UP, Direction.SOUTH, STAIRS_UP_SOUTH);
		setStraightStairs(Direction.UP, Direction.NORTH, STAIRS_UP_NORTH);
		setStraightStairs(Direction.DOWN, Direction.SOUTH, STAIRS_DOWN_SOUTH);
		setStraightStairs(Direction.DOWN, Direction.NORTH, STAIRS_DOWN_NORTH);
		setStraightStairs(Direction.UP, Direction.EAST, STAIRS_UP_EAST);
		setStraightStairs(Direction.UP, Direction.WEST, STAIRS_UP_WEST);
		setStraightStairs(Direction.DOWN, Direction.EAST, STAIRS_DOWN_EAST);
		setStraightStairs(Direction.DOWN, Direction.WEST, STAIRS_DOWN_WEST);
		setStraightStairs(Direction.EAST, Direction.SOUTH, STAIRS_EAST_SOUTH);
		setStraightStairs(Direction.EAST, Direction.NORTH, STAIRS_EAST_NORTH);
		setStraightStairs(Direction.WEST, Direction.SOUTH, STAIRS_WEST_SOUTH);
		setStraightStairs(Direction.WEST, Direction.NORTH, STAIRS_WEST_NORTH);
		setInnerStairs(Direction.EAST, Direction.UP, Direction.SOUTH, STAIRS_INNER_EAST_UP_SOUTH);
		setInnerStairs(Direction.WEST, Direction.UP, Direction.SOUTH, STAIRS_INNER_WEST_UP_SOUTH);
		setInnerStairs(Direction.EAST, Direction.DOWN, Direction.SOUTH, STAIRS_INNER_EAST_DOWN_SOUTH);
		setInnerStairs(Direction.WEST, Direction.DOWN, Direction.SOUTH, STAIRS_INNER_WEST_DOWN_SOUTH);
		setInnerStairs(Direction.EAST, Direction.UP, Direction.NORTH, STAIRS_INNER_EAST_UP_NORTH);
		setInnerStairs(Direction.WEST, Direction.UP, Direction.NORTH, STAIRS_INNER_WEST_UP_NORTH);
		setInnerStairs(Direction.EAST, Direction.DOWN, Direction.NORTH, STAIRS_INNER_EAST_DOWN_NORTH);
		setInnerStairs(Direction.WEST, Direction.DOWN, Direction.NORTH, STAIRS_INNER_WEST_DOWN_NORTH);
		setOuterStairs(Direction.EAST, Direction.UP, Direction.SOUTH, STAIRS_OUTER_EAST_UP_SOUTH);
		setOuterStairs(Direction.WEST, Direction.UP, Direction.SOUTH, STAIRS_OUTER_WEST_UP_SOUTH);
		setOuterStairs(Direction.EAST, Direction.DOWN, Direction.SOUTH, STAIRS_OUTER_EAST_DOWN_SOUTH);
		setOuterStairs(Direction.WEST, Direction.DOWN, Direction.SOUTH, STAIRS_OUTER_WEST_DOWN_SOUTH);
		setOuterStairs(Direction.EAST, Direction.UP, Direction.NORTH, STAIRS_OUTER_EAST_UP_NORTH);
		setOuterStairs(Direction.WEST, Direction.UP, Direction.NORTH, STAIRS_OUTER_WEST_UP_NORTH);
		setOuterStairs(Direction.EAST, Direction.DOWN, Direction.NORTH, STAIRS_OUTER_EAST_DOWN_NORTH);
		setOuterStairs(Direction.WEST, Direction.DOWN, Direction.NORTH, STAIRS_OUTER_WEST_DOWN_NORTH);
		setOuterFlatStairs(Direction.EAST, Direction.UP, Direction.SOUTH, STAIRS_FLAT_EAST_UP_SOUTH);
		setOuterFlatStairs(Direction.EAST, Direction.DOWN, Direction.SOUTH, STAIRS_FLAT_EAST_DOWN_SOUTH);
		setOuterFlatStairs(Direction.EAST, Direction.UP, Direction.NORTH, STAIRS_FLAT_EAST_UP_NORTH);
		setOuterFlatStairs(Direction.EAST, Direction.DOWN, Direction.NORTH, STAIRS_FLAT_EAST_DOWN_NORTH);
		setOuterFlatStairs(Direction.WEST, Direction.UP, Direction.SOUTH, STAIRS_FLAT_WEST_UP_SOUTH);
		setOuterFlatStairs(Direction.WEST, Direction.DOWN, Direction.SOUTH, STAIRS_FLAT_WEST_DOWN_SOUTH);
		setOuterFlatStairs(Direction.WEST, Direction.UP, Direction.NORTH, STAIRS_FLAT_WEST_UP_NORTH);
		setOuterFlatStairs(Direction.WEST, Direction.DOWN, Direction.NORTH, STAIRS_FLAT_WEST_DOWN_NORTH);
		setOuterFlatStairs(Direction.UP, Direction.EAST, Direction.SOUTH, STAIRS_FLAT_UP_EAST_SOUTH);
		setOuterFlatStairs(Direction.UP, Direction.WEST, Direction.SOUTH, STAIRS_FLAT_UP_WEST_SOUTH);
		setOuterFlatStairs(Direction.UP, Direction.EAST, Direction.NORTH, STAIRS_FLAT_UP_EAST_NORTH);
		setOuterFlatStairs(Direction.UP, Direction.WEST, Direction.NORTH, STAIRS_FLAT_UP_WEST_NORTH);
		setOuterFlatStairs(Direction.DOWN, Direction.EAST, Direction.SOUTH, STAIRS_FLAT_DOWN_EAST_SOUTH);
		setOuterFlatStairs(Direction.DOWN, Direction.WEST, Direction.SOUTH, STAIRS_FLAT_DOWN_WEST_SOUTH);
		setOuterFlatStairs(Direction.DOWN, Direction.EAST, Direction.NORTH, STAIRS_FLAT_DOWN_EAST_NORTH);
		setOuterFlatStairs(Direction.DOWN, Direction.WEST, Direction.NORTH, STAIRS_FLAT_DOWN_WEST_NORTH);
		setOuterFlatStairs(Direction.SOUTH, Direction.EAST, Direction.UP, STAIRS_FLAT_SOUTH_EAST_UP);
		setOuterFlatStairs(Direction.SOUTH, Direction.WEST, Direction.UP, STAIRS_FLAT_SOUTH_WEST_UP);
		setOuterFlatStairs(Direction.SOUTH, Direction.EAST, Direction.DOWN, STAIRS_FLAT_SOUTH_EAST_DOWN);
		setOuterFlatStairs(Direction.SOUTH, Direction.WEST, Direction.DOWN, STAIRS_FLAT_SOUTH_WEST_DOWN);
		setOuterFlatStairs(Direction.NORTH, Direction.EAST, Direction.UP, STAIRS_FLAT_NORTH_EAST_UP);
		setOuterFlatStairs(Direction.NORTH, Direction.WEST, Direction.UP, STAIRS_FLAT_NORTH_WEST_UP);
		setOuterFlatStairs(Direction.NORTH, Direction.EAST, Direction.DOWN, STAIRS_FLAT_NORTH_EAST_DOWN);
		setOuterFlatStairs(Direction.NORTH, Direction.WEST, Direction.DOWN, STAIRS_FLAT_NORTH_WEST_DOWN);
	}

	private static int getIndex(Direction dir)
	{
		return dir.ordinal();
	}

	private static int getIndex(Direction dir, Direction ignored)
	{
		int index = getIndex(dir);
		int index2 = getIndex(ignored);
		if (index > index2) return index - 2;
		else return index;
	}

	private static int getIndex(Direction dir, Direction ignored1, Direction ignored2)
	{
		int index = getIndex(dir);
		int index2 = getIndex(ignored1);
		int index3 = getIndex(ignored2);
		if (index > index2)
		{
			if (index > index3) return index - 4;
			else return index - 2;
		}
		else
		{
			if (index > index3) return index - 2;
			else return index;
		}
	}

	private static void set(VoxelShape[][] array, Direction side1, Direction side2, VoxelShape shape)
	{
		array[getIndex(side1)][getIndex(side2, side1)] =
		array[getIndex(side2)][getIndex(side1, side2)] = shape;
	}

	private static VoxelShape get(VoxelShape[][] array, Direction side1, Direction side2)
	{
		return array[getIndex(side1)][getIndex(side2, side1)];
	}

	private static void set(VoxelShape[][] array, Direction ignore, Direction side1, Direction side2, VoxelShape shape)
	{
		array[getIndex(side1, ignore)][getIndex(side2, ignore, side1)] =
		array[getIndex(side2, ignore)][getIndex(side1, ignore, side2)] = shape;
	}

	private static VoxelShape get(VoxelShape[][] array, Direction ignore, Direction side1, Direction side2)
	{
		return array[getIndex(side1, ignore)][getIndex(side2, ignore, side1)];
	}

	private static void set(VoxelShape[][][] array, Direction side1, Direction side2, Direction side3, VoxelShape shape)
	{
		array[getIndex(side1)][getIndex(side2, side1)][getIndex(side3, side1, side2)] =
		array[getIndex(side2)][getIndex(side3, side2)][getIndex(side1, side2, side3)] =
		array[getIndex(side3)][getIndex(side1, side3)][getIndex(side2, side3, side1)] =
		array[getIndex(side3)][getIndex(side2, side3)][getIndex(side1, side3, side2)] =
		array[getIndex(side1)][getIndex(side3, side1)][getIndex(side2, side1, side3)] =
		array[getIndex(side2)][getIndex(side1, side2)][getIndex(side3, side2, side1)] = shape;
	}

	private static VoxelShape get(VoxelShape[][][] array, Direction side1, Direction side2, Direction side3)
	{
		return array[getIndex(side1)][getIndex(side2, side1)][getIndex(side3, side1, side2)];
	}

	public static VoxelShape getSlab(Direction side)
	{
		return SLABS_BY_SIDE[getIndex(side)];
	}

	private static void setPillar(Direction side1, Direction side2, VoxelShape shape)
	{
		set(PILLARS_BY_SIDE, side1, side2, shape);
	}

	public static VoxelShape getPillar(Direction side1, Direction side2)
	{
		return get(PILLARS_BY_SIDE, side1, side2);
	}

	private static void setCorner(Direction side1, Direction side2, Direction side3, VoxelShape shape)
	{
		set(CORNERS_BY_SIDE, side1, side2, side3, shape);
	}

	public static VoxelShape getCorner(Direction side1, Direction side2, Direction side3)
	{
		return get(CORNERS_BY_SIDE, side1, side2, side3);
	}

	private static void setStraightStairs(Direction face1, Direction face2, VoxelShape shape)
	{
		set(STRAIGHT_STAIRS_BY_FACING, face1, face2, shape);
	}

	public static VoxelShape getStraightStairs(Direction face1, Direction face2)
	{
		return get(STRAIGHT_STAIRS_BY_FACING, face1, face2);
	}

	private static void setInnerStairs(Direction face1, Direction face2, Direction face3, VoxelShape shape)
	{
		set(INNER_STAIRS_BY_FACING, face1, face2, face3, shape);
	}

	public static VoxelShape getInnerStairs(Direction face1, Direction face2, Direction face3)
	{
		return get(INNER_STAIRS_BY_FACING, face1, face2, face3);
	}

	private static void setOuterStairs(Direction face1, Direction face2, Direction face3, VoxelShape shape)
	{
		set(OUTER_STAIRS_BY_FACING, face1, face2, face3, shape);
	}

	public static VoxelShape getOuterStairs(Direction face1, Direction face2, Direction face3)
	{
		return get(OUTER_STAIRS_BY_FACING, face1, face2, face3);
	}

	private static void setOuterFlatStairs(Direction top, Direction face1, Direction face2, VoxelShape shape)
	{
		set(OUTER_FLAT_STAIRS_BY_TOP_AND_FACING[getIndex(top)], top, face1, face2, shape);
	}

	public static VoxelShape getOuterFlatStairs(Direction top, Direction face1, Direction face2)
	{
		return get(OUTER_FLAT_STAIRS_BY_TOP_AND_FACING[getIndex(top)], top, face1, face2);
	}
}