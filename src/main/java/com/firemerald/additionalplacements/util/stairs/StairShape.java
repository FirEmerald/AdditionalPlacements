package com.firemerald.additionalplacements.util.stairs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.firemerald.additionalplacements.util.ComplexFacing;
import com.firemerald.additionalplacements.util.VoxelShapes;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.phys.shapes.VoxelShape;

public enum StairShape implements StringRepresentable {
	STRAIGHT("straight", VoxelShapes::getStraightStairs, StairsShape.STRAIGHT, StairsShape.STRAIGHT, false, false),

	INNER_FRONT_LEFT("inner_front_left", VoxelShapes::getLeftInnerStairs, StairsShape.INNER_RIGHT, StairsShape.INNER_LEFT, false, false),
	INNER_FRONT_RIGHT("inner_front_right", VoxelShapes::getRightInnerStairs, StairsShape.INNER_LEFT, StairsShape.INNER_RIGHT, false, false),
	INNER_TOP_LEFT("inner_top_left", VoxelShapes::getLeftInnerStairs, true, false),
	INNER_TOP_RIGHT("inner_top_right", VoxelShapes::getRightInnerStairs, true, false),
	INNER_BOTH_LEFT("inner_both_left", VoxelShapes::getLeftInnerStairs, true, true),
	INNER_BOTH_RIGHT("inner_both_right", VoxelShapes::getRightInnerStairs, true, true),
	
	OUTER_BACK_LEFT("outer_back_left", VoxelShapes::getLeftOuterBackFlatStairs, StairsShape.OUTER_RIGHT, StairsShape.OUTER_LEFT, false, false),
	OUTER_BACK_RIGHT("outer_back_right", VoxelShapes::getRightOuterBackFlatStairs, StairsShape.OUTER_LEFT, StairsShape.OUTER_RIGHT, false, false),
	OUTER_BOTTOM_LEFT("outer_bottom_left", VoxelShapes::getLeftOuterBottomFlatStairs, true, false),
	OUTER_BOTTOM_RIGHT("outer_bottom_right", VoxelShapes::getRightOuterBottomFlatStairs, true, false),
	OUTER_BOTH_LEFT("outer_both_left", VoxelShapes::getLeftOuterStairs, true, true),
	OUTER_BOTH_RIGHT("outer_both_right", VoxelShapes::getRightOuterStairs, true, true),

	OUTER_BACK_LEFT_BOTTOM_RIGHT("outer_back_left_bottom_right", VoxelShapes::getCounterClockwiseTwistStairs, true, true),
	OUTER_BACK_RIGHT_BOTTOM_LEFT("outer_back_right_bottom_left", VoxelShapes::getClockwiseTwistStairs, true, true);
	
	public static final StairShape[] ALL_SHAPES = values();
	public static final StairShape[] NO_VERTICAL_CONNECTIONS;
	public static final StairShape[] NO_MIXED_CONNECTIONS;

	private static final StairShape[] FROM_BASIC_TOP = new StairShape[5];
	private static final StairShape[] FROM_BASIC_BOTTOM = new StairShape[5];
	
	static {
		List<StairShape> noVerticalConnections = new ArrayList<>();
		List<StairShape> noMixedConnections = new ArrayList<>();
		for (StairShape shape : ALL_SHAPES) {
			if (!shape.isVerticalConnection) noVerticalConnections.add(shape);
			if (!shape.isMixedConnection) noMixedConnections.add(shape);
			if (shape.vanillaTopShape != null) FROM_BASIC_TOP[shape.vanillaTopShape.ordinal()] = shape;
			if (shape.vanillaBottomShape != null) FROM_BASIC_BOTTOM[shape.vanillaBottomShape.ordinal()] = shape;
		}
		NO_VERTICAL_CONNECTIONS = noVerticalConnections.toArray(StairShape[]::new);
		NO_MIXED_CONNECTIONS = noMixedConnections.toArray(StairShape[]::new);
	}
	
	public static StairShape getShape(StairsShape basicShape, Half half) {
		return (half == Half.TOP ? FROM_BASIC_TOP : FROM_BASIC_BOTTOM)[basicShape.ordinal()];
	}
	
	public static StairShape get(String name) {
		for (StairShape shape : ALL_SHAPES) if (shape.name.equals(name)) return shape;
		return null;
	}
	
	public final String name;
	public final Function<ComplexFacing, VoxelShape> getShape;
	public final StairsShape vanillaTopShape, vanillaBottomShape;
	public final boolean isVerticalConnection, isMixedConnection;
	
	StairShape(String name, Function<ComplexFacing, VoxelShape> getShape, boolean isVerticalConnection, boolean isMixedConnection) {
		this(name, getShape, null, null, isVerticalConnection, isMixedConnection);
	}
	
	StairShape(String name, Function<ComplexFacing, VoxelShape> getShape, StairsShape vanillaTopShape, StairsShape vanillaBottomShape, boolean isVerticalConnection, boolean isMixedConnection) {
		this.name = name;
		this.getShape = getShape;
		this.vanillaTopShape = vanillaTopShape;
		this.vanillaBottomShape = vanillaBottomShape;
		this.isVerticalConnection = isVerticalConnection;
		this.isMixedConnection = isMixedConnection;
	}
	
	public VoxelShape getVoxelShape(ComplexFacing facing) {
		return getShape.apply(facing);
	}
	
	public StairsShape getVanillaShape(Half half) {
		return half == Half.TOP ? vanillaTopShape : vanillaBottomShape;
	}
	
	public StairsShape getVanillaShape(ComplexFacing facing) {
		return facing.vanillaStairsHalf == null ? null : getVanillaShape(facing.vanillaStairsHalf);
	}

	@Override
	public String getSerializedName() {
		return name;
	}
}