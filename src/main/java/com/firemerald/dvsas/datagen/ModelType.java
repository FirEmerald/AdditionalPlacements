package com.firemerald.dvsas.datagen;

import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.http.util.Asserts;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;

public class ModelType
{
	final boolean hasItem;
	final String[] models;
	final Function<BlockState, StateModelDefinition> getModelDefinition;
	final Property<?>[] ignoredStateProperties;
	boolean has = false;
	Block block = null;
	String folder = null;
	String parentMod = null;
	String parentFolder = null;
	
	public ModelType(boolean hasItem, String[] models, Function<BlockState, StateModelDefinition> getModelDefinition, Property<?>... ignoredStateProperties)
	{
		this.hasItem = hasItem;
		this.models = models;
		this.getModelDefinition = getModelDefinition;
		this.ignoredStateProperties = ignoredStateProperties;
	}
	
	public void set(Block block, String folder, String parentMod, String parentFolder)
	{
		has = true;
		this.block = block;
		this.folder = folder;
		this.parentMod = parentMod;
		this.parentFolder = parentFolder;
	}
	
	public void set(Block block, String folder)
	{
		set(block, folder, null, null);
	}
	
	public void set(String folder)
	{
		set(null, folder, null, null);
	}
	
	public void setParent(String parentMod, String parentFolder)
	{
		this.parentMod = parentMod;
		this.parentFolder = parentFolder;
	}
	
	public void build(BlockStateProvider stateProvider, Consumer<BlockModelBuilder> actions, boolean uvLock)
	{
		if (has)
		{
			Asserts.notNull(folder, "folder");
			Asserts.notNull(parentMod, "parent model");
			Asserts.notNull(parentFolder, "parent model");
			if (actions == null) actions = model -> {};
			BlockModelProvider modelProvider = stateProvider.models();
			for (String model : models) actions.accept(modelProvider.withExistingParent(folder + model, new ResourceLocation(parentMod, parentFolder + model)));
			if (block != null)
			{
				stateProvider.getVariantBuilder(block).forAllStatesExcept(state -> {
					StateModelDefinition modelDef = getModelDefinition.apply(state);
					return ConfiguredModel.builder()
					.modelFile(modelProvider.getExistingFile(modelProvider.modLoc(folder + modelDef.model())))
					.rotationX(modelDef.xRotation())
					.rotationY(modelDef.yRotation())
					.uvLock(uvLock)
					.build();
				}, ignoredStateProperties);
				if (hasItem)
				{
					Item item = block.asItem();
					if (item != null) stateProvider.itemModels().withExistingParent(item.getRegistryName().getPath(), modelProvider.modLoc(folder + models[0]));
				}
			}
		}
	}
	
	public void clear()
	{
		has = false;
		this.block = null;
		this.folder = null;
		this.parentMod = null;
		this.parentFolder = null;
	}
}