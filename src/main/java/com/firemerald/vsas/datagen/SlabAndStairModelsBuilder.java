package com.firemerald.vsas.datagen;

import java.util.function.Consumer;

import com.firemerald.vsas.VSASMod;
import com.firemerald.vsas.block.VerticalSlabBlock;
import com.firemerald.vsas.block.VerticalStairBlock;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;

public class SlabAndStairModelsBuilder
{
	public static final String[] VERTICAL_SLAB_MODELS = new String[] {
			"/side"
	};
	public static final String[] HORIZONTAL_SLAB_MODELS = new String[] {
			"/bottom",
			"/top",
			"/double"
	};
	public static final String SLAB_BASE = "block/slabs/base";
	public static final String SLAB_COLUMN = "block/slabs/column";
	public static final String SLAB_SIDE_ALL = "block/slabs/side_all";
	public static final StateModelDefinition[] HORIZONTAL_SLAB_MODEL_DEFINITIONS = new StateModelDefinition[3];
	public static final StateModelDefinition[] VERTICAL_SLAB_MODEL_DEFINITIONS = new StateModelDefinition[4];

	static
	{
		VERTICAL_SLAB_MODEL_DEFINITIONS[Direction.SOUTH.get2DDataValue()] = new StateModelDefinition("/side", 0);
		VERTICAL_SLAB_MODEL_DEFINITIONS[Direction.WEST.get2DDataValue()] = new StateModelDefinition("/side", 90);
		VERTICAL_SLAB_MODEL_DEFINITIONS[Direction.NORTH.get2DDataValue()] = new StateModelDefinition("/side", 180);
		VERTICAL_SLAB_MODEL_DEFINITIONS[Direction.EAST.get2DDataValue()] = new StateModelDefinition("/side", 270);
		HORIZONTAL_SLAB_MODEL_DEFINITIONS[SlabType.BOTTOM.ordinal()] = new StateModelDefinition("/bottom");
		HORIZONTAL_SLAB_MODEL_DEFINITIONS[SlabType.TOP.ordinal()] = new StateModelDefinition("/top");
		HORIZONTAL_SLAB_MODEL_DEFINITIONS[SlabType.DOUBLE.ordinal()] = new StateModelDefinition("/double");
	}

	public static final String[] VERTICAL_STAIR_MODELS = new String[] { //TODO
			"/vertical/straight",
			"/vertical/inner_up",
			"/vertical/outer_up",
			"/vertical/outer_up_cw",
			"/vertical/outer_up_ccw",
			"/vertical/inner_down",
			"/vertical/outer_down",
			"/vertical/outer_down_cw",
			"/vertical/outer_down_ccw"
	};
	public static final String[] HORIZONTAL_STAIR_MODELS = new String[] { //TODO
			"/bottom/straight",
			"/bottom/inner_left",
			"/bottom/outer_left",
			"/bottom/inner_right",
			"/bottom/outer_right",
			"/top/straight",
			"/top/inner_left",
			"/top/outer_left",
			"/top/inner_right",
			"/top/outer_right"
	};
	public static final String STAIRS_BASE = "block/stairs/base";
	public static final String STAIRS_COLUMN = "block/stairs/column";
	public static final String STAIRS_SIDE_ALL = "block/stairs/side_all";
	public static final StateModelDefinition[][][] HORIZONTAL_STAIR_MODEL_DEFINITIONS = new StateModelDefinition[2][4][5];
	public static final StateModelDefinition[][] VERTICAL_STAIR_MODEL_DEFINITIONS = new StateModelDefinition[4][17];

	static
	{
		setHorizontalStairModelDefs(Direction.NORTH, 180);
		setHorizontalStairModelDefs(Direction.EAST, 270);
		setHorizontalStairModelDefs(Direction.SOUTH, 0);
		setHorizontalStairModelDefs(Direction.WEST, 90);
		setVerticalStairModelDefs(VerticalStairBlock.EnumPlacing.NORTH_EAST, 0);
		setVerticalStairModelDefs(VerticalStairBlock.EnumPlacing.EAST_SOUTH, 90);
		setVerticalStairModelDefs(VerticalStairBlock.EnumPlacing.SOUTH_WEST, 180);
		setVerticalStairModelDefs(VerticalStairBlock.EnumPlacing.WEST_NORTH, 270);
	}
	
