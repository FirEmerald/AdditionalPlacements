package com.firemerald.additionalplacements.block.interfaces;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.AdditionalPressurePlateBlock;

import net.minecraft.world.level.block.Block;

public interface IPressurePlateBlock<T extends Block> extends IBasePressurePlateBlock<T>
{
	public static interface IVanillaPressurePlateBlock extends IVanillaBasePressurePlateBlock<AdditionalPressurePlateBlock>, IPressurePlateBlock<AdditionalPressurePlateBlock> {}

    @Override
	public default boolean disablePlacement()
	{
		return AdditionalPlacementsMod.COMMON_CONFIG.disableAutomaticPressurePlatePlacement.get();
	}
}