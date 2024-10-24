package com.firemerald.additionalplacements.block.interfaces;

import com.firemerald.additionalplacements.block.AdditionalWeightedPressurePlateBlock;
import com.firemerald.additionalplacements.generation.APGenerationTypes;
import com.firemerald.additionalplacements.generation.GenerationType;

import net.minecraft.world.level.block.Block;

public interface IWeightedPressurePlateBlock<T extends Block> extends IBasePressurePlateBlock<T>
{
	public static interface IVanillaWeightedPressurePlateBlock extends IVanillaBasePressurePlateBlock<AdditionalWeightedPressurePlateBlock>, IWeightedPressurePlateBlock<AdditionalWeightedPressurePlateBlock> {}

	@Override
	public default GenerationType<?, ?> getGenerationType() {
		return APGenerationTypes.WEIGHTED_PRESSURE_PLATE;
	}
}