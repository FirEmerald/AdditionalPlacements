package com.firemerald.additionalplacements.util;

import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.Half;

public enum ComplexFacing implements StringRepresentable {
	//vanilla facing
	SOUTH_UP("south_up", Direction.SOUTH, Direction.UP, Direction.EAST, BlockRotation.IDENTITY, Direction.NORTH, Half.BOTTOM),
	WEST_UP("west_up", Direction.WEST, Direction.UP, Direction.SOUTH, BlockRotation.IDENTITY, Direction.EAST, Half.BOTTOM),
	NORTH_UP("north_up", Direction.NORTH, Direction.UP, Direction.WEST, BlockRotation.IDENTITY, Direction.SOUTH, Half.BOTTOM),
	EAST_UP("east_up", Direction.EAST, Direction.UP, Direction.NORTH, BlockRotation.IDENTITY, Direction.WEST, Half.BOTTOM),

	SOUTH_DOWN("south_down", Direction.SOUTH, Direction.DOWN, Direction.WEST, BlockRotation.IDENTITY, Direction.NORTH, Half.TOP),
	WEST_DOWN("west_down", Direction.WEST, Direction.DOWN, Direction.NORTH, BlockRotation.IDENTITY, Direction.EAST, Half.TOP),
	NORTH_DOWN("north_down", Direction.NORTH, Direction.DOWN, Direction.EAST, BlockRotation.IDENTITY, Direction.SOUTH, Half.TOP),
	EAST_DOWN("east_down", Direction.EAST, Direction.DOWN, Direction.SOUTH, BlockRotation.IDENTITY, Direction.WEST, Half.TOP),

	//rotate X -90 (270)
	DOWN_SOUTH("down_south", Direction.DOWN, Direction.SOUTH, Direction.EAST, BlockRotation.X_270, Direction.NORTH, Half.BOTTOM),
	WEST_SOUTH("west_south", Direction.WEST, Direction.SOUTH, Direction.DOWN, BlockRotation.X_270, Direction.EAST, Half.BOTTOM),
	UP_SOUTH("up_south", Direction.UP, Direction.SOUTH, Direction.WEST, BlockRotation.X_270, Direction.SOUTH, Half.BOTTOM),
	EAST_SOUTH("east_south", Direction.EAST, Direction.SOUTH, Direction.UP, BlockRotation.X_270, Direction.WEST, Half.BOTTOM),

	DOWN_NORTH("down_north", Direction.DOWN, Direction.NORTH, Direction.WEST, BlockRotation.X_270, Direction.NORTH, Half.TOP),
	WEST_NORTH("west_north", Direction.WEST, Direction.NORTH, Direction.UP, BlockRotation.X_270, Direction.EAST, Half.TOP),
	UP_NORTH("up_north", Direction.UP, Direction.NORTH, Direction.EAST, BlockRotation.X_270, Direction.SOUTH, Half.TOP),
	EAST_NORTH("east_north", Direction.EAST, Direction.NORTH, Direction.DOWN, BlockRotation.X_270, Direction.WEST, Half.TOP),
	
	//rotate X-90 (270) then Y-90 (270)
	DOWN_EAST("down_east", Direction.DOWN, Direction.EAST, Direction.NORTH, BlockRotation.X_270_Y_270, Direction.NORTH, Half.BOTTOM),
	SOUTH_EAST("south_east", Direction.SOUTH, Direction.EAST, Direction.DOWN, BlockRotation.X_270_Y_270, Direction.EAST, Half.BOTTOM),
	UP_EAST("up_east", Direction.UP, Direction.EAST, Direction.SOUTH, BlockRotation.X_270_Y_270, Direction.SOUTH, Half.BOTTOM),
	NORTH_EAST("north_east", Direction.NORTH, Direction.EAST, Direction.UP, BlockRotation.X_270_Y_270, Direction.WEST, Half.BOTTOM),
	
	DOWN_WEST("down_west", Direction.DOWN, Direction.WEST, Direction.SOUTH, BlockRotation.X_270_Y_270, Direction.NORTH, Half.TOP),
	SOUTH_WEST("south_west", Direction.SOUTH, Direction.WEST, Direction.UP, BlockRotation.X_270_Y_270, Direction.EAST, Half.TOP),
	UP_WEST("up_west", Direction.UP, Direction.WEST, Direction.NORTH, BlockRotation.X_270_Y_270, Direction.SOUTH, Half.TOP),
	NORTH_WEST("north_west", Direction.NORTH, Direction.WEST, Direction.DOWN, BlockRotation.X_270_Y_270, Direction.WEST, Half.TOP);
	
	public static final ComplexFacing[] ALL_FACING = values();
	
	private static final ComplexFacing[][] FACING = new ComplexFacing[6][6];
	
	static {
		for (ComplexFacing facing : values()) FACING[facing.forward.get3DDataValue()][facing.up.get3DDataValue()] = facing;
		for (ComplexFacing facing : values()) facing.flipped = forFacing(facing.up, facing.forward);
	}
	
	public static ComplexFacing forFacing(Direction forward, Direction up) {
		return FACING[forward.get3DDataValue()][up.get3DDataValue()];
	}
	
	public static ComplexFacing get(String name) {
		for (ComplexFacing facing : values()) if (facing.name.equals(name)) return facing;
		return null;
	}
	
	public final String name;
	public final Direction forward, up, left, backward, down, right;
	public final BlockRotation stairsModelRotation;
	public final Direction vanillaStairsFacing;
	public final Half vanillaStairsHalf;
	private ComplexFacing flipped;
	
	ComplexFacing(String name, Direction forward, Direction up, Direction left, BlockRotation stairsModelRotation, Direction vanillaStairsFacing, Half vanillaStairsHalf) {
		this.name = name;
		this.backward = (this.forward = forward).getOpposite();
		this.down = (this.up = up).getOpposite();
		this.right = (this.left = left).getOpposite();
		this.stairsModelRotation = stairsModelRotation;
		this.vanillaStairsFacing = vanillaStairsFacing;
		this.vanillaStairsHalf = vanillaStairsHalf;
	}
	
	public ComplexFacing flipped() {
		return flipped;
	}

	@Override
	public String getSerializedName() {
		return name;
	}
}
