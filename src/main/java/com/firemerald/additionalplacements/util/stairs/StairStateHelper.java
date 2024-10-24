package com.firemerald.additionalplacements.util.stairs;

import org.apache.commons.lang3.tuple.Pair;

import com.firemerald.additionalplacements.block.VerticalStairBlock;
import com.firemerald.additionalplacements.util.ComplexFacing;

import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

public class StairStateHelper
{
	@SuppressWarnings("incomplete-switch")
	public static ComplexFacing getFacingFromVanilla(BlockState blockState) {
		if (blockState.getValue(StairBlock.HALF) == Half.BOTTOM) switch (blockState.getValue(StairBlock.FACING))
		{
		case SOUTH: return ComplexFacing.NORTH_UP;
		case WEST: return ComplexFacing.EAST_UP;
		case NORTH: return ComplexFacing.SOUTH_UP;
		case EAST: return ComplexFacing.WEST_UP;
		}
		else switch (blockState.getValue(StairBlock.FACING))
		{
		case SOUTH: return ComplexFacing.NORTH_DOWN;
		case WEST: return ComplexFacing.EAST_DOWN;
		case NORTH: return ComplexFacing.SOUTH_DOWN;
		case EAST: return ComplexFacing.WEST_DOWN;
		}
		return null;
	}
	
	public static ComplexFacing getFacingFromAdditional(BlockState blockState) {
		return blockState.getValue(((VerticalStairBlock) blockState.getBlock()).shapeProperty()).facingType.fromCompressedFacing(blockState.getValue(VerticalStairBlock.FACING));
	}
	
	public static ComplexFacing getFacing(BlockState blockState) {
		if (blockState.getBlock() instanceof StairBlock) return getFacingFromVanilla(blockState);
		else if (blockState.getBlock() instanceof VerticalStairBlock) return getFacingFromAdditional(blockState);
		else return null;
	}
	
	public static StairShape getShapeFromVanilla(BlockState blockState) {
		return StairShape.getShape(blockState.getValue(StairBlock.SHAPE), blockState.getValue(StairBlock.HALF));
	}
	
	public static StairShape getShapeFromAdditional(BlockState blockState) {
		return blockState.getValue(((VerticalStairBlock) blockState.getBlock()).shapeProperty()).shape;
	}
	
	public static StairShape getShape(BlockState blockState) {
		if (blockState.getBlock() instanceof StairBlock) return getShapeFromVanilla(blockState);
		else if (blockState.getBlock() instanceof VerticalStairBlock) return getShapeFromAdditional(blockState);
		else return null;
	}

	public static Pair<ComplexFacing, StairShape> getFullState(BlockState blockState)
	{
		if (blockState.getBlock() instanceof StairBlock) return Pair.of(getFacingFromVanilla(blockState), getShapeFromVanilla(blockState));
		else return Pair.of(getFacingFromAdditional(blockState), getShapeFromAdditional(blockState));
	}
}
