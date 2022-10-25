package com.firemerald.additionalplacements.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class AdditionalBlockStateProperties
{
	   public static final DirectionProperty HORIZONTAL_OR_UP_FACING = DirectionProperty.create("facing", Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);
}