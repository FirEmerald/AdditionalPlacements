package com.firemerald.additionalplacements.block.interfaces;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public interface IPaneConnectable {
    boolean paneConnectOverride(BlockState ourState, Direction.Axis paneAxis, Direction connectDir);
}
