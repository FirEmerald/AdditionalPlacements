package com.firemerald.additionalplacements.datagen;

import java.util.function.BiConsumer;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.VerticalSlabBlock;
import com.firemerald.additionalplacements.block.VerticalStairBlock;
import com.firemerald.additionalplacements.block.interfaces.ISlabBlock;
import com.firemerald.additionalplacements.block.interfaces.IStairBlock;
import com.firemerald.additionalplacements.util.stairs.CompressedStairFacing;
import com.firemerald.additionalplacements.util.stairs.CompressedStairShape;

import net.minecraft.core.Direction.Axis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;

public class SlabAndStairModelsBuilder
{
	public static final String[] SLAB_MODELS = new String[] {
			"/negative",
			"/positive",
			"/double"
	};
	public static final String SLAB_BASE = "block/slabs/base";
	public static final String SLAB_COLUMN = "block/slabs/column";
	public static final String SLAB_SIDE_ALL = "block/slabs/side_all";
	public static final String SLAB_DYNAMIC = "block/slabs/dynamic";
	public static final StateModelDefinition[][] SLAB_MODEL_DEFINITIONS = new StateModelDefinition[2][3];

	static
	{
		SLAB_MODEL_DEFINITIONS[0][SlabType.BOTTOM.ordinal()] = new StateModelDefinition("/negative", 270);
		SLAB_MODEL_DEFINITIONS[0][SlabType.TOP.ordinal()] = new StateModelDefinition("/positive", 270);
		SLAB_MODEL_DEFINITIONS[0][SlabType.DOUBLE.ordinal()] = new StateModelDefinition("/double", 270);
		SLAB_MODEL_DEFINITIONS[1][SlabType.BOTTOM.ordinal()] = new StateModelDefinition("/negative", 0);
		SLAB_MODEL_DEFINITIONS[1][SlabType.TOP.ordinal()] = new StateModelDefinition("/positive", 0);
		SLAB_MODEL_DEFINITIONS[1][SlabType.DOUBLE.ordinal()] = new StateModelDefinition("/double", 0);
	}

	public static final String[] STAIR_MODELS = new String[] {
			"/top/straight",
			"/top/inner",
			"/top/outer_back",
			"/top/outer_bottom_left",
			"/top/outer_bottom_right",
			"/top/outer_both",
			"/top/twist_left",
			"/top/twist_right",
			"/bottom/straight",
			"/bottom/inner",
			"/bottom/outer_back",
			"/bottom/outer_top_left",
			"/bottom/outer_top_right",
			"/bottom/outer_both",
			"/bottom/twist_left",
			"/bottom/twist_right",
			"/side/straight",
			"/side/twist_left",
			"/side/twist_right"
	};
	public static final String STAIRS_BASE = "block/stairs/base";
	public static final String STAIRS_COLUMN = "block/stairs/column";
	public static final String STAIRS_SIDE_ALL = "block/stairs/side_all";
	public static final String STAIRS_DYNAMIC = "block/stairs/dynamic";
	public static final StateModelDefinition[][] STAIR_MODEL_DEFINITIONS = new StateModelDefinition[8][40];

