package com.firemerald.additionalplacements.client.models.definitions;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.AdditionalFloorBlock;

import com.mojang.math.Quadrant;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.WeightedPressurePlateBlock;
import net.minecraft.world.level.block.state.BlockState;

public class PressurePlateModels {
	public static final ResourceLocation BASE_MODEL_FOLDER = ResourceLocation.fromNamespaceAndPath(AdditionalPlacementsMod.MOD_ID, "block/pressure_plates/base");
	public static final ResourceLocation COLUMN_MODEL_FOLDER = ResourceLocation.fromNamespaceAndPath(AdditionalPlacementsMod.MOD_ID, "block/pressure_plates/column");
	public static final ResourceLocation SIDE_ALL_MODEL_FOLDER = ResourceLocation.fromNamespaceAndPath(AdditionalPlacementsMod.MOD_ID, "block/pressure_plates/side_all");

	public static final String[] MODELS = new String[] {
			"/unpressed",
			"/pressed",
	};

	public static final StateModelDefinition[][] MODEL_DEFINITIONS = new StateModelDefinition[5][2];

	static
	{
		setStateModelDefinitions(Direction.UP, Quadrant.R180, Quadrant.R0);
		setStateModelDefinitions(Direction.SOUTH, Quadrant.R270, Quadrant.R180);
		setStateModelDefinitions(Direction.EAST, Quadrant.R270, Quadrant.R270);
		setStateModelDefinitions(Direction.NORTH, Quadrant.R270, Quadrant.R0);
		setStateModelDefinitions(Direction.WEST, Quadrant.R270, Quadrant.R90);
	}

	static void setStateModelDefinitions(Direction dir, Quadrant rotX, Quadrant rotY)
	{
		StateModelDefinition[] array = MODEL_DEFINITIONS[dir.ordinal() - 1];
		array[0] = new StateModelDefinition("/unpressed", rotX, rotY);
		array[1] = new StateModelDefinition("/pressed", rotX, rotY);
	}

	public static StateModelDefinition getPressurePlateModel(BlockState state) {
		return MODEL_DEFINITIONS[state.getValue(AdditionalFloorBlock.PLACING).ordinal() - 1][state.getValue(PressurePlateBlock.POWERED) ? 1 : 0];
	}

	public static StateModelDefinition getWeightedPressurePlateModel(BlockState state) {
		return MODEL_DEFINITIONS[state.getValue(AdditionalFloorBlock.PLACING).ordinal() - 1][state.getValue(WeightedPressurePlateBlock.POWER) > 0 ? 1 : 0];
	}
}
