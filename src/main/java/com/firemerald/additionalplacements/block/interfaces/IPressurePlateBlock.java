package com.firemerald.additionalplacements.block.interfaces;

import com.firemerald.additionalplacements.block.AdditionalPressurePlateBlock;
import com.firemerald.additionalplacements.generation.APGenerationTypes;
import com.firemerald.additionalplacements.generation.GenerationType;

import net.minecraft.block.Block;

public interface IPressurePlateBlock<T extends Block> extends IBasePressurePlateBlock<T>
{
	interface IVanillaPressurePlateBlock extends IVanillaBasePressurePlateBlock<AdditionalPressurePlateBlock>, IPressurePlateBlock<AdditionalPressurePlateBlock> {}

	@Override
    default GenerationType<?, ?> getGenerationType() {
		return APGenerationTypes.pressurePlate();
	}
}