package com.firemerald.additionalplacements.block.stairs.common;

import java.util.function.Function;

import com.firemerald.additionalplacements.util.ComplexFacing;
import com.firemerald.additionalplacements.util.VoxelShapes;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.shapes.VoxelShape;

public enum CommonStairShape implements IStringSerializable {
	STRAIGHT("straight", VoxelShapes::getStraightStairs, false, false, PaneConnection.HORIZONTAL, PaneConnection.HORIZONTAL, PaneConnection.BOTH, PaneConnection.BOTH),

	INNER_FRONT_LEFT("inner_front_left", VoxelShapes::getLeftInnerStairs, false, false, PaneConnection.BOTH, PaneConnection.BOTH, PaneConnection.BOTH, PaneConnection.BOTH),
	INNER_FRONT_RIGHT("inner_front_right", VoxelShapes::getRightInnerStairs, false, false, PaneConnection.BOTH, PaneConnection.BOTH, PaneConnection.BOTH, PaneConnection.BOTH),
	INNER_TOP_LEFT("inner_top_left", VoxelShapes::getLeftInnerStairs, true, false, PaneConnection.BOTH, PaneConnection.BOTH, PaneConnection.BOTH, PaneConnection.BOTH, INNER_FRONT_LEFT),
	INNER_TOP_RIGHT("inner_top_right", VoxelShapes::getRightInnerStairs, true, false, PaneConnection.BOTH, PaneConnection.BOTH, PaneConnection.BOTH, PaneConnection.BOTH, INNER_FRONT_RIGHT),
	INNER_BOTH_LEFT("inner_both_left", VoxelShapes::getLeftInnerStairs, true, true, PaneConnection.BOTH, PaneConnection.BOTH, PaneConnection.BOTH, PaneConnection.BOTH, INNER_FRONT_LEFT),
	INNER_BOTH_RIGHT("inner_both_right", VoxelShapes::getRightInnerStairs, true, true, PaneConnection.BOTH, PaneConnection.BOTH, PaneConnection.BOTH, PaneConnection.BOTH, INNER_FRONT_RIGHT),
	
	OUTER_BACK_LEFT("outer_back_left", VoxelShapes::getLeftOuterBackFlatStairs, false, false, PaneConnection.HORIZONTAL, PaneConnection.NONE, PaneConnection.HORIZONTAL, PaneConnection.BOTH),
	OUTER_BACK_RIGHT("outer_back_right", VoxelShapes::getRightOuterBackFlatStairs, false, false, PaneConnection.HORIZONTAL, PaneConnection.NONE, PaneConnection.BOTH, PaneConnection.HORIZONTAL),
	OUTER_BOTTOM_LEFT("outer_bottom_left", VoxelShapes::getLeftOuterBottomFlatStairs, true, false, PaneConnection.NONE, PaneConnection.HORIZONTAL, PaneConnection.VERTICAL, PaneConnection.BOTH, OUTER_BACK_LEFT),
	OUTER_BOTTOM_RIGHT("outer_bottom_right", VoxelShapes::getRightOuterBottomFlatStairs, true, false, PaneConnection.NONE, PaneConnection.HORIZONTAL, PaneConnection.BOTH, PaneConnection.VERTICAL, OUTER_BACK_RIGHT),
	OUTER_BOTH_LEFT("outer_both_left", VoxelShapes::getLeftOuterStairs, true, true, PaneConnection.NONE, PaneConnection.NONE, PaneConnection.NONE, PaneConnection.BOTH, OUTER_BACK_LEFT),
	OUTER_BOTH_RIGHT("outer_both_right", VoxelShapes::getRightOuterStairs, true, true, PaneConnection.NONE, PaneConnection.NONE, PaneConnection.BOTH, PaneConnection.NONE, OUTER_BACK_RIGHT),

	OUTER_BACK_LEFT_BOTTOM_RIGHT("outer_back_left_bottom_right", VoxelShapes::getCounterClockwiseTwistStairs, true, true, PaneConnection.NONE, PaneConnection.NONE, PaneConnection.VERTICAL, PaneConnection.HORIZONTAL, STRAIGHT),
	OUTER_BACK_RIGHT_BOTTOM_LEFT("outer_back_right_bottom_left", VoxelShapes::getClockwiseTwistStairs, true, true, PaneConnection.NONE, PaneConnection.NONE, PaneConnection.HORIZONTAL, PaneConnection.VERTICAL, STRAIGHT);
	
	static {
		STRAIGHT.flipped = STRAIGHT;
		
		setFlipped(INNER_FRONT_LEFT, INNER_TOP_RIGHT);
		setFlipped(INNER_TOP_LEFT, INNER_FRONT_RIGHT);
		setFlipped(INNER_BOTH_LEFT, INNER_BOTH_RIGHT);
		
		setFlipped(OUTER_BACK_LEFT, OUTER_BOTTOM_RIGHT);
		setFlipped(OUTER_BOTTOM_LEFT, OUTER_BACK_RIGHT);
		setFlipped(OUTER_BOTH_LEFT, OUTER_BOTH_RIGHT);

		OUTER_BACK_LEFT_BOTTOM_RIGHT.flipped = OUTER_BACK_LEFT_BOTTOM_RIGHT;
		OUTER_BACK_RIGHT_BOTTOM_LEFT.flipped = OUTER_BACK_RIGHT_BOTTOM_LEFT;
	}
	
	private static void setFlipped(CommonStairShape a, CommonStairShape b) {
		a.flipped = b;
		b.flipped = a;
	}
	
	public static CommonStairShape get(String name) {
		for (CommonStairShape shape : values()) if (shape.name.equals(name)) return shape;
		return null;
	}
	
	public final String name;
	public final Function<ComplexFacing, VoxelShape> getShape;
	public final CommonStairShape closestVanillaMatch;
	public final boolean isVertical, isMixed;
	private CommonStairShape flipped;
	public final PaneConnection paneFront, paneTop, paneLeft, paneRight;
	
	CommonStairShape(String name, Function<ComplexFacing, VoxelShape> getShape, boolean isVertical, boolean isMixed, PaneConnection paneFront, PaneConnection paneTop, PaneConnection paneLeft, PaneConnection paneRight) {
		this.name = name;
		this.getShape = getShape;
		this.isVertical = isVertical;
		this.isMixed = isMixed;
		this.paneFront = paneFront;
		this.paneTop = paneTop;
		this.paneLeft = paneLeft;
		this.paneRight = paneRight;
		this.closestVanillaMatch = this;
	}

	CommonStairShape(String name, Function<ComplexFacing, VoxelShape> getShape, boolean isVertical, boolean isMixed, PaneConnection paneFront, PaneConnection paneTop, PaneConnection paneLeft, PaneConnection paneRight, CommonStairShape closestVanillaMatch) {
		this.name = name;
		this.getShape = getShape;
		this.isVertical = isVertical;
		this.isMixed = isMixed;
		this.paneFront = paneFront;
		this.paneTop = paneTop;
		this.paneLeft = paneLeft;
		this.paneRight = paneRight;
		this.closestVanillaMatch = closestVanillaMatch;
	}
	
	public VoxelShape getVoxelShape(ComplexFacing facing) {
		return getShape.apply(facing);
	}
	
	public CommonStairShape flipped() { 
		return flipped;
	}

	@Override
	public String getSerializedName() {
		return name;
	}
}