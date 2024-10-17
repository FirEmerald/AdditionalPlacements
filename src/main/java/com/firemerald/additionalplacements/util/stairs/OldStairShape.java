package com.firemerald.additionalplacements.util.stairs;

public enum OldStairShape {
    STRAIGHT("straight", false, false, false, false, StairShape.STRAIGHT),

    OUTER_TWIST_CW("outer_twist_clockwise", false, false, false, false, StairShape.OUTER_BACK_LEFT_BOTTOM_RIGHT),
    OUTER_TWIST_CCW("outer_twist_counter_clockwise", false, false, false, false, StairShape.OUTER_BACK_RIGHT_BOTTOM_LEFT),


    INNER_UP("inner_up", true, false, false, false, StairShape.INNER_BOTH_LEFT),

    OUTER_UP("outer_up", true, false, false, false, StairShape.OUTER_BOTH_LEFT),
    OUTER_UP_FROM_CW("outer_up_from_clockwise", true, false, true, false, StairShape.OUTER_BOTH_LEFT),
    OUTER_UP_FROM_CCW("outer_up_from_counter_clockwise", true, false, false, true, StairShape.OUTER_BOTH_RIGHT),

    OUTER_FLAT_UP_CW("outer_flat_up_clockwise", true, false, false, false, StairShape.OUTER_BACK_RIGHT),
    OUTER_FLAT_UP_FROM_CW("outer_flat_up_from_clockwise", true, false, true, false, StairShape.OUTER_BOTTOM_LEFT),

    OUTER_FLAT_UP_CCW("outer_flat_up_counter_clockwise", true, false, false, false, StairShape.OUTER_BOTTOM_LEFT),
    OUTER_FLAT_UP_FROM_CCW("outer_flat_up_from_counter_clockwise", true, false, false, true, StairShape.OUTER_BOTTOM_RIGHT),

    OUTER_TWIST_UP_CW("outer_twist_up_clockwise", true, false, true, false, StairShape.OUTER_BACK_LEFT_BOTTOM_RIGHT),
    OUTER_TWIST_UP_CCW("outer_twist_up_counter_clockwise", true, false, false, true, StairShape.OUTER_BACK_RIGHT_BOTTOM_LEFT),


    INNER_DOWN("inner_down", false, true, false, false, StairShape.INNER_BOTH_RIGHT),

    OUTER_DOWN("outer_down", false, true, false, false, StairShape.OUTER_BOTH_RIGHT),
    OUTER_DOWN_FROM_CW("outer_down_from_clockwise", false, true, true, false, StairShape.OUTER_BOTH_RIGHT),
    OUTER_DOWN_FROM_CCW("outer_down_from_counter_clockwise", false, true, false, true, StairShape.OUTER_BOTH_LEFT),

    OUTER_FLAT_DOWN_CW("outer_flat_down_clockwise", false, true, false, false, StairShape.OUTER_BOTTOM_RIGHT),
    OUTER_FLAT_DOWN_FROM_CW("outer_flat_down_from_clockwise", false, true, true, false, StairShape.OUTER_BOTTOM_RIGHT),

    OUTER_FLAT_DOWN_CCW("outer_flat_down_counter_clockwise", false, true, false, false, StairShape.OUTER_BOTTOM_RIGHT),
    OUTER_FLAT_DOWN_FROM_CCW("outer_flat_down_from_counter_clockwise", false, true, false, true, StairShape.OUTER_BOTTOM_LEFT),

    OUTER_TWIST_DOWN_CW("outer_twist_down_clockwise", false, true, true, false, StairShape.OUTER_BACK_LEFT_BOTTOM_RIGHT),
    OUTER_TWIST_DOWN_CCW("outer_twist_down_counter_clockwise", false, true, false, true, StairShape.OUTER_BACK_RIGHT_BOTTOM_LEFT);
	
	public static OldStairShape get(String name) {
		for (OldStairShape shape : values()) if (shape.name.equals(name)) return shape;
		return null;
	}

    private final String name;
    public final boolean isUp, isDown, isClockwise, isCounterClockwise;
    public final StairShape equivalent;

    private OldStairShape(String name, boolean isUp, boolean isDown, boolean isClockwise, boolean isCounterClockwise, StairShape equivalent)
    {
        this.name = name;
        this.isUp = isUp;
        this.isDown = isDown;
        this.isClockwise = isClockwise;
        this.isCounterClockwise = isCounterClockwise;
        this.equivalent = equivalent;
    }

    @Override
	public String toString()
    {
        return this.name;
    }
}
