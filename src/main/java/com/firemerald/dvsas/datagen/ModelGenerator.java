package com.firemerald.dvsas.datagen;

import com.firemerald.dvsas.DVSaSMod;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModelGenerator extends BlockStateProvider
{
	public ModelGenerator(DataGenerator gen, String modid, ExistingFileHelper exFileHelper)
	{
		super(gen, modid, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels()
	{
		new SlabAndStairModelsBuilder(this)
		.setSlab(SlabAndStairModelsBuilder.SLAB_SIDE_ALL, DVSaSMod.MOD_ID, SlabAndStairModelsBuilder.SLAB_BASE).setStairs(SlabAndStairModelsBuilder.STAIRS_SIDE_ALL, DVSaSMod.MOD_ID, SlabAndStairModelsBuilder.STAIRS_BASE).addAction((builder, model) -> builder.texture("side", "#all").texture("top", "#all").texture("bottom", "#all")).compile() //build side_all
		.setSlab(SlabAndStairModelsBuilder.SLAB_COLUMN, DVSaSMod.MOD_ID, SlabAndStairModelsBuilder.SLAB_BASE).setStairs(SlabAndStairModelsBuilder.STAIRS_COLUMN, DVSaSMod.MOD_ID, SlabAndStairModelsBuilder.STAIRS_BASE).addAction((builder, model) -> builder.texture("top", "#end").texture("bottom", "#end")).compile() //build pillar
		.setSlab(SlabAndStairModelsBuilder.SLAB_DYNAMIC).addAction((builder, model) -> builder.customLoader(DynamicModelLoaderBuilder::new).setModel(new ResourceLocation(DVSaSMod.MOD_ID, SlabAndStairModelsBuilder.SLAB_BASE + model))).compile() //build dynamic slab
		.setStairs(SlabAndStairModelsBuilder.STAIRS_DYNAMIC).addAction((builder, model) -> builder.customLoader(DynamicModelLoaderBuilder::new).setModel(new ResourceLocation(DVSaSMod.MOD_ID, SlabAndStairModelsBuilder.STAIRS_BASE + model))).compile(); //build dynamic stairs
	}
}