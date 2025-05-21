package com.firemerald.additionalplacements.block.interfaces;

import com.firemerald.additionalplacements.block.AdditionalWeightedPressurePlateBlock;
import com.firemerald.additionalplacements.generation.APGenerationTypes;
import com.firemerald.additionalplacements.generation.GenerationType;

import net.minecraft.block.Block;

public interface IWeightedPressurePlateBlock<T extends Block> extends IBasePressurePlateBlock<T>
{
	interface IVanillaWeightedPressurePlateBlock extends IVanillaBasePressurePlateBlock<AdditionalWeightedPressurePlateBlock>, IWeightedPressurePlateBlock<AdditionalWeightedPressurePlateBlock> {}

	@Override
    default GenerationType<?, ?> getGenerationType() {
		return APGenerationTypes.weightedPressurePlate();
	}
}