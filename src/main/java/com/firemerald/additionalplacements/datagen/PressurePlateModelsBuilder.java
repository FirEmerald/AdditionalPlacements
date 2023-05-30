package com.firemerald.additionalplacements.datagen;

import java.util.function.BiConsumer;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.AdditionalPressurePlateBlock;

import net.minecraft.block.PressurePlateBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;

public class PressurePlateModelsBuilder
{
	public static final String[] PRESSURE_PLATE_MODELS = new String[] {
			"/unpressed",
			"/pressed",
	};
	public static final String PRESSURE_PLATE_BASE = "block/pressure_plates/base";
	public static final String PRESSURE_PLATE_COLUMN = "block/pressure_plates/column";
	public static final String PRESSURE_PLATE_SIDE_ALL = "block/pressure_plates/side_all";
	public static final String PRESSURE_PLATE_DYNAMIC = "block/pressure_plates/dynamic";
	public static final StateModelDefinition[][] PRESSURE_PLATE_DEFINITIONS = new StateModelDefinition[5][2];

	static
	{
		setStateModelDefinitions(Direction.UP, 180, 0);
		setStateModelDefinitions(Direction.SOUTH, 270, 180);
		setStateModelDefinitions(Direction.SOUTH, 270, 270);
		setStateModelDefinitions(Direction.SOUTH, 270, 0);
		setStateModelDefinitions(Direction.SOUTH, 270, 90);
	}
	
	static void setStateModelDefinitions(Direction dir, int rotX, int rotY)
	{
		StateModelDefinition[] array = PRESSURE_PLATE_DEFINITIONS[dir.ordinal() - 1];
		array[0] = new StateModelDefinition("/unpressed", rotX, rotY);
		array[1] = new StateModelDefinition("/pressed", rotX, rotY);
	}

	private final BlockStateProvider stateProvider;
	private final ModelType<AdditionalPressurePlateBlock> pressurePlateDef = new ModelType<>(false, PRESSURE_PLATE_MODELS, state -> PRESSURE_PLATE_DEFINITIONS[state.getValue(AdditionalPressurePlateBlock.PLACING).ordinal() - 1][state.getValue(PressurePlateBlock.POWERED) ? 1 : 0], AdditionalPressurePlateBlock::getCopyProps);
	private BiConsumer<BlockModelBuilder, String> actions = null;
	private boolean uvLock = false;

	public PressurePlateModelsBuilder(BlockStateProvider stateProvider)
	{
		this.stateProvider = stateProvider;
	}

	public PressurePlateModelsBuilder reset()
	{
		pressurePlateDef.clear();
		actions = null;
		uvLock = false;
		return this;
	}

	public PressurePlateModelsBuilder addAction(BiConsumer<BlockModelBuilder, String> action)
	{
		actions = actions == null ? action : actions.andThen(action);
		return this;
	}

	public PressurePlateModelsBuilder setTextures(String side, String top, String bottom)
	{
		return addAction((builder, model) ->
		builder
		.texture("side", side)
		.texture("top", top)
		.texture("bottom", bottom));
	}

	public PressurePlateModelsBuilder setBase(String pressurePlateParentMod, String pressurePlateParentFolder)
	{
		if (pressurePlateDef.has) pressurePlateDef.setParent(pressurePlateParentMod, pressurePlateParentFolder);
		return this;
	}

	public PressurePlateModelsBuilder setPillar(ResourceLocation side, ResourceLocation top, ResourceLocation bottom)
	{
		return setBase(AdditionalPlacementsMod.MOD_ID, PRESSURE_PLATE_BASE).addAction((builder, model) ->
		builder
		.texture("side", side)
		.texture("top", top)
		.texture("bottom", bottom));
	}

	public PressurePlateModelsBuilder setColumn(ResourceLocation side, ResourceLocation end)
	{
		return setBase(AdditionalPlacementsMod.MOD_ID, PRESSURE_PLATE_COLUMN).addAction((builder, model) ->
		builder
		.texture("side", side)
		.texture("end", end));
	}

	public PressurePlateModelsBuilder setAllSides(ResourceLocation all)
	{
		return setBase(AdditionalPlacementsMod.MOD_ID, PRESSURE_PLATE_SIDE_ALL).addAction((builder, model) ->
		builder
		.texture("all", all));
	}

	public PressurePlateModelsBuilder setPressurePlate(AdditionalPressurePlateBlock block, String folder, String parentMod, String parentFolder)
	{
		pressurePlateDef.set(block, folder, parentMod, parentFolder);
		return this;
	}

	public PressurePlateModelsBuilder setPressurePlate(AdditionalPressurePlateBlock block, String folder)
	{
		return setPressurePlate(block, folder, null, null);
	}

	public PressurePlateModelsBuilder setPressurePlate(String folder, String parentMod, String parentFolder)
	{
		return setPressurePlate(null, folder, parentMod, parentFolder);
	}

	public PressurePlateModelsBuilder setPressurePlate(String folder)
	{
		return setPressurePlate(folder, null, null);
	}

	public PressurePlateModelsBuilder clearPressurePlate()
	{
		pressurePlateDef.clear();
		return this;
	}

	public PressurePlateModelsBuilder setUVLock(boolean uvLock)
	{
		this.uvLock = uvLock;
		return this;
	}

	public PressurePlateModelsBuilder clearActions()
	{
		this.actions = null;
		return this;
	}

	public PressurePlateModelsBuilder buildPressurePlate()
	{
		pressurePlateDef.build(stateProvider, actions, uvLock);
		return this;
	}

	public PressurePlateModelsBuilder compilePressurePlate()
	{
		return buildPressurePlate().clearPressurePlate();
	}

	public PressurePlateModelsBuilder compile()
	{
		return compilePressurePlate().clearActions();
	}
}