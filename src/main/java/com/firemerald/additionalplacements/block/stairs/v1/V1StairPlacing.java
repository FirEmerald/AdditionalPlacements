package com.firemerald.additionalplacements.block.stairs.v1;

import com.firemerald.additionalplacements.util.ComplexFacing;

import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;

public enum V1StairPlacing implements IStringSerializable {
	NORTH_EAST("north_east", Direction.NORTH, Direction.EAST,
			ComplexFacing.NORTH_EAST, 
			ComplexFacing.EAST_UP, ComplexFacing.NORTH_UP, 
			ComplexFacing.EAST_DOWN, ComplexFacing.NORTH_DOWN
			),
	EAST_SOUTH("east_south", Direction.EAST, Direction.SOUTH,
			ComplexFacing.EAST_SOUTH, 
			ComplexFacing.SOUTH_UP, ComplexFacing.EAST_UP, 
			ComplexFacing.SOUTH_DOWN, ComplexFacing.EAST_DOWN
			),
	SOUTH_WEST("south_west", Direction.SOUTH, Direction.WEST,
			ComplexFacing.SOUTH_WEST, 
			ComplexFacing.WEST_UP, ComplexFacing.SOUTH_UP, 
			ComplexFacing.WEST_DOWN, ComplexFacing.SOUTH_DOWN
			),
	WEST_NORTH("west_north", Direction.WEST, Direction.NORTH,
			ComplexFacing.WEST_NORTH, 
			ComplexFacing.NORTH_UP, ComplexFacing.WEST_UP, 
			ComplexFacing.NORTH_DOWN, ComplexFacing.WEST_DOWN
			);
	
	public static V1StairPlacing get(String name) {
		for (V1StairPlacing placing : values()) if (placing.name.equals(name)) return placing;
		return null;
	}

    private final String name;
    public final Direction counterClockWiseFront, clockWiseFront, counterClockWiseBack, clockWiseBack;
    public final ComplexFacing equivalent, cwTop, ccwTop, cwBottom, ccwBottom;

    V1StairPlacing(String name, Direction counterClockWise, Direction clockWise, ComplexFacing equivalent, ComplexFacing cwTop, ComplexFacing ccwTop, ComplexFacing cwBottom, ComplexFacing ccwBottom)
    {
        this.name = name;
        this.counterClockWiseFront = counterClockWise;
        this.clockWiseFront = clockWise;
        this.counterClockWiseBack = counterClockWise.getOpposite();
        this.clockWiseBack = clockWise.getOpposite();
        this.equivalent = equivalent;
        this.cwTop = cwTop;
        this.ccwTop = ccwTop;
        this.cwBottom = cwBottom;
        this.ccwBottom = ccwBottom;
    }

	@Override
	public String getSerializedName() {
		return name;
	}
}
