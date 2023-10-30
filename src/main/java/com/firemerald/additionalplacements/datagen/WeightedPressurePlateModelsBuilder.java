package com.firemerald.additionalplacements.datagen;

import java.util.function.BiConsumer;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.AdditionalPressurePlateBlock;
import com.firemerald.additionalplacements.block.AdditionalWeightedPressurePlateBlock;

import io.github.fabricators_of_create.porting_lib.models.generators.block.BlockModelBuilder;
import io.github.fabricators_of_create.porting_lib.models.generators.block.BlockStateProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.WeightedPressurePlateBlock;

public class WeightedPressurePlateModelsBuilder
{
	private final BlockStateProvider stateProvider;
	private final ModelType<AdditionalWeightedPressurePlateBlock> pressurePlateDef = new ModelType<>(false, PressurePlateModelsBuilder.PRESSURE_PLATE_MODELS, state -> PressurePlateModelsBuilder.PRESSURE_PLATE_DEFINITIONS[state.getValue(AdditionalPressurePlateBlock.PLACING).ordinal() - 1][state.getValue(WeightedPressurePlateBlock.POWER) > 0 ? 1 : 0], AdditionalWeightedPressurePlateBlock::getCopyProps);
	private BiConsumer<BlockModelBuilder, String> actions = null;
	private boolean uvLock = false;

	public WeightedPressurePlateModelsBuilder(BlockStateProvider stateProvider)
	{
		this.stateProvider = stateProvider;
	}

	public WeightedPressurePlateModelsBuilder reset()
	{
		pressurePlateDef.clear();
		actions = null;
		uvLock = false;
		return this;
	}

	public WeightedPressurePlateModelsBuilder addAction(BiConsumer<BlockModelBuilder, String> action)
	{
		actions = actions == null ? action : actions.andThen(action);
		return this;
	}

	public WeightedPressurePlateModelsBuilder setTextures(String side, String top, String bottom)
	{
		return addAction((builder, model) ->
		builder
		.texture("side", side)
		.texture("top", top)
		.texture("bottom", bottom));
	}

	public WeightedPressurePlateModelsBuilder setBase(String pressurePlateParentMod, String pressurePlateParentFolder)
	{
		if (pressurePlateDef.has) pressurePlateDef.setParent(pressurePlateParentMod, pressurePlateParentFolder);
		return this;
	}

	public WeightedPressurePlateModelsBuilder setPillar(ResourceLocation side, ResourceLocation top, ResourceLocation bottom)
	{
		return setBase(AdditionalPlacementsMod.MOD_ID, PressurePlateModelsBuilder.PRESSURE_PLATE_BASE).addAction((builder, model) ->
		builder
		.texture("side", side)
		.texture("top", top)
		.texture("bottom", bottom));
	}

	public WeightedPressurePlateModelsBuilder setColumn(ResourceLocation side, ResourceLocation end)
	{
		return setBase(AdditionalPlacementsMod.MOD_ID, PressurePlateModelsBuilder.PRESSURE_PLATE_COLUMN).addAction((builder, model) ->
		builder
		.texture("side", side)
		.texture("end", end));
	}

	public WeightedPressurePlateModelsBuilder setAllSides(ResourceLocation all)
	{
		return setBase(AdditionalPlacementsMod.MOD_ID, PressurePlateModelsBuilder.PRESSURE_PLATE_SIDE_ALL).addAction((builder, model) ->
		builder
		.texture("all", all));
	}

	public WeightedPressurePlateModelsBuilder setPressurePlate(AdditionalWeightedPressurePlateBlock block, String folder, String parentMod, String parentFolder)
	{
		pressurePlateDef.set(block, folder, parentMod, parentFolder);
		return this;
	}

	public WeightedPressurePlateModelsBuilder setPressurePlate(AdditionalWeightedPressurePlateBlock block, String folder)
	{
		return setPressurePlate(block, folder, null, null);
	}

	public WeightedPressurePlateModelsBuilder setPressurePlate(String folder, String parentMod, String parentFolder)
	{
		return setPressurePlate(null, folder, parentMod, parentFolder);
	}

	public WeightedPressurePlateModelsBuilder setPressurePlate(String folder)
	{
		return setPressurePlate(folder, null, null);
	}

	public WeightedPressurePlateModelsBuilder clearPressurePlate()
	{
		pressurePlateDef.clear();
		return this;
	}

	public WeightedPressurePlateModelsBuilder setUVLock(boolean uvLock)
	{
		this.uvLock = uvLock;
		return this;
	}

	public WeightedPressurePlateModelsBuilder clearActions()
	{
		this.actions = null;
		return this;
	}

	public WeightedPressurePlateModelsBuilder buildPressurePlate()
	{
		pressurePlateDef.build(stateProvider, actions, uvLock);
		return this;
	}

	public WeightedPressurePlateModelsBuilder compilePressurePlate()
	{
		return buildPressurePlate().clearPressurePlate();
	}

	public WeightedPressurePlateModelsBuilder compile()
	{
		return compilePressurePlate().clearActions();
	}
}