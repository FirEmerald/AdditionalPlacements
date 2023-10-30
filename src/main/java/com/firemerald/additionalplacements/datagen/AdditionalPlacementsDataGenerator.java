package com.firemerald.additionalplacements.datagen;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack.Factory;

public class AdditionalPlacementsDataGenerator implements DataGeneratorEntrypoint
{
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator)
	{
		fabricDataGenerator.createPack().addProvider((Factory<?>) (gen -> new ModelGenerator(gen, AdditionalPlacementsMod.MOD_ID, ExistingFileHelper.withResourcesFromArg())));
	}
}