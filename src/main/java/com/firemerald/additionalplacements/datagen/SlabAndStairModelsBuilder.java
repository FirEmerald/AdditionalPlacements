package com.firemerald.additionalplacements.datagen;

import java.util.function.BiConsumer;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.VerticalSlabBlock;
import com.firemerald.additionalplacements.block.VerticalStairBlock;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;

public class SlabAndStairModelsBuilder
{
	public static final String[] SLAB_MODELS = new String[] {
			"/side"
	};
	public static final String SLAB_BASE = "block/slabs/base";
	public static final String SLAB_COLUMN = "block/slabs/column";
	public static final String SLAB_SIDE_ALL = "block/slabs/side_all";
	public static final String SLAB_DYNAMIC = "block/slabs/dynamic";
	public static final StateModelDefinition[] SLAB_MODEL_DEFINITIONS = new StateModelDefinition[4];

	static
	{
		SLAB_MODEL_DEFINITIONS[Direction.SOUTH.get2DDataValue()] = new StateModelDefinition("/side", 0);
		SLAB_MODEL_DEFINITIONS[Direction.WEST.get2DDataValue()] = new StateModelDefinition("/side", 90);
		SLAB_MODEL_DEFINITIONS[Direction.NORTH.get2DDataValue()] = new StateModelDefinition("/side", 180);
		SLAB_MODEL_DEFINITIONS[Direction.EAST.get2DDataValue()] = new StateModelDefinition("/side", 270);
	}

	public static final String[] STAIR_MODELS = new String[] {
			"/straight",
			"/inner_up",
			"/outer_up",
			"/outer_up_cw",
			"/outer_up_ccw",
			"/inner_down",
			"/outer_down",
			"/outer_down_cw",
			"/outer_down_ccw"
	};
	public static final String STAIRS_BASE = "block/stairs/base";
	public static final String STAIRS_COLUMN = "block/stairs/column";
	public static final String STAIRS_SIDE_ALL = "block/stairs/side_all";
	public static final String STAIRS_DYNAMIC = "block/stairs/dynamic";
	public static final StateModelDefinition[][] STAIR_MODEL_DEFINITIONS = new StateModelDefinition[4][17];

	static
	{
		setVerticalStairModelDefs(VerticalStairBlock.EnumPlacing.NORTH_EAST, 0);
		setVerticalStairModelDefs(VerticalStairBlock.EnumPlacing.EAST_SOUTH, 90);
		setVerticalStairModelDefs(VerticalStairBlock.EnumPlacing.SOUTH_WEST, 180);
		setVerticalStairModelDefs(VerticalStairBlock.EnumPlacing.WEST_NORTH, 270);
	}

	private static void setVerticalStairModelDefs(VerticalStairBlock.EnumPlacing placing, int rotation)
	{
		StateModelDefinition[] models = STAIR_MODEL_DEFINITIONS[placing.ordinal()];
		models[VerticalStairBlock.EnumShape.STRAIGHT.ordinal()] = new StateModelDefinition("/vertical/straight", rotation);
		models[VerticalStairBlock.EnumShape.INNER_UP.ordinal()] = new StateModelDefinition("/vertical/inner_up", rotation);
		models[VerticalStairBlock.EnumShape.OUTER_UP.ordinal()] =
		models[VerticalStairBlock.EnumShape.OUTER_UP_FROM_CW.ordinal()] =
		models[VerticalStairBlock.EnumShape.OUTER_UP_FROM_CCW.ordinal()] = new StateModelDefinition("/vertical/outer_up", rotation);
		models[VerticalStairBlock.EnumShape.OUTER_FLAT_UP_CW.ordinal()] =
		models[VerticalStairBlock.EnumShape.OUTER_FLAT_UP_FROM_CW.ordinal()] = new StateModelDefinition("/vertical/outer_up_cw", rotation);
		models[VerticalStairBlock.EnumShape.OUTER_FLAT_UP_CCW.ordinal()] =
		models[VerticalStairBlock.EnumShape.OUTER_FLAT_UP_FROM_CCW.ordinal()] = new StateModelDefinition("/vertical/outer_up_ccw", rotation);
		models[VerticalStairBlock.EnumShape.INNER_DOWN.ordinal()] = new StateModelDefinition("/vertical/inner_down", rotation);
		models[VerticalStairBlock.EnumShape.OUTER_DOWN.ordinal()] =
		models[VerticalStairBlock.EnumShape.OUTER_DOWN_FROM_CW.ordinal()] =
		models[VerticalStairBlock.EnumShape.OUTER_DOWN_FROM_CCW.ordinal()] = new StateModelDefinition("/vertical/outer_down", rotation);
		models[VerticalStairBlock.EnumShape.OUTER_FLAT_DOWN_CW.ordinal()] =
		models[VerticalStairBlock.EnumShape.OUTER_FLAT_DOWN_FROM_CW.ordinal()] = new StateModelDefinition("/vertical/outer_down_cw", rotation);
		models[VerticalStairBlock.EnumShape.OUTER_FLAT_DOWN_CCW.ordinal()] =
		models[VerticalStairBlock.EnumShape.OUTER_FLAT_DOWN_FROM_CCW.ordinal()] = new StateModelDefinition("/vertical/outer_down_ccw", rotation);
	}

