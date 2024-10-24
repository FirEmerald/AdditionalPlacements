package com.firemerald.additionalplacements.datagen;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;

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
		/*
		// Build stairs blockstate template (must be manually copied)
		new SlabAndStairModelsBuilder(this)
		.setStairs((StairBlock) Blocks.BRICK_STAIRS, SlabAndStairModelsBuilder.STAIRS_DYNAMIC)
		.setUVLock(true)
		.buildStairs();
		*/
		
		new SlabAndStairModelsBuilder(this)
		.setSlab(SlabAndStairModelsBuilder.SLAB_SIDE_ALL, AdditionalPlacementsMod.MOD_ID, SlabAndStairModelsBuilder.SLAB_BASE).setStairs(SlabAndStairModelsBuilder.STAIRS_SIDE_ALL, AdditionalPlacementsMod.MOD_ID, SlabAndStairModelsBuilder.STAIRS_BASE).addAction((builder, model) -> builder.texture("side", "#all").texture("top", "#all").texture("bottom", "#all")).compile() //build side_all
		.setSlab(SlabAndStairModelsBuilder.SLAB_COLUMN, AdditionalPlacementsMod.MOD_ID, SlabAndStairModelsBuilder.SLAB_BASE).setStairs(SlabAndStairModelsBuilder.STAIRS_COLUMN, AdditionalPlacementsMod.MOD_ID, SlabAndStairModelsBuilder.STAIRS_BASE).addAction((builder, model) -> builder.texture("top", "#end").texture("bottom", "#end")).compile() //build pillar
		.setSlab(SlabAndStairModelsBuilder.SLAB_DYNAMIC).addAction((builder, model) -> builder.customLoader(DynamicModelLoaderBuilder::new).setModel(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, SlabAndStairModelsBuilder.SLAB_BASE + model))).compile() //build dynamic slab
		.setStairs(SlabAndStairModelsBuilder.STAIRS_DYNAMIC).addAction((builder, model) -> builder.customLoader(DynamicModelLoaderBuilder::new).setModel(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, SlabAndStairModelsBuilder.STAIRS_BASE + model))).compile(); //build dynamic stairs
		new CarpetModelsBuilder(this)
		.setCarpet(CarpetModelsBuilder.CARPET_SIDE_ALL, AdditionalPlacementsMod.MOD_ID, CarpetModelsBuilder.CARPET_BASE).addAction((builder, model) -> builder.texture("side", "#all").texture("top", "#all").texture("bottom", "#all")).compile() //build side_all
		.setCarpet(CarpetModelsBuilder.CARPET_COLUMN, AdditionalPlacementsMod.MOD_ID, CarpetModelsBuilder.CARPET_BASE).addAction((builder, model) -> builder.texture("top", "#end").texture("bottom", "#end")).compile() //build pillar
		.setCarpet(CarpetModelsBuilder.CARPET_DYNAMIC).addAction((builder, model) -> builder.customLoader(DynamicModelLoaderBuilder::new).setModel(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, CarpetModelsBuilder.CARPET_BASE + model))).compile(); //build dynamic carpet
		new PressurePlateModelsBuilder(this)
		.setPressurePlate(PressurePlateModelsBuilder.PRESSURE_PLATE_SIDE_ALL, AdditionalPlacementsMod.MOD_ID, PressurePlateModelsBuilder.PRESSURE_PLATE_BASE).addAction((builder, model) -> builder.texture("side", "#all").texture("top", "#all").texture("bottom", "#all")).compile() //build side_all
		.setPressurePlate(PressurePlateModelsBuilder.PRESSURE_PLATE_COLUMN, AdditionalPlacementsMod.MOD_ID, PressurePlateModelsBuilder.PRESSURE_PLATE_BASE).addAction((builder, model) -> builder.texture("top", "#end").texture("bottom", "#end")).compile() //build pillar
		.setPressurePlate(PressurePlateModelsBuilder.PRESSURE_PLATE_DYNAMIC).addAction((builder, model) -> builder.customLoader(DynamicModelLoaderBuilder::new).setModel(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, PressurePlateModelsBuilder.PRESSURE_PLATE_BASE + model))).compile(); //build dynamic pressure plate
	}
}