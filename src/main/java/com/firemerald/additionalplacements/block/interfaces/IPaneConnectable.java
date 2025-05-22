package com.firemerald.additionalplacements.block.interfaces;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;

public interface IPaneConnectable {
    boolean paneConnectOverride(BlockState ourState, Direction.Axis paneAxis, Direction connectDir);
}
