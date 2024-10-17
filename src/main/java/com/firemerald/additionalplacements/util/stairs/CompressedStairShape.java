package com.firemerald.additionalplacements.util.stairs;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.phys.shapes.VoxelShape;

public enum CompressedStairShape implements StringRepresentable {
	//NORMAL_STRAIGHT("normal_straight", StairFacingType.NORMAL, StairShape.STRAIGHT),

	//NORMAL_INNER_FRONT_LEFT("normal_inner_front_left", StairFacingType.NORMAL, StairShape.INNER_FRONT_LEFT),
	//NORMAL_INNER_FRONT_RIGHT("normal_inner_front_right", StairFacingType.NORMAL, StairShape.INNER_FRONT_RIGHT),
	NORMAL_INNER_TOP_LEFT("normal_inner_top_left", StairFacingType.NORMAL, StairShape.INNER_TOP_LEFT),
	NORMAL_INNER_TOP_RIGHT("normal_inner_top_right", StairFacingType.NORMAL, StairShape.INNER_TOP_RIGHT),
	NORMAL_INNER_BOTH_LEFT("normal_inner_both_left", StairFacingType.NORMAL, StairShape.INNER_BOTH_LEFT),
	NORMAL_INNER_BOTH_RIGHT("normal_inner_both_right", StairFacingType.NORMAL, StairShape.INNER_BOTH_RIGHT),
	
	//NORMAL_OUTER_BACK_LEFT("normal_outer_back_left", StairFacingType.NORMAL, StairShape.OUTER_BACK_LEFT),
	//NORMAL_OUTER_BACK_RIGHT("normal_outer_back_right", StairFacingType.NORMAL, StairShape.OUTER_BACK_RIGHT),
	NORMAL_OUTER_BOTTOM_LEFT("normal_outer_bottom_left", StairFacingType.NORMAL, StairShape.OUTER_BOTTOM_LEFT),
	NORMAL_OUTER_BOTTOM_RIGHT("normal_outer_bottom_right", StairFacingType.NORMAL, StairShape.OUTER_BOTTOM_RIGHT),
	NORMAL_OUTER_BOTH_LEFT("normal_outer_both_left", StairFacingType.NORMAL, StairShape.OUTER_BOTH_LEFT),
	NORMAL_OUTER_BOTH_RIGHT("normal_outer_both_right", StairFacingType.NORMAL, StairShape.OUTER_BOTH_RIGHT),

	NORMAL_OUTER_BACK_LEFT_BOTTOM_RIGHT("normal_outer_back_left_bottom_right", StairFacingType.NORMAL, StairShape.OUTER_BACK_LEFT_BOTTOM_RIGHT),
	NORMAL_OUTER_BACK_RIGHT_BOTTOM_LEFT("normal_outer_back_right_bottom_left", StairFacingType.NORMAL, StairShape.OUTER_BACK_RIGHT_BOTTOM_LEFT),
	

	FLIPPED_STRAIGHT("flipped_straight", StairFacingType.FLIPPED, StairShape.STRAIGHT),

	FLIPPED_INNER_FRONT_LEFT("flipped_inner_front_left", StairFacingType.FLIPPED, StairShape.INNER_FRONT_LEFT),
	FLIPPED_INNER_FRONT_RIGHT("flipped_inner_front_right", StairFacingType.FLIPPED, StairShape.INNER_FRONT_RIGHT),
	FLIPPED_INNER_TOP_LEFT("flipped_inner_top_left", StairFacingType.FLIPPED, StairShape.INNER_TOP_LEFT),
	FLIPPED_INNER_TOP_RIGHT("flipped_inner_top_right", StairFacingType.FLIPPED, StairShape.INNER_TOP_RIGHT),
	FLIPPED_INNER_BOTH_LEFT("flipped_inner_both_left", StairFacingType.FLIPPED, StairShape.INNER_BOTH_LEFT),
	FLIPPED_INNER_BOTH_RIGHT("flipped_inner_both_right", StairFacingType.FLIPPED, StairShape.INNER_BOTH_RIGHT),
	
	FLIPPED_OUTER_BACK_LEFT("flipped_outer_back_left", StairFacingType.FLIPPED, StairShape.OUTER_BACK_LEFT),
	FLIPPED_OUTER_BACK_RIGHT("flipped_outer_back_right", StairFacingType.FLIPPED, StairShape.OUTER_BACK_RIGHT),
	FLIPPED_OUTER_BOTTOM_LEFT("flipped_outer_bottom_left", StairFacingType.FLIPPED, StairShape.OUTER_BOTTOM_LEFT),
	FLIPPED_OUTER_BOTTOM_RIGHT("flipped_outer_bottom_right", StairFacingType.FLIPPED, StairShape.OUTER_BOTTOM_RIGHT),
	FLIPPED_OUTER_BOTH_LEFT("flipped_outer_both_left", StairFacingType.FLIPPED, StairShape.OUTER_BOTH_LEFT),
	FLIPPED_OUTER_BOTH_RIGHT("flipped_outer_both_right", StairFacingType.FLIPPED, StairShape.OUTER_BOTH_RIGHT),

