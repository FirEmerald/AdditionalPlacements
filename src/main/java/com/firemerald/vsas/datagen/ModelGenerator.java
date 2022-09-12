package com.firemerald.vsas.datagen;

import com.firemerald.vsas.VSASMod;
import com.firemerald.vsas.init.VSASBlocks;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModelGenerator extends BlockStateProvider
{
	public ModelGenerator(DataGenerator gen, String modid, ExistingFileHelper exFileHelper)
	{
		super(gen, modid, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels()
	{
		new SlabAndStairModelsBuilder(this)
		.setSlab(SlabAndStairModelsBuilder.SLAB_SIDE_ALL, VSASMod.MOD_ID, SlabAndStairModelsBuilder.SLAB_BASE).setStairs(SlabAndStairModelsBuilder.STAIRS_SIDE_ALL, VSASMod.MOD_ID, SlabAndStairModelsBuilder.STAIRS_BASE).addAction(model ->
		model
		.texture("side", "#all")
		.texture("top", "#all")
		.texture("bottom", "#all")).compile()
		.setSlab(SlabAndStairModelsBuilder.SLAB_COLUMN, VSASMod.MOD_ID, SlabAndStairModelsBuilder.SLAB_BASE).setStairs(SlabAndStairModelsBuilder.STAIRS_COLUMN, VSASMod.MOD_ID, SlabAndStairModelsBuilder.STAIRS_BASE).addAction(model ->
		model
		.texture("top", "#end")
		.texture("bottom", "#end")).compile()
		.setUVLock(true)
		.setHorizontalSlab(VSASBlocks.BEDROCK_SLAB, "block/slabs/bedrock").setVerticalSlab(VSASBlocks.VERTICAL_BEDROCK_SLAB, "block/slabs/bedrock").setHorizontalStairs(VSASBlocks.BEDROCK_STAIRS, "block/stairs/bedrock").setVerticalStairs(VSASBlocks.VERTICAL_BEDROCK_STAIRS, "block/stairs/bedrock").setAllSides(this.mcLoc("block/bedrock")).compile()
		.setVerticalSlab(VSASBlocks.CUT_COPPER_SLAB, "block/slabs/cut_copper").setVerticalStairs(VSASBlocks.CUT_COPPER_STAIRS, "block/stairs/cut_copper").setAllSides(this.mcLoc("block/cut_copper")).compile()
		.setVerticalSlab(VSASBlocks.EXPOSED_CUT_COPPER_SLAB, "block/slabs/exposed_cut_copper").setVerticalStairs(VSASBlocks.EXPOSED_CUT_COPPER_STAIRS, "block/stairs/exposed_cut_copper").setAllSides(this.mcLoc("block/exposed_cut_copper")).compile()
		.setVerticalSlab(VSASBlocks.WEATHERED_CUT_COPPER_SLAB, "block/slabs/weathered_cut_copper").setVerticalStairs(VSASBlocks.WEATHERED_CUT_COPPER_STAIRS, "block/stairs/weathered_cut_copper").setAllSides(this.mcLoc("block/weathered_cut_copper")).compile()
		.setVerticalSlab(VSASBlocks.OXIDIZED_CUT_COPPER_SLAB, "block/slabs/oxidized_cut_copper").setVerticalStairs(VSASBlocks.OXIDIZED_CUT_COPPER_STAIRS, "block/stairs/oxidized_cut_copper").setAllSides(this.mcLoc("block/oxidized_cut_copper")).compile()
		.setVerticalSlab(VSASBlocks.WAXED_CUT_COPPER_SLAB, "block/slabs/waxed_cut_copper").setVerticalStairs(VSASBlocks.WAXED_CUT_COPPER_STAIRS, "block/stairs/waxed_cut_copper").setAllSides(this.mcLoc("block/cut_copper")).compile()
		.setVerticalSlab(VSASBlocks.WAXED_EXPOSED_CUT_COPPER_SLAB, "block/slabs/waxed_exposed_cut_copper").setVerticalStairs(VSASBlocks.WAXED_EXPOSED_CUT_COPPER_STAIRS, "block/stairs/waxed_exposed_cut_copper").setAllSides(this.mcLoc("block/exposed_cut_copper")).compile()
		.setVerticalSlab(VSASBlocks.WAXED_WEATHERED_CUT_COPPER_SLAB, "block/slabs/waxed_weathered_cut_copper").setVerticalStairs(VSASBlocks.WAXED_WEATHERED_CUT_COPPER_STAIRS, "block/stairs/waxed_weathered_cut_copper").setAllSides(this.mcLoc("block/weathered_cut_copper")).compile()
		.setVerticalSlab(VSASBlocks.WAXED_OXIDIZED_CUT_COPPER_SLAB, "block/slabs/waxed_oxidized_cut_copper").setVerticalStairs(VSASBlocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS, "block/stairs/waxed_oxidized_cut_copper").setAllSides(this.mcLoc("block/oxidized_cut_copper")).compile()
		.setVerticalSlab(VSASBlocks.OAK_SLAB, "block/slabs/oak").setVerticalStairs(VSASBlocks.OAK_STAIRS, "block/stairs/oak").setAllSides(this.mcLoc("block/oak_planks")).compile()
		.setVerticalSlab(VSASBlocks.SPRUCE_SLAB, "block/slabs/spruce").setVerticalStairs(VSASBlocks.SPRUCE_STAIRS, "block/stairs/spruce").setAllSides(this.mcLoc("block/spruce_planks")).compile()
		.setVerticalSlab(VSASBlocks.BIRCH_SLAB, "block/slabs/birch").setVerticalStairs(VSASBlocks.BIRCH_STAIRS, "block/stairs/birch").setAllSides(this.mcLoc("block/birch_planks")).compile()
		.setVerticalSlab(VSASBlocks.JUNGLE_SLAB, "block/slabs/jungle").setVerticalStairs(VSASBlocks.JUNGLE_STAIRS, "block/stairs/jungle").setAllSides(this.mcLoc("block/jungle_planks")).compile()
		.setVerticalSlab(VSASBlocks.ACACIA_SLAB, "block/slabs/acacia").setVerticalStairs(VSASBlocks.ACACIA_STAIRS, "block/stairs/acacia").setAllSides(this.mcLoc("block/acacia_planks")).compile()
		.setVerticalSlab(VSASBlocks.DARK_OAK_SLAB, "block/slabs/dark_oak").setVerticalStairs(VSASBlocks.DARK_OAK_STAIRS, "block/stairs/dark_oak").setAllSides(this.mcLoc("block/dark_oak_planks")).compile()
		.setVerticalSlab(VSASBlocks.CRIMSON_SLAB, "block/slabs/crimson").setVerticalStairs(VSASBlocks.CRIMSON_STAIRS, "block/stairs/crimson").setAllSides(this.mcLoc("block/crimson_planks")).compile()
		.setVerticalSlab(VSASBlocks.WARPED_SLAB, "block/slabs/warped").setVerticalStairs(VSASBlocks.WARPED_STAIRS, "block/stairs/warped").setAllSides(this.mcLoc("block/warped_planks")).compile()
		.setVerticalSlab(VSASBlocks.PETRIFIED_OAK_SLAB, "block/slabs/petrified_oak").setAllSides(this.mcLoc("block/oak_planks")).compile()
		.setVerticalSlab(VSASBlocks.COBBLESTONE_SLAB, "block/slabs/cobblestone").setVerticalStairs(VSASBlocks.COBBLESTONE_STAIRS, "block/stairs/cobblestone").setAllSides(this.mcLoc("block/cobblestone")).compile()
		.setVerticalSlab(VSASBlocks.MOSSY_COBBLESTONE_SLAB, "block/slabs/mossy_cobblestone").setVerticalStairs(VSASBlocks.MOSSY_COBBLESTONE_STAIRS, "block/stairs/mossy_cobblestone").setAllSides(this.mcLoc("block/mossy_cobblestone")).compile()
		.setVerticalSlab(VSASBlocks.STONE_SLAB, "block/slabs/stone").setVerticalStairs(VSASBlocks.STONE_STAIRS, "block/stairs/stone").setAllSides(this.mcLoc("block/stone")).compile()
		.setVerticalSlab(VSASBlocks.SMOOTH_STONE_SLAB, "block/slabs/smooth_stone").setAllSides(this.mcLoc("block/smooth_stone")).compile()
		.setVerticalSlab(VSASBlocks.STONE_BRICK_SLAB, "block/slabs/stone_brick").setVerticalStairs(VSASBlocks.STONE_BRICK_STAIRS, "block/stairs/stone_brick").setAllSides(this.mcLoc("block/stone_bricks")).compile()
		.setVerticalSlab(VSASBlocks.MOSSY_STONE_BRICK_SLAB, "block/slabs/mossy_stone_brick").setVerticalStairs(VSASBlocks.MOSSY_STONE_BRICK_STAIRS, "block/stairs/mossy_stone_brick").setAllSides(this.mcLoc("block/mossy_stone_bricks")).compile()
		.setVerticalSlab(VSASBlocks.GRANITE_SLAB, "block/slabs/granite").setVerticalStairs(VSASBlocks.GRANITE_STAIRS, "block/stairs/granite").setAllSides(this.mcLoc("block/granite")).compile()
		.setVerticalSlab(VSASBlocks.POLISHED_GRANITE_SLAB, "block/slabs/polished_granite").setVerticalStairs(VSASBlocks.POLISHED_GRANITE_STAIRS, "block/stairs/polished_granite").setAllSides(this.mcLoc("block/polished_granite")).compile()
		.setVerticalSlab(VSASBlocks.DIORITE_SLAB, "block/slabs/diorite").setVerticalStairs(VSASBlocks.DIORITE_STAIRS, "block/stairs/diorite").setAllSides(this.mcLoc("block/diorite")).compile()
		.setVerticalSlab(VSASBlocks.POLISHED_DIORITE_SLAB, "block/slabs/polished_diorite").setVerticalStairs(VSASBlocks.POLISHED_DIORITE_STAIRS, "block/stairs/polished_diorite").setAllSides(this.mcLoc("block/polished_diorite")).compile()
		.setVerticalSlab(VSASBlocks.ANDESITE_SLAB, "block/slabs/andesite").setVerticalStairs(VSASBlocks.ANDESITE_STAIRS, "block/stairs/andesite").setAllSides(this.mcLoc("block/andesite")).compile()
		.setVerticalSlab(VSASBlocks.POLISHED_ANDESITE_SLAB, "block/slabs/polished_andesite").setVerticalStairs(VSASBlocks.POLISHED_ANDESITE_STAIRS, "block/stairs/polished_andesite").setAllSides(this.mcLoc("block/polished_andesite")).compile()
		.setVerticalSlab(VSASBlocks.COBBLED_DEEPSLATE_SLAB, "block/slabs/cobbled_deepslate").setVerticalStairs(VSASBlocks.COBBLED_DEEPSLATE_STAIRS, "block/stairs/cobbled_deepslate").setAllSides(this.mcLoc("block/cobbled_deepslate")).compile()
		.setVerticalSlab(VSASBlocks.POLISHED_DEEPSLATE_SLAB, "block/slabs/polished_deepslate").setVerticalStairs(VSASBlocks.POLISHED_DEEPSLATE_STAIRS, "block/stairs/polished_deepslate").setAllSides(this.mcLoc("block/polished_deepslate")).compile()
		.setVerticalSlab(VSASBlocks.DEEPSLATE_BRICK_SLAB, "block/slabs/deepslate_brick").setVerticalStairs(VSASBlocks.DEEPSLATE_BRICK_STAIRS, "block/stairs/deepslate_brick").setAllSides(this.mcLoc("block/deepslate_bricks")).compile()
		.setVerticalSlab(VSASBlocks.DEEPSLATE_TILE_SLAB, "block/slabs/deepslate_tile").setVerticalStairs(VSASBlocks.DEEPSLATE_TILE_STAIRS, "block/stairs/deepslate_tile").setAllSides(this.mcLoc("block/deepslate_tiles")).compile()
		.setVerticalSlab(VSASBlocks.BLACKSTONE_SLAB, "block/slabs/blackstone").setVerticalStairs(VSASBlocks.BLACKSTONE_STAIRS, "block/stairs/blackstone").setAllSides(this.mcLoc("block/blackstone")).compile()
		.setVerticalSlab(VSASBlocks.POLISHED_BLACKSTONE_SLAB, "block/slabs/polished_blackstone").setVerticalStairs(VSASBlocks.POLISHED_BLACKSTONE_STAIRS, "block/stairs/polished_blackstone").setAllSides(this.mcLoc("block/polished_blackstone")).compile()
		.setVerticalSlab(VSASBlocks.POLISHED_BLACKSTONE_BRICK_SLAB, "block/slabs/polished_blackstone_brick").setVerticalStairs(VSASBlocks.POLISHED_BLACKSTONE_BRICK_STAIRS, "block/stairs/polished_blackstone_brick").setAllSides(this.mcLoc("block/polished_blackstone_bricks")).compile()
		.setVerticalSlab(VSASBlocks.SANDSTONE_SLAB, "block/slabs/sandstone").setVerticalStairs(VSASBlocks.SANDSTONE_STAIRS, "block/stairs/sandstone").setPillar(this.mcLoc("block/sandstone"), this.mcLoc("block/sandstone_top"), this.mcLoc("block/sandstone_bottom")).compile()
		.setVerticalSlab(VSASBlocks.SMOOTH_SANDSTONE_SLAB, "block/slabs/smooth_sandstone").setVerticalStairs(VSASBlocks.SMOOTH_SANDSTONE_STAIRS, "block/stairs/smooth_sandstone").setAllSides(this.mcLoc("block/sandstone_top")).compile()
		.setVerticalSlab(VSASBlocks.CUT_SANDSTONE_SLAB, "block/slabs/cut_sandstone").setColumn(this.mcLoc("block/cut_sandstone"), this.mcLoc("block/sandstone_top")).compile()
		.setVerticalSlab(VSASBlocks.RED_SANDSTONE_SLAB, "block/slabs/red_sandstone").setVerticalStairs(VSASBlocks.RED_SANDSTONE_STAIRS, "block/stairs/red_sandstone").setPillar(this.mcLoc("block/red_sandstone"), this.mcLoc("block/red_sandstone_top"), this.mcLoc("block/red_sandstone_bottom")).compile()
		.setVerticalSlab(VSASBlocks.SMOOTH_RED_SANDSTONE_SLAB, "block/slabs/smooth_red_sandstone").setVerticalStairs(VSASBlocks.SMOOTH_RED_SANDSTONE_STAIRS, "block/stairs/smooth_red_sandstone").setAllSides(this.mcLoc("block/red_sandstone_top")).compile()
		.setVerticalSlab(VSASBlocks.CUT_RED_SANDSTONE_SLAB, "block/slabs/cut_red_sandstone").setColumn(this.mcLoc("block/cut_red_sandstone"), this.mcLoc("block/red_sandstone_top")).compile()
		.setVerticalSlab(VSASBlocks.BRICK_SLAB, "block/slabs/brick").setVerticalStairs(VSASBlocks.BRICK_STAIRS, "block/stairs/brick").setAllSides(this.mcLoc("block/bricks")).compile()
		.setVerticalSlab(VSASBlocks.NETHER_BRICK_SLAB, "block/slabs/nether_brick").setVerticalStairs(VSASBlocks.NETHER_BRICK_STAIRS, "block/stairs/nether_brick").setAllSides(this.mcLoc("block/nether_bricks")).compile()
		.setVerticalSlab(VSASBlocks.RED_NETHER_BRICK_SLAB, "block/slabs/red_nether_brick").setVerticalStairs(VSASBlocks.RED_NETHER_BRICK_STAIRS, "block/stairs/red_nether_brick").setAllSides(this.mcLoc("block/red_nether_bricks")).compile()
		.setVerticalSlab(VSASBlocks.QUARTZ_SLAB, "block/slabs/quartz").setVerticalStairs(VSASBlocks.QUARTZ_STAIRS, "block/stairs/quartz").setColumn(this.mcLoc("block/quartz_block_side"), this.mcLoc("block/quartz_block_top")).compile()
		.setVerticalSlab(VSASBlocks.SMOOTH_QUARTZ_SLAB, "block/slabs/smooth_quartz").setVerticalStairs(VSASBlocks.SMOOTH_QUARTZ_STAIRS, "block/stairs/smooth_quartz").setAllSides(this.mcLoc("block/quartz_block_bottom")).compile()
		.setVerticalSlab(VSASBlocks.PRISMARINE_SLAB, "block/slabs/prismarine").setVerticalStairs(VSASBlocks.PRISMARINE_STAIRS, "block/stairs/prismarine").setAllSides(this.mcLoc("block/prismarine")).compile()
		.setVerticalSlab(VSASBlocks.PRISMARINE_BRICK_SLAB, "block/slabs/prismarine_brick").setVerticalStairs(VSASBlocks.PRISMARINE_BRICK_STAIRS, "block/stairs/prismarine_brick").setAllSides(this.mcLoc("block/prismarine_bricks")).compile()
		.setVerticalSlab(VSASBlocks.DARK_PRISMARINE_SLAB, "block/slabs/dark_prismarine").setVerticalStairs(VSASBlocks.DARK_PRISMARINE_STAIRS, "block/stairs/dark_prismarine").setAllSides(this.mcLoc("block/dark_prismarine")).compile()
		.setVerticalSlab(VSASBlocks.END_STONE_BRICK_SLAB, "block/slabs/end_stone_brick").setVerticalStairs(VSASBlocks.END_STONE_BRICK_STAIRS, "block/stairs/end_stone_brick").setAllSides(this.mcLoc("block/end_stone_bricks")).compile()
		.setVerticalSlab(VSASBlocks.PURPUR_SLAB, "block/slabs/purpur").setVerticalStairs(VSASBlocks.PURPUR_STAIRS, "block/stairs/purpur").setAllSides(this.mcLoc("block/purpur_block")).compile();
	}
}