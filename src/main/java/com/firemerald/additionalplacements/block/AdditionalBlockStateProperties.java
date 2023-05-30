package com.firemerald.additionalplacements.block;

import net.minecraft.state.DirectionProperty;
import net.minecraft.util.Direction;

public class AdditionalBlockStateProperties
{
	   public static final DirectionProperty HORIZONTAL_OR_UP_FACING = DirectionProperty.create("facing", Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);
}