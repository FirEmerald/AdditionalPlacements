package com.firemerald.additionalplacements.client.models.definitions;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.AdditionalFloorBlock;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class CarpetModels {
	public static final ResourceLocation BASE_MODEL_FOLDER = ResourceLocation.fromNamespaceAndPath(AdditionalPlacementsMod.MOD_ID, "block/carpets/base");
	public static final ResourceLocation COLUMN_MODEL_FOLDER = ResourceLocation.fromNamespaceAndPath(AdditionalPlacementsMod.MOD_ID, "block/carpets/column");
	public static final ResourceLocation SIDE_ALL_MODEL_FOLDER = ResourceLocation.fromNamespaceAndPath(AdditionalPlacementsMod.MOD_ID, "block/carpets/side_all");

	public static final String[] MODELS = new String[] {
			"/carpet"
	};

	public static final StateModelDefinition[] MODEL_DEFINITIONS = new StateModelDefinition[5];

	static
	{
		MODEL_DEFINITIONS[Direction.UP.ordinal() - 1] = new StateModelDefinition("/carpet", 180, 0);
		MODEL_DEFINITIONS[Direction.SOUTH.ordinal() - 1] = new StateModelDefinition("/carpet", 270, 180);
		MODEL_DEFINITIONS[Direction.WEST.ordinal() - 1] = new StateModelDefinition("/carpet", 270, 270);
		MODEL_DEFINITIONS[Direction.NORTH.ordinal() - 1] = new StateModelDefinition("/carpet", 270, 0);
		MODEL_DEFINITIONS[Direction.EAST.ordinal() - 1] = new StateModelDefinition("/carpet", 270, 90);
	}

	public static StateModelDefinition getModel(BlockState state) {
		return MODEL_DEFINITIONS[state.getValue(AdditionalFloorBlock.PLACING).ordinal() - 1];
	}
}
