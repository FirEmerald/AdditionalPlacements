package com.firemerald.additionalplacements.util.stairs;

import com.firemerald.additionalplacements.util.ComplexFacing;

import net.minecraft.core.Direction;

public enum OldStairPlacing {
	NORTH_EAST("north_east", Direction.NORTH, Direction.EAST,
			ComplexFacing.NORTH_EAST, 
			ComplexFacing.NORTH_UP, ComplexFacing.EAST_UP,
			ComplexFacing.NORTH_DOWN, ComplexFacing.EAST_DOWN
			),
	EAST_SOUTH("east_south", Direction.EAST, Direction.SOUTH,
			ComplexFacing.EAST_SOUTH, 
			ComplexFacing.EAST_UP, ComplexFacing.SOUTH_UP,
			ComplexFacing.EAST_DOWN, ComplexFacing.SOUTH_DOWN
			),
	SOUTH_WEST("south_west", Direction.SOUTH, Direction.WEST,
			ComplexFacing.SOUTH_WEST, 
			ComplexFacing.SOUTH_UP, ComplexFacing.WEST_UP,
			ComplexFacing.SOUTH_DOWN, ComplexFacing.WEST_DOWN
			),
	WEST_NORTH("west_north", Direction.WEST, Direction.NORTH,
			ComplexFacing.WEST_NORTH, 
			ComplexFacing.WEST_UP, ComplexFacing.NORTH_UP,
			ComplexFacing.WEST_DOWN, ComplexFacing.NORTH_DOWN
			);
	
	public static OldStairPlacing get(String name) {
		for (OldStairPlacing placing : values()) if (placing.name.equals(name)) return placing;
		return null;
	}

    private final String name;
    public final Direction counterClockWiseFront, clockWiseFront, counterClockWiseBack, clockWiseBack;
    public final ComplexFacing equivalent, cwTop, ccwTop, cwBottom, ccwBottom;

    private OldStairPlacing(String name, Direction counterClockWise, Direction clockWise, ComplexFacing equivalent, ComplexFacing cwTop, ComplexFacing ccwTop, ComplexFacing cwBottom, ComplexFacing ccwBottom)
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
	public String toString()
    {
        return this.name;
    }
    
    public ComplexFacing equivalent(OldStairShape shape) {
    	if (shape.isUp) {
    		if (shape.isClockwise) {
    			return cwTop;
    		} else {
    			return ccwTop;
    		}
    	} else if (shape.isDown) {
    		if (shape.isClockwise) {
    			return cwBottom;
    		} else {
    			return ccwBottom;
    		}
    	}
    	else return equivalent;
    }
}
