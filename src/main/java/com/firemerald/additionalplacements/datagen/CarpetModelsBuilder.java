package com.firemerald.additionalplacements.datagen;

import java.util.function.BiConsumer;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.AdditionalCarpetBlock;

import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;

public class CarpetModelsBuilder
{
	public static final String[] CARPET_MODELS = new String[] {
			"/carpet"
	};
	public static final String CARPET_BASE = "block/carpets/base";
	public static final String CARPET_COLUMN = "block/carpets/column";
	public static final String CARPET_SIDE_ALL = "block/carpets/side_all";
	public static final String CARPET_DYNAMIC = "block/carpets/dynamic";
	public static final StateModelDefinition[] CARPET_MODEL_DEFINITIONS = new StateModelDefinition[5];

	static
	{
		CARPET_MODEL_DEFINITIONS[Direction.UP.ordinal() - 1] = new StateModelDefinition("/carpet", 180, 0);
		CARPET_MODEL_DEFINITIONS[Direction.SOUTH.ordinal() - 1] = new StateModelDefinition("/carpet", 270, 180);
		CARPET_MODEL_DEFINITIONS[Direction.WEST.ordinal() - 1] = new StateModelDefinition("/carpet", 270, 270);
		CARPET_MODEL_DEFINITIONS[Direction.NORTH.ordinal() - 1] = new StateModelDefinition("/carpet", 270, 0);
		CARPET_MODEL_DEFINITIONS[Direction.EAST.ordinal() - 1] = new StateModelDefinition("/carpet", 270, 90);
	}

	private final BlockStateProvider stateProvider;
	private final ModelType<AdditionalCarpetBlock> carpetDef = new ModelType<>(false, CARPET_MODELS, state -> CARPET_MODEL_DEFINITIONS[state.getValue(AdditionalCarpetBlock.PLACING).ordinal() - 1], AdditionalCarpetBlock::getCopyProps);
	private BiConsumer<BlockModelBuilder, String> actions = null;
	private boolean uvLock = false;

	public CarpetModelsBuilder(BlockStateProvider stateProvider)
	{
		this.stateProvider = stateProvider;
	}

	public CarpetModelsBuilder reset()
	{
		carpetDef.clear();
		actions = null;
		uvLock = false;
		return this;
	}

	public CarpetModelsBuilder addAction(BiConsumer<BlockModelBuilder, String> action)
	{
		actions = actions == null ? action : actions.andThen(action);
		return this;
	}

	public CarpetModelsBuilder setTextures(String side, String top, String bottom)
	{
		return addAction((builder, model) ->
		builder
		.texture("side", side)
		.texture("top", top)
		.texture("bottom", bottom));
	}

	public CarpetModelsBuilder setBase(String carpetParentMod, String carpetParentFolder)
	{
		if (carpetDef.has) carpetDef.setParent(carpetParentMod, carpetParentFolder);
		return this;
	}

	public CarpetModelsBuilder setPillar(ResourceLocation side, ResourceLocation top, ResourceLocation bottom)
	{
		return setBase(AdditionalPlacementsMod.MOD_ID, CARPET_BASE).addAction((builder, model) ->
		builder
		.texture("side", side)
		.texture("top", top)
		.texture("bottom", bottom));
	}

	public CarpetModelsBuilder setColumn(ResourceLocation side, ResourceLocation end)
	{
		return setBase(AdditionalPlacementsMod.MOD_ID, CARPET_COLUMN).addAction((builder, model) ->
		builder
		.texture("side", side)
		.texture("end", end));
	}

	public CarpetModelsBuilder setAllSides(ResourceLocation all)
	{
		return setBase(AdditionalPlacementsMod.MOD_ID, CARPET_SIDE_ALL).addAction((builder, model) ->
		builder
		.texture("all", all));
	}

	public CarpetModelsBuilder setCarpet(AdditionalCarpetBlock block, String folder, String parentMod, String parentFolder)
	{
		carpetDef.set(block, folder, parentMod, parentFolder);
		return this;
	}

	public CarpetModelsBuilder setCarpet(AdditionalCarpetBlock block, String folder)
	{
		return setCarpet(block, folder, null, null);
	}

	public CarpetModelsBuilder setCarpet(String folder, String parentMod, String parentFolder)
	{
		return setCarpet(null, folder, parentMod, parentFolder);
	}

	public CarpetModelsBuilder setCarpet(String folder)
	{
		return setCarpet(folder, null, null);
	}

	public CarpetModelsBuilder clearCarpet()
	{
		carpetDef.clear();
		return this;
	}

	public CarpetModelsBuilder setUVLock(boolean uvLock)
	{
		this.uvLock = uvLock;
		return this;
	}

	public CarpetModelsBuilder clearActions()
	{
		this.actions = null;
		return this;
	}

	public CarpetModelsBuilder buildCarpet()
	{
		carpetDef.build(stateProvider, actions, uvLock);
		return this;
	}

	public CarpetModelsBuilder compileCarpet()
	{
		return buildCarpet().clearCarpet();
	}

	public CarpetModelsBuilder compile()
	{
		return compileCarpet().clearActions();
	}
}