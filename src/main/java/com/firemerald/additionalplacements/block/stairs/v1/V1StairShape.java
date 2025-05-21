package com.firemerald.additionalplacements.block.stairs.v1;

import com.firemerald.additionalplacements.block.stairs.common.CommonStairShape;

import net.minecraft.util.IStringSerializable;

public enum V1StairShape implements IStringSerializable {
    STRAIGHT("straight", V1StairPlacingType.NORMAL, CommonStairShape.STRAIGHT),

    OUTER_TWIST_CW("outer_twist_clockwise", V1StairPlacingType.NORMAL, CommonStairShape.OUTER_BACK_RIGHT_BOTTOM_LEFT),
    OUTER_TWIST_CCW("outer_twist_counter_clockwise", V1StairPlacingType.NORMAL, CommonStairShape.OUTER_BACK_LEFT_BOTTOM_RIGHT),


    INNER_UP("inner_up", V1StairPlacingType.NORMAL, CommonStairShape.INNER_BOTH_LEFT),

    OUTER_UP("outer_up", V1StairPlacingType.NORMAL, CommonStairShape.OUTER_BOTH_LEFT),
    OUTER_UP_FROM_CW("outer_up_from_clockwise", V1StairPlacingType.UP_CLOCKWISE, CommonStairShape.OUTER_BOTH_LEFT),
    OUTER_UP_FROM_CCW("outer_up_from_counter_clockwise", V1StairPlacingType.UP_COUNTER_CLOCKWISE, CommonStairShape.OUTER_BOTH_RIGHT),

    OUTER_FLAT_UP_CW("outer_flat_up_clockwise", V1StairPlacingType.NORMAL, CommonStairShape.OUTER_BACK_LEFT),
    OUTER_FLAT_UP_FROM_CW("outer_flat_up_from_clockwise", V1StairPlacingType.UP_CLOCKWISE, CommonStairShape.OUTER_BOTTOM_LEFT),

    OUTER_FLAT_UP_CCW("outer_flat_up_counter_clockwise", V1StairPlacingType.NORMAL, CommonStairShape.OUTER_BOTTOM_LEFT),
    OUTER_FLAT_UP_FROM_CCW("outer_flat_up_from_counter_clockwise", V1StairPlacingType.UP_COUNTER_CLOCKWISE, CommonStairShape.OUTER_BOTTOM_RIGHT),

    OUTER_TWIST_UP_CW("outer_twist_up_clockwise", V1StairPlacingType.UP_CLOCKWISE, CommonStairShape.OUTER_BACK_LEFT_BOTTOM_RIGHT),
    OUTER_TWIST_UP_CCW("outer_twist_up_counter_clockwise", V1StairPlacingType.UP_COUNTER_CLOCKWISE, CommonStairShape.OUTER_BACK_RIGHT_BOTTOM_LEFT),


    INNER_DOWN("inner_down", V1StairPlacingType.NORMAL, CommonStairShape.INNER_BOTH_RIGHT),

    OUTER_DOWN("outer_down", V1StairPlacingType.NORMAL, CommonStairShape.OUTER_BOTH_RIGHT),
    OUTER_DOWN_FROM_CW("outer_down_from_clockwise", V1StairPlacingType.DOWN_CLOCKWISE, CommonStairShape.OUTER_BOTH_RIGHT),
    OUTER_DOWN_FROM_CCW("outer_down_from_counter_clockwise", V1StairPlacingType.DOWN_COUNTER_CLOCKWISE, CommonStairShape.OUTER_BOTH_LEFT),

    OUTER_FLAT_DOWN_CW("outer_flat_down_clockwise", V1StairPlacingType.NORMAL, CommonStairShape.OUTER_BACK_RIGHT),
    OUTER_FLAT_DOWN_FROM_CW("outer_flat_down_from_clockwise", V1StairPlacingType.DOWN_CLOCKWISE, CommonStairShape.OUTER_BOTTOM_RIGHT),

    OUTER_FLAT_DOWN_CCW("outer_flat_down_counter_clockwise", V1StairPlacingType.NORMAL, CommonStairShape.OUTER_BOTTOM_RIGHT),
    OUTER_FLAT_DOWN_FROM_CCW("outer_flat_down_from_counter_clockwise", V1StairPlacingType.DOWN_COUNTER_CLOCKWISE, CommonStairShape.OUTER_BOTTOM_LEFT),

    OUTER_TWIST_DOWN_CW("outer_twist_down_clockwise", V1StairPlacingType.DOWN_CLOCKWISE, CommonStairShape.OUTER_BACK_RIGHT_BOTTOM_LEFT),
    OUTER_TWIST_DOWN_CCW("outer_twist_down_counter_clockwise", V1StairPlacingType.DOWN_COUNTER_CLOCKWISE, CommonStairShape.OUTER_BACK_LEFT_BOTTOM_RIGHT);
	
	public static V1StairShape get(String name) {
		for (V1StairShape shape : values()) if (shape.name.equals(name)) return shape;
		return null;
	}

    private final String name;
    public final V1StairPlacingType placingType;
    public final CommonStairShape equivalent;

    V1StairShape(String name, V1StairPlacingType placingType, CommonStairShape equivalent)
    {
        this.name = name;
        this.placingType = placingType;
        this.equivalent = equivalent;
    }

	@Override
	public String getSerializedName() {
		return name;
	}
}