	static
	{
		/*
		setUpStairModelDefs(ComplexFacing.SOUTH_UP, ComplexFacing.UP_SOUTH, 0);
		setUpStairModelDefs(ComplexFacing.WEST_UP, ComplexFacing.UP_WEST, 90);
		setUpStairModelDefs(ComplexFacing.NORTH_UP, ComplexFacing.UP_NORTH, 180);
		setUpStairModelDefs(ComplexFacing.EAST_UP, ComplexFacing.UP_EAST, 270);

		setDownStairModelDefs(ComplexFacing.SOUTH_DOWN, ComplexFacing.DOWN_SOUTH, 0);
		setDownStairModelDefs(ComplexFacing.WEST_DOWN, ComplexFacing.DOWN_WEST, 90);
		setDownStairModelDefs(ComplexFacing.NORTH_DOWN, ComplexFacing.DOWN_NORTH, 180);
		setDownStairModelDefs(ComplexFacing.EAST_DOWN, ComplexFacing.DOWN_EAST, 270);

		setVerticalStairModelDefs(ComplexFacing.SOUTH_EAST, ComplexFacing.EAST_SOUTH, 0);
		setVerticalStairModelDefs(ComplexFacing.WEST_SOUTH, ComplexFacing.SOUTH_WEST, 90);
		setVerticalStairModelDefs(ComplexFacing.NORTH_WEST, ComplexFacing.WEST_NORTH, 180);
		setVerticalStairModelDefs(ComplexFacing.EAST_NORTH, ComplexFacing.NORTH_EAST, 270);
		
		Map<StateModelDefinition, List<Pair<CompressedStairFacing, CompressedStairShape>>> map = new LinkedHashMap<>();
		for (String model : STAIR_MODELS) for (int i = 0; i < 360; i += 90) map.put(new StateModelDefinition(model, i), new ArrayList<>());
		for (CompressedStairFacing facing : CompressedStairFacing.ALL_FACINGS) for (CompressedStairShape shape : CompressedStairShape.ALL_SHAPES) {
			map.get(STAIR_MODEL_DEFINITIONS[facing.ordinal()][shape.ordinal()]).add(Pair.of(facing, shape));
		}
		map.forEach((model, states) -> {
			StringJoiner joiner = new StringJoiner("\n");
			states.forEach(state -> {
				joiner.add("STAIR_MODEL_DEFINITIONS[CompressedStairFacing." + state.getLeft().name() + ".ordinal()][CompressedStairShape." + state.getRight().name() + ".ordinal()] = ");
			});
			System.out.println(joiner.toString() + "new StateModelDefinition(\"" + model.model() + "\", " + model.yRotation() + ");");
		});
		*/
		
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.FLIPPED_STRAIGHT.ordinal()] = new StateModelDefinition("/top/straight", 0);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.FLIPPED_STRAIGHT.ordinal()] = new StateModelDefinition("/top/straight", 90);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.FLIPPED_STRAIGHT.ordinal()] = new StateModelDefinition("/top/straight", 180);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.FLIPPED_STRAIGHT.ordinal()] = new StateModelDefinition("/top/straight", 270);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.NORMAL_INNER_TOP_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.NORMAL_INNER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.FLIPPED_INNER_FRONT_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.FLIPPED_INNER_TOP_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.FLIPPED_INNER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.VERTICAL_INNER_FRONT_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.VERTICAL_INNER_TOP_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.VERTICAL_INNER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.NORMAL_INNER_TOP_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.NORMAL_INNER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.FLIPPED_INNER_FRONT_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.FLIPPED_INNER_TOP_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.FLIPPED_INNER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.VERTICAL_INNER_FRONT_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.VERTICAL_INNER_TOP_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.VERTICAL_INNER_BOTH_LEFT.ordinal()] = new StateModelDefinition("/top/inner", 0);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.NORMAL_INNER_TOP_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.NORMAL_INNER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.FLIPPED_INNER_FRONT_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.FLIPPED_INNER_TOP_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.FLIPPED_INNER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.NORMAL_INNER_TOP_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.NORMAL_INNER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.FLIPPED_INNER_FRONT_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.FLIPPED_INNER_TOP_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.FLIPPED_INNER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.VERTICAL_INNER_FRONT_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.VERTICAL_INNER_TOP_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.VERTICAL_INNER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.VERTICAL_INNER_FRONT_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.VERTICAL_INNER_TOP_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.VERTICAL_INNER_BOTH_LEFT.ordinal()] = new StateModelDefinition("/top/inner", 90);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.NORMAL_INNER_TOP_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.NORMAL_INNER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.FLIPPED_INNER_FRONT_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.FLIPPED_INNER_TOP_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.FLIPPED_INNER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.NORMAL_INNER_TOP_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.NORMAL_INNER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.FLIPPED_INNER_FRONT_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.FLIPPED_INNER_TOP_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.FLIPPED_INNER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.VERTICAL_INNER_FRONT_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.VERTICAL_INNER_TOP_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.VERTICAL_INNER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.VERTICAL_INNER_FRONT_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.VERTICAL_INNER_TOP_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.VERTICAL_INNER_BOTH_LEFT.ordinal()] = new StateModelDefinition("/top/inner", 180);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.NORMAL_INNER_TOP_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.NORMAL_INNER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.FLIPPED_INNER_FRONT_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.FLIPPED_INNER_TOP_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.FLIPPED_INNER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.NORMAL_INNER_TOP_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.NORMAL_INNER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.FLIPPED_INNER_FRONT_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.FLIPPED_INNER_TOP_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.FLIPPED_INNER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.VERTICAL_INNER_FRONT_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.VERTICAL_INNER_TOP_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.VERTICAL_INNER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.VERTICAL_INNER_FRONT_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.VERTICAL_INNER_TOP_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.VERTICAL_INNER_BOTH_LEFT.ordinal()] = new StateModelDefinition("/top/inner", 270);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTTOM_LEFT.ordinal()] = new StateModelDefinition("/top/outer_back", 0);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTTOM_RIGHT.ordinal()] = new StateModelDefinition("/top/outer_back", 90);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTTOM_RIGHT.ordinal()] = new StateModelDefinition("/top/outer_back", 180);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTTOM_RIGHT.ordinal()] = new StateModelDefinition("/top/outer_back", 270);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_LEFT.ordinal()] = new StateModelDefinition("/top/outer_bottom_left", 0);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_LEFT.ordinal()] = new StateModelDefinition("/top/outer_bottom_left", 90);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_LEFT.ordinal()] = new StateModelDefinition("/top/outer_bottom_left", 180);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_LEFT.ordinal()] = new StateModelDefinition("/top/outer_bottom_left", 270);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTTOM_LEFT.ordinal()] = new StateModelDefinition("/top/outer_bottom_right", 0);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTTOM_LEFT.ordinal()] = new StateModelDefinition("/top/outer_bottom_right", 90);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTTOM_LEFT.ordinal()] = new StateModelDefinition("/top/outer_bottom_right", 180);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTTOM_LEFT.ordinal()] = new StateModelDefinition("/top/outer_bottom_right", 270);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTH_LEFT.ordinal()] = new StateModelDefinition("/top/outer_both", 0);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTH_LEFT.ordinal()] = new StateModelDefinition("/top/outer_both", 90);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTH_LEFT.ordinal()] = new StateModelDefinition("/top/outer_both", 180);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTH_LEFT.ordinal()] = new StateModelDefinition("/top/outer_both", 270);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.NORMAL_OUTER_BACK_RIGHT_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_RIGHT_BOTTOM_LEFT.ordinal()] = new StateModelDefinition("/top/twist_left", 0);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.NORMAL_OUTER_BACK_RIGHT_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_RIGHT_BOTTOM_LEFT.ordinal()] = new StateModelDefinition("/top/twist_left", 90);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.NORMAL_OUTER_BACK_RIGHT_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_RIGHT_BOTTOM_LEFT.ordinal()] = new StateModelDefinition("/top/twist_left", 180);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.NORMAL_OUTER_BACK_RIGHT_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_RIGHT_BOTTOM_LEFT.ordinal()] = new StateModelDefinition("/top/twist_left", 270);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.NORMAL_OUTER_BACK_LEFT_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_LEFT_BOTTOM_RIGHT.ordinal()] = new StateModelDefinition("/top/twist_right", 0);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.NORMAL_OUTER_BACK_LEFT_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_LEFT_BOTTOM_RIGHT.ordinal()] = new StateModelDefinition("/top/twist_right", 90);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.NORMAL_OUTER_BACK_LEFT_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_LEFT_BOTTOM_RIGHT.ordinal()] = new StateModelDefinition("/top/twist_right", 180);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.NORMAL_OUTER_BACK_LEFT_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_LEFT_BOTTOM_RIGHT.ordinal()] = new StateModelDefinition("/top/twist_right", 270);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.FLIPPED_STRAIGHT.ordinal()] = new StateModelDefinition("/bottom/straight", 0);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.FLIPPED_STRAIGHT.ordinal()] = new StateModelDefinition("/bottom/straight", 90);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.FLIPPED_STRAIGHT.ordinal()] = new StateModelDefinition("/bottom/straight", 180);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.FLIPPED_STRAIGHT.ordinal()] = new StateModelDefinition("/bottom/straight", 270);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.VERTICAL_INNER_FRONT_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.VERTICAL_INNER_TOP_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.VERTICAL_INNER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.NORMAL_INNER_TOP_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.NORMAL_INNER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.FLIPPED_INNER_FRONT_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.FLIPPED_INNER_TOP_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.FLIPPED_INNER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.NORMAL_INNER_TOP_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.NORMAL_INNER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.FLIPPED_INNER_FRONT_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.FLIPPED_INNER_TOP_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.FLIPPED_INNER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.VERTICAL_INNER_FRONT_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.VERTICAL_INNER_TOP_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.VERTICAL_INNER_BOTH_RIGHT.ordinal()] = new StateModelDefinition("/bottom/inner", 0);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.VERTICAL_INNER_FRONT_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.VERTICAL_INNER_TOP_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.VERTICAL_INNER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.NORMAL_INNER_TOP_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.NORMAL_INNER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.FLIPPED_INNER_FRONT_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.FLIPPED_INNER_TOP_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.FLIPPED_INNER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.VERTICAL_INNER_FRONT_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.VERTICAL_INNER_TOP_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.VERTICAL_INNER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.NORMAL_INNER_TOP_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.NORMAL_INNER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.FLIPPED_INNER_FRONT_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.FLIPPED_INNER_TOP_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.FLIPPED_INNER_BOTH_LEFT.ordinal()] = new StateModelDefinition("/bottom/inner", 90);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.VERTICAL_INNER_FRONT_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.VERTICAL_INNER_TOP_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.VERTICAL_INNER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.NORMAL_INNER_TOP_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.NORMAL_INNER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.FLIPPED_INNER_FRONT_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.FLIPPED_INNER_TOP_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.FLIPPED_INNER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.VERTICAL_INNER_FRONT_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.VERTICAL_INNER_TOP_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.VERTICAL_INNER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.NORMAL_INNER_TOP_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.NORMAL_INNER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.FLIPPED_INNER_FRONT_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.FLIPPED_INNER_TOP_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.FLIPPED_INNER_BOTH_LEFT.ordinal()] = new StateModelDefinition("/bottom/inner", 180);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.VERTICAL_INNER_FRONT_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.VERTICAL_INNER_TOP_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.VERTICAL_INNER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.NORMAL_INNER_TOP_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.NORMAL_INNER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.FLIPPED_INNER_FRONT_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.FLIPPED_INNER_TOP_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.FLIPPED_INNER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.VERTICAL_INNER_FRONT_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.VERTICAL_INNER_TOP_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.VERTICAL_INNER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.NORMAL_INNER_TOP_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.NORMAL_INNER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.FLIPPED_INNER_FRONT_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.FLIPPED_INNER_TOP_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.FLIPPED_INNER_BOTH_LEFT.ordinal()] = new StateModelDefinition("/bottom/inner", 270);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTTOM_RIGHT.ordinal()] = new StateModelDefinition("/bottom/outer_back", 0);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTTOM_LEFT.ordinal()] = new StateModelDefinition("/bottom/outer_back", 90);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTTOM_LEFT.ordinal()] = new StateModelDefinition("/bottom/outer_back", 180);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTTOM_LEFT.ordinal()] = new StateModelDefinition("/bottom/outer_back", 270);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_RIGHT.ordinal()] = new StateModelDefinition("/bottom/outer_top_left", 0);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_LEFT.ordinal()] = new StateModelDefinition("/bottom/outer_top_left", 90);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_LEFT.ordinal()] = new StateModelDefinition("/bottom/outer_top_left", 180);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_LEFT.ordinal()] = new StateModelDefinition("/bottom/outer_top_left", 270);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTTOM_RIGHT.ordinal()] = new StateModelDefinition("/bottom/outer_top_right", 0);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTTOM_RIGHT.ordinal()] = new StateModelDefinition("/bottom/outer_top_right", 90);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTTOM_RIGHT.ordinal()] = new StateModelDefinition("/bottom/outer_top_right", 180);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTTOM_RIGHT.ordinal()] = new StateModelDefinition("/bottom/outer_top_right", 270);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTH_RIGHT.ordinal()] = new StateModelDefinition("/bottom/outer_both", 0);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTH_LEFT.ordinal()] = new StateModelDefinition("/bottom/outer_both", 90);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTH_LEFT.ordinal()] = new StateModelDefinition("/bottom/outer_both", 180);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTH_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.NORMAL_OUTER_BOTH_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BOTH_LEFT.ordinal()] = new StateModelDefinition("/bottom/outer_both", 270);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.NORMAL_OUTER_BACK_LEFT_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_LEFT_BOTTOM_RIGHT.ordinal()] = new StateModelDefinition("/bottom/twist_left", 0);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.NORMAL_OUTER_BACK_LEFT_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_LEFT_BOTTOM_RIGHT.ordinal()] = new StateModelDefinition("/bottom/twist_left", 90);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.NORMAL_OUTER_BACK_LEFT_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_LEFT_BOTTOM_RIGHT.ordinal()] = new StateModelDefinition("/bottom/twist_left", 180);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.NORMAL_OUTER_BACK_LEFT_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_LEFT_BOTTOM_RIGHT.ordinal()] = new StateModelDefinition("/bottom/twist_left", 270);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.NORMAL_OUTER_BACK_RIGHT_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_RIGHT_BOTTOM_LEFT.ordinal()] = new StateModelDefinition("/bottom/twist_right", 0);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.NORMAL_OUTER_BACK_RIGHT_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_RIGHT_BOTTOM_LEFT.ordinal()] = new StateModelDefinition("/bottom/twist_right", 90);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.NORMAL_OUTER_BACK_RIGHT_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_RIGHT_BOTTOM_LEFT.ordinal()] = new StateModelDefinition("/bottom/twist_right", 180);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.NORMAL_OUTER_BACK_RIGHT_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.FLIPPED_OUTER_BACK_RIGHT_BOTTOM_LEFT.ordinal()] = new StateModelDefinition("/bottom/twist_right", 270);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.VERTICAL_STRAIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.VERTICAL_STRAIGHT.ordinal()] = new StateModelDefinition("/side/straight", 0);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.VERTICAL_STRAIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.VERTICAL_STRAIGHT.ordinal()] = new StateModelDefinition("/side/straight", 90);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.VERTICAL_STRAIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.VERTICAL_STRAIGHT.ordinal()] = new StateModelDefinition("/side/straight", 180);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.VERTICAL_STRAIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.VERTICAL_STRAIGHT.ordinal()] = new StateModelDefinition("/side/straight", 270);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_LEFT_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_LEFT_BOTTOM_RIGHT.ordinal()] = new StateModelDefinition("/side/twist_left", 0);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_LEFT_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_LEFT_BOTTOM_RIGHT.ordinal()] = new StateModelDefinition("/side/twist_left", 90);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_LEFT_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_LEFT_BOTTOM_RIGHT.ordinal()] = new StateModelDefinition("/side/twist_left", 180);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_LEFT_BOTTOM_RIGHT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_LEFT_BOTTOM_RIGHT.ordinal()] = new StateModelDefinition("/side/twist_left", 270);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_UP_EAST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_RIGHT_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_DOWN_SOUTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_RIGHT_BOTTOM_LEFT.ordinal()] = new StateModelDefinition("/side/twist_right", 0);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_UP_SOUTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_RIGHT_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.SOUTH_DOWN_WEST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_RIGHT_BOTTOM_LEFT.ordinal()] = new StateModelDefinition("/side/twist_right", 90);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_UP_WEST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_RIGHT_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.WEST_DOWN_NORTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_RIGHT_BOTTOM_LEFT.ordinal()] = new StateModelDefinition("/side/twist_right", 180);
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.EAST_UP_NORTH.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_RIGHT_BOTTOM_LEFT.ordinal()] = 
		STAIR_MODEL_DEFINITIONS[CompressedStairFacing.NORTH_DOWN_EAST.ordinal()][CompressedStairShape.VERTICAL_OUTER_BACK_RIGHT_BOTTOM_LEFT.ordinal()] = new StateModelDefinition("/side/twist_right", 270);
	}
	/*
	private static void setUpStairModelDefs(ComplexFacing normal, ComplexFacing flipped, int rotation) {
		Pair<CompressedStairFacing, StairFacingType> normalPair = CompressedStairFacing.getCompressedFacing(normal);
		Pair<CompressedStairFacing, StairFacingType> flippedPair = CompressedStairFacing.getCompressedFacing(flipped);
		setUpStairModelDefs(STAIR_MODEL_DEFINITIONS[normalPair.getLeft().ordinal()], normalPair.getRight(), STAIR_MODEL_DEFINITIONS[flippedPair.getLeft().ordinal()], flippedPair.getRight(), rotation);
	}
	
	private static void setUpStairModelDefs(StateModelDefinition[] normal, StairFacingType normalType, StateModelDefinition[] flipped, StairFacingType flippedType, int rotation) {
		int rotation2 = (rotation + 90) % 360;
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.STRAIGHT, new StateModelDefinition("/top/straight", rotation));

		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.INNER_FRONT_LEFT, new StateModelDefinition("/top/inner", rotation));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.INNER_FRONT_RIGHT, new StateModelDefinition("/top/inner", rotation2));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.INNER_TOP_LEFT, new StateModelDefinition("/top/inner", rotation));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.INNER_TOP_RIGHT, new StateModelDefinition("/top/inner", rotation2));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.INNER_BOTH_LEFT, new StateModelDefinition("/top/inner", rotation));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.INNER_BOTH_RIGHT, new StateModelDefinition("/top/inner", rotation2));

		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.OUTER_BACK_LEFT, new StateModelDefinition("/top/outer_back", rotation));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.OUTER_BACK_RIGHT, new StateModelDefinition("/top/outer_back", rotation2));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.OUTER_BOTTOM_LEFT, new StateModelDefinition("/top/outer_bottom_left", rotation));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.OUTER_BOTTOM_RIGHT, new StateModelDefinition("/top/outer_bottom_right", rotation));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.OUTER_BOTH_LEFT, new StateModelDefinition("/top/outer_both", rotation));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.OUTER_BOTH_RIGHT, new StateModelDefinition("/top/outer_both", rotation2));

		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.OUTER_BACK_LEFT_BOTTOM_RIGHT, new StateModelDefinition("/top/twist_right", rotation));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.OUTER_BACK_RIGHT_BOTTOM_LEFT, new StateModelDefinition("/top/twist_left", rotation));
	}
	
	private static void setDownStairModelDefs(ComplexFacing normal, ComplexFacing flipped, int rotation) {
		Pair<CompressedStairFacing, StairFacingType> normalPair = CompressedStairFacing.getCompressedFacing(normal);
		Pair<CompressedStairFacing, StairFacingType> flippedPair = CompressedStairFacing.getCompressedFacing(flipped);
		setDownStairModelDefs(STAIR_MODEL_DEFINITIONS[normalPair.getLeft().ordinal()], normalPair.getRight(), STAIR_MODEL_DEFINITIONS[flippedPair.getLeft().ordinal()], flippedPair.getRight(), rotation);
	}
	
	private static void setDownStairModelDefs(StateModelDefinition[] normal, StairFacingType normalType, StateModelDefinition[] flipped, StairFacingType flippedType, int rotation) {
		int rotation2 = (rotation + 90) % 360;
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.STRAIGHT, new StateModelDefinition("/bottom/straight", rotation));

		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.INNER_FRONT_LEFT, new StateModelDefinition("/bottom/inner", rotation2));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.INNER_FRONT_RIGHT, new StateModelDefinition("/bottom/inner", rotation));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.INNER_TOP_LEFT, new StateModelDefinition("/bottom/inner", rotation2));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.INNER_TOP_RIGHT, new StateModelDefinition("/bottom/inner", rotation));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.INNER_BOTH_LEFT, new StateModelDefinition("/bottom/inner", rotation2));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.INNER_BOTH_RIGHT, new StateModelDefinition("/bottom/inner", rotation));

		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.OUTER_BACK_LEFT, new StateModelDefinition("/bottom/outer_back", rotation2));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.OUTER_BACK_RIGHT, new StateModelDefinition("/bottom/outer_back", rotation));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.OUTER_BOTTOM_LEFT, new StateModelDefinition("/bottom/outer_top_right", rotation));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.OUTER_BOTTOM_RIGHT, new StateModelDefinition("/bottom/outer_top_left", rotation));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.OUTER_BOTH_LEFT, new StateModelDefinition("/bottom/outer_both", rotation2));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.OUTER_BOTH_RIGHT, new StateModelDefinition("/bottom/outer_both", rotation));

		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.OUTER_BACK_LEFT_BOTTOM_RIGHT, new StateModelDefinition("/bottom/twist_left", rotation));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.OUTER_BACK_RIGHT_BOTTOM_LEFT, new StateModelDefinition("/bottom/twist_right", rotation));
	}
	
	private static void setVerticalStairModelDefs(ComplexFacing normal, ComplexFacing flipped, int rotation) {
		Pair<CompressedStairFacing, StairFacingType> normalPair = CompressedStairFacing.getCompressedFacing(normal);
		Pair<CompressedStairFacing, StairFacingType> flippedPair = CompressedStairFacing.getCompressedFacing(flipped);
		setVerticalStairModelDefs(STAIR_MODEL_DEFINITIONS[normalPair.getLeft().ordinal()], normalPair.getRight(), STAIR_MODEL_DEFINITIONS[flippedPair.getLeft().ordinal()], flippedPair.getRight(), rotation);
	}
	
	private static void setVerticalStairModelDefs(StateModelDefinition[] normal, StairFacingType normalType, StateModelDefinition[] flipped, StairFacingType flippedType, int rotation) {
		int rotation2 = (rotation + 270) % 360;
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.STRAIGHT, new StateModelDefinition("/side/straight", rotation));

		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.INNER_FRONT_LEFT, new StateModelDefinition("/top/inner", rotation), new StateModelDefinition("/bottom/inner", rotation));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.INNER_FRONT_RIGHT, new StateModelDefinition("/bottom/inner", rotation), new StateModelDefinition("/top/inner", rotation));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.INNER_TOP_LEFT, new StateModelDefinition("/bottom/inner", rotation));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.INNER_TOP_RIGHT, new StateModelDefinition("/top/inner", rotation));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.INNER_BOTH_LEFT, new StateModelDefinition("/bottom/inner", rotation));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.INNER_BOTH_RIGHT, new StateModelDefinition("/top/inner", rotation));

		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.OUTER_BACK_LEFT, new StateModelDefinition("/top/outer_bottom_right", rotation), new StateModelDefinition("/bottom/outer_top_right", rotation2));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.OUTER_BACK_RIGHT, new StateModelDefinition("/bottom/outer_top_right", rotation), new StateModelDefinition("/top/outer_bottom_right", rotation2));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.OUTER_BOTTOM_LEFT, new StateModelDefinition("/bottom/outer_top_left", rotation));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.OUTER_BOTTOM_RIGHT, new StateModelDefinition("/top/outer_bottom_left", rotation));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.OUTER_BOTH_LEFT, new StateModelDefinition("/bottom/outer_both", rotation));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.OUTER_BOTH_RIGHT, new StateModelDefinition("/top/outer_both", rotation));

		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.OUTER_BACK_LEFT_BOTTOM_RIGHT, new StateModelDefinition("/side/twist_left", rotation));
		setStairModelDef(normal, normalType, flipped, flippedType, StairShape.OUTER_BACK_RIGHT_BOTTOM_LEFT, new StateModelDefinition("/side/twist_right", rotation));
	}
	
	private static void setStairModelDef(StateModelDefinition[] normal, StairFacingType normalType, StateModelDefinition[] flipped, StairFacingType flippedType, StairShape shape, StateModelDefinition model) {
		CompressedStairShape normalShape = CompressedStairShape.getCompressedShape(normalType, shape);
		if (normalShape != null) normal[normalShape.ordinal()] = model;
		CompressedStairShape flippedShape = CompressedStairShape.getCompressedShape(flippedType, shape.flipped());
		if (flippedShape != null) flipped[flippedShape.ordinal()] = model;
	}
	
	private static void setStairModelDef(StateModelDefinition[] normal, StairFacingType normalType, StateModelDefinition[] flipped, StairFacingType flippedType, StairShape shape, StateModelDefinition normalModel, StateModelDefinition flippedModel) {
		CompressedStairShape normalShape = CompressedStairShape.getCompressedShape(normalType, shape);
		if (normalShape != null) normal[normalShape.ordinal()] = normalModel;
		CompressedStairShape flippedShape = CompressedStairShape.getCompressedShape(flippedType, shape.flipped());
		if (flippedShape != null) flipped[flippedShape.ordinal()] = flippedModel;
	}
	*/
	
	private final BlockStateProvider stateProvider;
	private final ModelType<VerticalSlabBlock> slabDef = new ModelType<>(false, SLAB_MODELS, state -> SLAB_MODEL_DEFINITIONS[state.getValue(VerticalSlabBlock.AXIS) == Axis.X ? 0 : 1][state.getValue(SlabBlock.TYPE).ordinal()], VerticalSlabBlock::getCopyProps);
	private final ModelType<VerticalStairBlock> stairDef = new ModelType<>(false, STAIR_MODELS, state -> STAIR_MODEL_DEFINITIONS[state.getValue(VerticalStairBlock.FACING).ordinal()][state.getValue(((VerticalStairBlock) state.getBlock()).shapeProperty()).ordinal()], VerticalStairBlock::getCopyProps);
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

	@SuppressWarnings("unchecked")
	public SlabAndStairModelsBuilder setSlab(SlabBlock block, String folder, String parentMod, String parentFolder)
	{
		slabDef.set(((ISlabBlock<VerticalSlabBlock>) block).getOtherBlock(), folder, parentMod, parentFolder);
		return this;
	}

	public SlabAndStairModelsBuilder setSlab(VerticalSlabBlock block, String folder)
	{
		return setSlab(block, folder, null, null);
	}

	public SlabAndStairModelsBuilder setSlab(SlabBlock block, String folder)
	{
		return setSlab(block, folder, null, null);
	}

	public SlabAndStairModelsBuilder setSlab(String folder, String parentMod, String parentFolder)
	{
		return setSlab((VerticalSlabBlock) null, folder, parentMod, parentFolder);
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

	@SuppressWarnings("unchecked")
	public SlabAndStairModelsBuilder setStairs(StairBlock block, String folder, String parentMod, String parentFolder)
	{
		stairDef.set(((IStairBlock<VerticalStairBlock>) block).getOtherBlock(), folder, parentMod, parentFolder);
		return this;
	}

	public SlabAndStairModelsBuilder setStairs(VerticalStairBlock block, String folder)
	{
		return setStairs(block, folder, null, null);
	}

	public SlabAndStairModelsBuilder setStairs(StairBlock block, String folder)
	{
		return setStairs(block, folder, null, null);
	}

	public SlabAndStairModelsBuilder setStairs(String folder, String parentMod, String parentFolder)
	{
		return setStairs((VerticalStairBlock) null, folder, parentMod, parentFolder);
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