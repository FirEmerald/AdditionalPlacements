package com.firemerald.additionalplacements.datagen;

import com.firemerald.additionalplacements.block.AdditionalCarpetBlock;
import com.firemerald.additionalplacements.block.AdditionalFloorBlock;
import com.firemerald.additionalplacements.client.models.definitions.*;

import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.CarpetBlock;

public class CarpetModelsGenerator extends SimpleModelsGenerator<CarpetBlock, AdditionalCarpetBlock, CarpetModelsGenerator> {
	@Override
	public String[] modelPaths() {
		return CarpetModels.MODELS;
	}
	
	@Override
	public PropertyDispatch<MultiVariant> dispatch(ResourceLocation modelPrefix) {
		return PropertyDispatch.initial(AdditionalFloorBlock.PLACING).generate(placing -> variantOf(CarpetModels.getModel(placing), modelPrefix));
	}
}
