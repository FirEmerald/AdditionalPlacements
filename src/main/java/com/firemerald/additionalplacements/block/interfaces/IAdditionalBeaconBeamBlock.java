package com.firemerald.additionalplacements.block.interfaces;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.Block;

public interface IAdditionalBeaconBeamBlock<T extends Block> extends IPlacementBlock<T>, BeaconBeamBlock
{
	@Override
    public default DyeColor getColor()
    {
    	return ((BeaconBeamBlock) getOtherBlock()).getColor();
    }
}