	private static void setHorizontalStairModelDefs(Direction direction, int rotation)
	{
		StateModelDefinition[] models = HORIZONTAL_STAIR_MODEL_DEFINITIONS[Half.BOTTOM.ordinal()][direction.get2DDataValue()];
		models[StairsShape.STRAIGHT.ordinal()] = new StateModelDefinition("/bottom/straight", rotation);
		models[StairsShape.INNER_LEFT.ordinal()] = new StateModelDefinition("/bottom/inner_left", rotation);
		models[StairsShape.OUTER_LEFT.ordinal()] = new StateModelDefinition("/bottom/outer_left", rotation);
		models[StairsShape.INNER_RIGHT.ordinal()] = new StateModelDefinition("/bottom/inner_right", rotation);
		models[StairsShape.OUTER_RIGHT.ordinal()] = new StateModelDefinition("/bottom/outer_right", rotation);
		models = HORIZONTAL_STAIR_MODEL_DEFINITIONS[Half.TOP.ordinal()][direction.get2DDataValue()];
		models[StairsShape.STRAIGHT.ordinal()] = new StateModelDefinition("/top/straight", rotation);
		models[StairsShape.INNER_LEFT.ordinal()] = new StateModelDefinition("/top/inner_left", rotation);
		models[StairsShape.OUTER_LEFT.ordinal()] = new StateModelDefinition("/top/outer_left", rotation);
		models[StairsShape.INNER_RIGHT.ordinal()] = new StateModelDefinition("/top/inner_right", rotation);
		models[StairsShape.OUTER_RIGHT.ordinal()] = new StateModelDefinition("/top/outer_right", rotation);
	}
	
