package com.firemerald.additionalplacements.datagen;

import com.firemerald.additionalplacements.block.AdditionalFloorBlock;
import com.firemerald.additionalplacements.block.AdditionalWeightedPressurePlateBlock;
import com.firemerald.additionalplacements.client.models.definitions.*;

import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.WeightedPressurePlateBlock;

public class WeightedPressurePlateModelsGenerator extends PressurePlateBaseModelsGenerator<WeightedPressurePlateBlock, AdditionalWeightedPressurePlateBlock, WeightedPressurePlateModelsGenerator> {
	@Override
	public PropertyDispatch<MultiVariant> dispatch(ResourceLocation modelPrefix) {
		return PropertyDispatch.initial(AdditionalFloorBlock.PLACING, WeightedPressurePlateBlock.POWER).generate((placing, power) -> variantOf(PressurePlateModels.getModel(placing, power), modelPrefix));
	}
}
