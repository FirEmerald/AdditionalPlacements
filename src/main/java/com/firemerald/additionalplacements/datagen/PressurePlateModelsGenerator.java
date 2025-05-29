package com.firemerald.additionalplacements.datagen;

import com.firemerald.additionalplacements.block.AdditionalFloorBlock;
import com.firemerald.additionalplacements.block.AdditionalPressurePlateBlock;
import com.firemerald.additionalplacements.client.models.definitions.*;

import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.PressurePlateBlock;

public class PressurePlateModelsGenerator extends PressurePlateBaseModelsGenerator<PressurePlateBlock, AdditionalPressurePlateBlock, PressurePlateModelsGenerator> {
	@Override
	public PropertyDispatch<MultiVariant> dispatch(ResourceLocation modelPrefix) {
		return PropertyDispatch.initial(AdditionalFloorBlock.PLACING, PressurePlateBlock.POWERED).generate((placing, powered) -> variantOf(PressurePlateModels.getModel(placing, powered), modelPrefix));
	}
}
