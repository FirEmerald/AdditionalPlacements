package com.firemerald.additionalplacements.datagen;

import com.firemerald.additionalplacements.block.VerticalSlabBlock;
import com.firemerald.additionalplacements.client.models.definitions.*;

import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SlabBlock;

public class SlabModelsGenerator extends SimpleModelsGenerator<SlabBlock, VerticalSlabBlock, SlabModelsGenerator> {
	@Override
	public String[] modelPaths() {
		return SlabModels.MODELS;
	}

	@Override
	public PropertyDispatch<MultiVariant> dispatch(ResourceLocation modelPrefix) {
		return PropertyDispatch.initial(VerticalSlabBlock.AXIS, SlabBlock.TYPE).generate((axis, type) -> variantOf(SlabModels.getModel(axis, type), modelPrefix));
	}
}