	private final BlockStateProvider stateProvider;
	private final ModelType<VerticalSlabBlock> slabDef = new ModelType<>(false, SLAB_MODELS, state -> SLAB_MODEL_DEFINITIONS[state.getValue(VerticalSlabBlock.PLACING).get2DDataValue()], VerticalSlabBlock::getCopyProps);
	private final ModelType<VerticalStairBlock> stairDef = new ModelType<>(false, STAIR_MODELS, state -> STAIR_MODEL_DEFINITIONS[state.getValue(VerticalStairBlock.PLACING).ordinal()][state.getValue(VerticalStairBlock.SHAPE).ordinal()], VerticalStairBlock::getCopyProps);
	private BiConsumer<BlockModelBuilder, String> actions = null;
	private boolean uvLock = false;

	public SlabAndStairModelsBuilder(BlockStateProvider stateProvider)
	{
		this.stateProvider = stateProvider;
	}

	public SlabAndStairModelsBuilder reset()
	{
		slabDef.clear();
		stairDef.clear();
		actions = null;
		uvLock = false;
		return this;
	}

	public SlabAndStairModelsBuilder addAction(BiConsumer<BlockModelBuilder, String> action)
	{
		actions = actions == null ? action : actions.andThen(action);
		return this;
	}

	public SlabAndStairModelsBuilder setTextures(String side, String top, String bottom)
	{
		return addAction((builder, model) ->
		builder
		.texture("side", side)
		.texture("top", top)
		.texture("bottom", bottom));
	}

	public SlabAndStairModelsBuilder setBase(String slabParentMod, String slabParentFolder, String stairsParentMod, String stairsParentFolder)
	{
		if (slabDef.has) slabDef.setParent(slabParentMod, slabParentFolder);
		if (stairDef.has) stairDef.setParent(stairsParentMod, stairsParentFolder);
		return this;
	}

	public SlabAndStairModelsBuilder setPillar(ResourceLocation side, ResourceLocation top, ResourceLocation bottom)
	{
		return setBase(AdditionalPlacementsMod.MOD_ID, SLAB_BASE, AdditionalPlacementsMod.MOD_ID, STAIRS_BASE).addAction((builder, model) ->
		builder
		.texture("side", side)
		.texture("top", top)
		.texture("bottom", bottom));
	}

	public SlabAndStairModelsBuilder setColumn(ResourceLocation side, ResourceLocation end)
	{
		return setBase(AdditionalPlacementsMod.MOD_ID, SLAB_COLUMN, AdditionalPlacementsMod.MOD_ID, STAIRS_COLUMN).addAction((builder, model) ->
		builder
		.texture("side", side)
		.texture("end", end));
	}

	public SlabAndStairModelsBuilder setAllSides(ResourceLocation all)
	{
		return setBase(AdditionalPlacementsMod.MOD_ID, SLAB_SIDE_ALL, AdditionalPlacementsMod.MOD_ID, STAIRS_SIDE_ALL).addAction((builder, model) ->
		builder
		.texture("all", all));
	}

	public SlabAndStairModelsBuilder setSlab(VerticalSlabBlock block, String folder, String parentMod, String parentFolder)
	{
		slabDef.set(block, folder, parentMod, parentFolder);
		return this;
	}

	public SlabAndStairModelsBuilder setSlab(VerticalSlabBlock block, String folder)
	{
		return setSlab(block, folder, null, null);
	}

	public SlabAndStairModelsBuilder setSlab(String folder, String parentMod, String parentFolder)
	{
		return setSlab(null, folder, parentMod, parentFolder);
	}

	public SlabAndStairModelsBuilder setSlab(String folder)
	{
		return setSlab(folder, null, null);
	}

	public SlabAndStairModelsBuilder clearSlab()
	{
		slabDef.clear();
		return this;
	}

	public SlabAndStairModelsBuilder setStairs(VerticalStairBlock block, String folder, String parentMod, String parentFolder)
	{
		stairDef.set(block, folder, parentMod, parentFolder);
		return this;
	}

	public SlabAndStairModelsBuilder setStairs(VerticalStairBlock block, String folder)
	{
		return setStairs(block, folder, null, null);
	}

	public SlabAndStairModelsBuilder setStairs(String folder, String parentMod, String parentFolder)
	{
		return setStairs(null, folder, parentMod, parentFolder);
	}

	public SlabAndStairModelsBuilder setStairs(String folder)
	{
		return setStairs(folder, null, null);
	}

	public SlabAndStairModelsBuilder clearStairs()
	{
		stairDef.clear();
		return this;
	}

	public SlabAndStairModelsBuilder setUVLock(boolean uvLock)
	{
		this.uvLock = uvLock;
		return this;
	}

	public SlabAndStairModelsBuilder clearActions()
	{
		this.actions = null;
		return this;
	}

	public SlabAndStairModelsBuilder buildSlab()
	{
		slabDef.build(stateProvider, actions, uvLock);
		return this;
	}

	public SlabAndStairModelsBuilder buildStairs()
	{
		stairDef.build(stateProvider, actions, uvLock);
		return this;
	}

	public SlabAndStairModelsBuilder build()
	{
		return buildSlab().buildStairs();
	}

	public SlabAndStairModelsBuilder compileSlab()
	{
		return buildSlab().clearSlab();
	}

	public SlabAndStairModelsBuilder compileStairs()
	{
		return buildStairs().clearStairs();
	}

	public SlabAndStairModelsBuilder compile()
	{
		return compileSlab().compileStairs().clearActions();
	}
}