	private static void setVerticalStairModelDefs(VerticalStairBlock.EnumPlacing placing, int rotation)
	{
		StateModelDefinition[] models = VERTICAL_STAIR_MODEL_DEFINITIONS[placing.ordinal()];
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
	private final ModelType horizontalSlabDef = new ModelType(true, HORIZONTAL_SLAB_MODELS, state -> HORIZONTAL_SLAB_MODEL_DEFINITIONS[state.getValue(SlabBlock.TYPE).ordinal()], SlabBlock.WATERLOGGED);
	private final ModelType verticalSlabDef = new ModelType(false, VERTICAL_SLAB_MODELS, state -> VERTICAL_SLAB_MODEL_DEFINITIONS[state.getValue(VerticalSlabBlock.PLACING).get2DDataValue()], VerticalSlabBlock.WATERLOGGED);
	private final ModelType horizontalStairDef = new ModelType(true, HORIZONTAL_STAIR_MODELS, state -> HORIZONTAL_STAIR_MODEL_DEFINITIONS[state.getValue(StairBlock.HALF).ordinal()][state.getValue(StairBlock.FACING).get2DDataValue()][state.getValue(StairBlock.SHAPE).ordinal()], StairBlock.WATERLOGGED);
	private final ModelType verticalStairDef = new ModelType(false, VERTICAL_STAIR_MODELS, state -> VERTICAL_STAIR_MODEL_DEFINITIONS[state.getValue(VerticalStairBlock.PLACING).ordinal()][state.getValue(VerticalStairBlock.SHAPE).ordinal()], VerticalStairBlock.WATERLOGGED);
	private Consumer<BlockModelBuilder> actions = null;
	private boolean uvLock = false;
	
	public SlabAndStairModelsBuilder(BlockStateProvider stateProvider)
	{
		this.stateProvider = stateProvider;
	}
	
	public SlabAndStairModelsBuilder reset()
	{
		horizontalSlabDef.clear();
		verticalSlabDef.clear();
		horizontalStairDef.clear();
		verticalStairDef.clear();
		actions = null;
		uvLock = false;
		return this;
	}
	
	public SlabAndStairModelsBuilder addAction(Consumer<BlockModelBuilder> action)
	{
		actions = actions == null ? action : actions.andThen(action);
		return this;
	}
	
	public SlabAndStairModelsBuilder setTextures(String side, String top, String bottom)
	{
		return addAction(model ->
		model
		.texture("side", side)
		.texture("top", top)
		.texture("bottom", bottom));
	}

	public SlabAndStairModelsBuilder setBase(String slabParentMod, String slabParentFolder, String stairsParentMod, String stairsParentFolder)
	{
		if (horizontalSlabDef.has) horizontalSlabDef.setParent(slabParentMod, slabParentFolder);
		if (verticalSlabDef.has) verticalSlabDef.setParent(slabParentMod, slabParentFolder);
		if (horizontalStairDef.has) horizontalStairDef.setParent(stairsParentMod, stairsParentFolder);
		if (verticalStairDef.has) verticalStairDef.setParent(stairsParentMod, stairsParentFolder);
		return this;
	}

	public SlabAndStairModelsBuilder setPillar(ResourceLocation side, ResourceLocation top, ResourceLocation bottom)
	{
		return setBase(VSASMod.MOD_ID, SLAB_BASE, VSASMod.MOD_ID, STAIRS_BASE).addAction(model ->
		model
		.texture("side", side)
		.texture("top", top)
		.texture("bottom", bottom));
	}

	public SlabAndStairModelsBuilder setColumn(ResourceLocation side, ResourceLocation end)
	{
		return setBase(VSASMod.MOD_ID, SLAB_COLUMN, VSASMod.MOD_ID, STAIRS_COLUMN).addAction(model ->
		model
		.texture("side", side)
		.texture("end", end));
	}

	public SlabAndStairModelsBuilder setAllSides(ResourceLocation all)
	{
		return setBase(VSASMod.MOD_ID, SLAB_SIDE_ALL, VSASMod.MOD_ID, STAIRS_SIDE_ALL).addAction(model ->
		model
		.texture("all", all));
	}
	
	public SlabAndStairModelsBuilder setHorizontalSlab(Block block, String folder, String parentMod, String parentFolder)
	{
		horizontalSlabDef.set(block, folder, parentMod, parentFolder);
		return this;
	}
	
	public SlabAndStairModelsBuilder setHorizontalSlab(Block block, String folder)
	{
		return setHorizontalSlab(block, folder, null, null);
	}
	
	public SlabAndStairModelsBuilder setHorizontalSlab(String folder, String parentMod, String parentFolder)
	{
		return setHorizontalSlab(null, folder, parentMod, parentFolder);
	}
	
	public SlabAndStairModelsBuilder setHorizontalSlab(String folder)
	{
		return setHorizontalSlab(folder, null, null);
	}
	
	public SlabAndStairModelsBuilder clearHorizontalSlab()
	{
		horizontalSlabDef.clear();
		return this;
	}
	
	public SlabAndStairModelsBuilder setVerticalSlab(Block block, String folder, String parentMod, String parentFolder)
	{
		verticalSlabDef.set(block, folder, parentMod, parentFolder);
		return this;
	}
	
	public SlabAndStairModelsBuilder setVerticalSlab(Block block, String folder)
	{
		return setVerticalSlab(block, folder, null, null);
	}
	
	public SlabAndStairModelsBuilder setVerticalSlab(String folder, String parentMod, String parentFolder)
	{
		return setVerticalSlab(null, folder, parentMod, parentFolder);
	}
	
	public SlabAndStairModelsBuilder setVerticalSlab(String folder)
	{
		return setVerticalSlab(folder, null, null);
	}
	
	public SlabAndStairModelsBuilder clearVerticalSlab()
	{
		verticalSlabDef.clear();
		return this;
	}
	
	public SlabAndStairModelsBuilder setSlab(String folder, String parentMod, String parentFolder)
	{
		return setHorizontalSlab(folder, parentMod, parentFolder).setVerticalSlab(folder, parentMod, parentFolder);
	}
	
	public SlabAndStairModelsBuilder setSlab(String folder)
	{
		return setHorizontalSlab(folder).setVerticalSlab(folder);
	}
	
	public SlabAndStairModelsBuilder setHorizontalStairs(Block block, String folder, String parentMod, String parentFolder)
	{
		horizontalStairDef.set(block, folder, parentMod, parentFolder);
		return this;
	}
	
	public SlabAndStairModelsBuilder setHorizontalStairs(Block block, String folder)
	{
		return setHorizontalStairs(block, folder, null, null);
	}
	
	public SlabAndStairModelsBuilder setHorizontalStairs(String folder, String parentMod, String parentFolder)
	{
		return setHorizontalStairs(null, folder, parentMod, parentFolder);
	}
	
	public SlabAndStairModelsBuilder setHorizontalStairs(String folder)
	{
		return setHorizontalStairs(folder, null, null);
	}
	
	public SlabAndStairModelsBuilder clearHorizontalStairs()
	{
		horizontalStairDef.clear();
		return this;
	}
	
	public SlabAndStairModelsBuilder setVerticalStairs(Block block, String folder, String parentMod, String parentFolder)
	{
		verticalStairDef.set(block, folder, parentMod, parentFolder);
		return this;
	}
	
	public SlabAndStairModelsBuilder setVerticalStairs(Block block, String folder)
	{
		return setVerticalStairs(block, folder, null, null);
	}
	
	public SlabAndStairModelsBuilder setVerticalStairs(String folder, String parentMod, String parentFolder)
	{
		return setVerticalStairs(null, folder, parentMod, parentFolder);
	}
	
	public SlabAndStairModelsBuilder setVerticalStairs(String folder)
	{
		return setVerticalStairs(folder, null, null);
	}
	
	public SlabAndStairModelsBuilder clearVerticalStairs()
	{
		verticalStairDef.clear();
		return this;
	}
	
	public SlabAndStairModelsBuilder setStairs(String folder, String parentMod, String parentFolder)
	{
		return setHorizontalStairs(folder, parentMod, parentFolder).setVerticalStairs(folder, parentMod, parentFolder);
	}
	
	public SlabAndStairModelsBuilder setStairs(String folder)
	{
		return setHorizontalStairs(folder).setVerticalStairs(folder);
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
	
	public SlabAndStairModelsBuilder buildHorizontalSlab()
	{
		horizontalSlabDef.build(stateProvider, actions, uvLock);
		return this;
	}
	
	public SlabAndStairModelsBuilder buildVerticalSlab()
	{
		verticalSlabDef.build(stateProvider, actions, uvLock);
		return this;
	}
	
	public SlabAndStairModelsBuilder buildSlabs()
	{
		return buildHorizontalSlab().buildVerticalSlab();
	}
	
	public SlabAndStairModelsBuilder buildHorizontalStairs()
	{
		horizontalStairDef.build(stateProvider, actions, uvLock);
		return this;
	}
	
	public SlabAndStairModelsBuilder buildVerticalStairs()
	{
		verticalStairDef.build(stateProvider, actions, uvLock);
		return this;
	}
	
	public SlabAndStairModelsBuilder buildStairs()
	{
		return buildHorizontalStairs().buildVerticalStairs();
	}
	
	public SlabAndStairModelsBuilder buildSlabsAndStairs()
	{
		return buildSlabs().buildStairs();
	}
	
	public SlabAndStairModelsBuilder compileHorizontalSlab()
	{
		return buildHorizontalSlab().clearHorizontalSlab();
	}
	
	public SlabAndStairModelsBuilder compileVerticalSlab()
	{
		return buildVerticalSlab().clearVerticalSlab();
	}
	
	public SlabAndStairModelsBuilder compileSlabs()
	{
		return compileHorizontalSlab().compileVerticalSlab();
	}
	
	public SlabAndStairModelsBuilder compileHorizontalStairs()
	{
		return buildHorizontalStairs().clearHorizontalStairs();
	}
	
	public SlabAndStairModelsBuilder compileVerticalStairs()
	{
		return buildVerticalStairs().clearVerticalStairs();
	}
	
	public SlabAndStairModelsBuilder compileStairs()
	{
		return compileHorizontalStairs().compileVerticalStairs();
	}
	
	public SlabAndStairModelsBuilder compileSlabsAndStairs()
	{
		return compileSlabs().compileStairs();
	}
	
	public SlabAndStairModelsBuilder compile()
	{
		return compileSlabsAndStairs().clearActions();
	}
}