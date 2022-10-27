package com.firemerald.additionalplacements.block.interfaces;

import net.minecraft.world.level.block.Block;

public interface IVanillaBlock<T extends Block & IPlacementBlock<?>> extends IPlacementBlock<T>
{
	public void setOtherBlock(T block);
}
