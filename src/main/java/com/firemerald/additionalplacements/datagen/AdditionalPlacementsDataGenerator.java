package com.firemerald.additionalplacements.datagen;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class AdditionalPlacementsDataGenerator implements DataGeneratorEntrypoint
{
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator)
	{
		fabricDataGenerator.addProvider(gen -> new ModelGenerator(gen, AdditionalPlacementsMod.MOD_ID, ExistingFileHelper.withResourcesFromArg()));
	}
}