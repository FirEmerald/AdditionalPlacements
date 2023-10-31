package com.firemerald.additionalplacements.block.interfaces;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.AdditionalWeightedPressurePlateBlock;

import net.minecraft.world.level.block.Block;

public interface IWeightedPressurePlateBlock<T extends Block> extends IBasePressurePlateBlock<T>
{
	public static interface IVanillaWeightedPressurePlateBlock extends IVanillaBasePressurePlateBlock<AdditionalWeightedPressurePlateBlock>, IWeightedPressurePlateBlock<AdditionalWeightedPressurePlateBlock> {}

    @Override
	public default boolean disablePlacement()
	{
		return AdditionalPlacementsMod.COMMON_CONFIG.disableAutomaticWeightedPressurePlatePlacement.get();
	}
}