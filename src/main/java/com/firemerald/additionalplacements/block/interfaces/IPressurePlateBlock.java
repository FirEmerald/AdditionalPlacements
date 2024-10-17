package com.firemerald.additionalplacements.block.interfaces;

import com.firemerald.additionalplacements.block.AdditionalPressurePlateBlock;
import com.firemerald.additionalplacements.generation.APGenerationTypes;
import com.firemerald.additionalplacements.generation.GenerationType;

import net.minecraft.world.level.block.Block;

public interface IPressurePlateBlock<T extends Block> extends IBasePressurePlateBlock<T>
{
	public static interface IVanillaPressurePlateBlock extends IVanillaBasePressurePlateBlock<AdditionalPressurePlateBlock>, IPressurePlateBlock<AdditionalPressurePlateBlock> {}

	@Override
	public default GenerationType<?, ?> getGenerationType() {
		return APGenerationTypes.PRESSURE_PLATE;
	}
}