	FLIPPED_OUTER_BACK_LEFT_BOTTOM_RIGHT("flipped_outer_back_left_bottom_right", StairFacingType.FLIPPED, StairShape.OUTER_BACK_LEFT_BOTTOM_RIGHT),
	FLIPPED_OUTER_BACK_RIGHT_BOTTOM_LEFT("flipped_outer_back_right_bottom_left", StairFacingType.FLIPPED, StairShape.OUTER_BACK_RIGHT_BOTTOM_LEFT),
	

	VERTICAL_STRAIGHT("vertical_straight", StairFacingType.VERTICAL, StairShape.STRAIGHT),

	VERTICAL_INNER_FRONT_LEFT("vertical_inner_front_left", StairFacingType.VERTICAL, StairShape.INNER_FRONT_LEFT),
	VERTICAL_INNER_FRONT_RIGHT("vertical_inner_front_right", StairFacingType.VERTICAL, StairShape.INNER_FRONT_RIGHT),
	VERTICAL_INNER_TOP_LEFT("vertical_inner_top_left", StairFacingType.VERTICAL, StairShape.INNER_TOP_LEFT),
	VERTICAL_INNER_TOP_RIGHT("vertical_inner_top_right", StairFacingType.VERTICAL, StairShape.INNER_TOP_RIGHT),
	VERTICAL_INNER_BOTH_LEFT("vertical_inner_both_left", StairFacingType.VERTICAL, StairShape.INNER_BOTH_LEFT),
	VERTICAL_INNER_BOTH_RIGHT("vertical_inner_both_right", StairFacingType.VERTICAL, StairShape.INNER_BOTH_RIGHT),
	
	VERTICAL_OUTER_BACK_LEFT("vertical_outer_back_left", StairFacingType.VERTICAL, StairShape.OUTER_BACK_LEFT),
	VERTICAL_OUTER_BACK_RIGHT("vertical_outer_back_right", StairFacingType.VERTICAL, StairShape.OUTER_BACK_RIGHT),
	VERTICAL_OUTER_BOTTOM_LEFT("vertical_outer_bottom_left", StairFacingType.VERTICAL, StairShape.OUTER_BOTTOM_LEFT),
	VERTICAL_OUTER_BOTTOM_RIGHT("vertical_outer_bottom_right", StairFacingType.VERTICAL, StairShape.OUTER_BOTTOM_RIGHT),
	VERTICAL_OUTER_BOTH_LEFT("vertical_outer_both_left", StairFacingType.VERTICAL, StairShape.OUTER_BOTH_LEFT),
	VERTICAL_OUTER_BOTH_RIGHT("vertical_outer_both_right", StairFacingType.VERTICAL, StairShape.OUTER_BOTH_RIGHT),

	VERTICAL_OUTER_BACK_LEFT_BOTTOM_RIGHT("vertical_outer_back_left_bottom_right", StairFacingType.VERTICAL, StairShape.OUTER_BACK_LEFT_BOTTOM_RIGHT),
	VERTICAL_OUTER_BACK_RIGHT_BOTTOM_LEFT("vertical_outer_back_right_bottom_left", StairFacingType.VERTICAL, StairShape.OUTER_BACK_RIGHT_BOTTOM_LEFT);
	
	public static final CompressedStairShape[] ALL_SHAPES = values();
	public static final CompressedStairShape[] NO_VERTICAL_CONNECTIONS;
	public static final CompressedStairShape[] NO_MIXED_CONNECTIONS;
	
	private static final CompressedStairShape[][] SHAPE_MAP = new CompressedStairShape[3][15];
	
	static {
		List<CompressedStairShape> noVerticalConnections = new ArrayList<>();
		List<CompressedStairShape> noMixedConnections = new ArrayList<>();
		for (CompressedStairShape shape : ALL_SHAPES) {
			if (!shape.shape.isVerticalConnection) noVerticalConnections.add(shape);
			if (!shape.shape.isMixedConnection) noMixedConnections.add(shape);
			SHAPE_MAP[shape.facingType.ordinal()][shape.shape.ordinal()] = shape;
		}
		NO_VERTICAL_CONNECTIONS = noVerticalConnections.toArray(CompressedStairShape[]::new);
		NO_MIXED_CONNECTIONS = noMixedConnections.toArray(CompressedStairShape[]::new);
	}
	
	public static CompressedStairShape getCompressedShape(StairFacingType facingType, StairShape shape) {
		return SHAPE_MAP[facingType.ordinal()][shape.ordinal()];
	}
	
	public final String name;
	public final StairFacingType facingType;
	public final StairShape shape;
	
	CompressedStairShape(String name, StairFacingType facingType, StairShape shape) {
		this.name = name;
		this.facingType = facingType;
		this.shape = shape;
	}

	@Override
	public String getSerializedName() {
		return name;
	}
	
	public VoxelShape getVoxelShape(CompressedStairFacing facing) {
		return shape.getVoxelShape(facingType.fromCompressedFacing(facing));
	}
}
