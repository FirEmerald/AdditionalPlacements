package com.firemerald.additionalplacements.block.interfaces;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.AdditionalBasePressurePlateBlock;

import net.minecraft.world.level.block.Block;

public interface IWeightedPressurePlateBlock<T extends Block> extends IPressurePlateBlock<T>
{
	public static interface IVanillaWeightedPressurePlateBlock extends IWeightedPressurePlateBlock<AdditionalBasePressurePlateBlock<?>>, IVanillaBlock<AdditionalBasePressurePlateBlock<?>> {}
	
    @Override
	public default boolean disablePlacement()
	{
		return AdditionalPlacementsMod.COMMON_CONFIG.disableAutomaticWeightedPressurePlatePlacement.get();
	}
}