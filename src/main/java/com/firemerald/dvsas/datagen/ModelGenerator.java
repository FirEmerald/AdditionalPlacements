package com.firemerald.dvsas.datagen;

import com.firemerald.dvsas.DVSaSMod;
import com.firemerald.dvsas.init.DVSaSBlocks;

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
		.setSlab(SlabAndStairModelsBuilder.SLAB_SIDE_ALL, DVSaSMod.MOD_ID, SlabAndStairModelsBuilder.SLAB_BASE).setStairs(SlabAndStairModelsBuilder.STAIRS_SIDE_ALL, DVSaSMod.MOD_ID, SlabAndStairModelsBuilder.STAIRS_BASE).addAction(model ->
		model
		.texture("side", "#all")
		.texture("top", "#all")
		.texture("bottom", "#all")).compile()
		.setSlab(SlabAndStairModelsBuilder.SLAB_COLUMN, DVSaSMod.MOD_ID, SlabAndStairModelsBuilder.SLAB_BASE).setStairs(SlabAndStairModelsBuilder.STAIRS_COLUMN, DVSaSMod.MOD_ID, SlabAndStairModelsBuilder.STAIRS_BASE).addAction(model ->
		model
		.texture("top", "#end")
		.texture("bottom", "#end")).compile()
		.setUVLock(true)
		.setHorizontalSlab(DVSaSBlocks.BEDROCK_SLAB, "block/slabs/bedrock").setVerticalSlab(DVSaSBlocks.VERTICAL_BEDROCK_SLAB, "block/slabs/bedrock").setHorizontalStairs(DVSaSBlocks.BEDROCK_STAIRS, "block/stairs/bedrock").setVerticalStairs(DVSaSBlocks.VERTICAL_BEDROCK_STAIRS, "block/stairs/bedrock").setAllSides(this.mcLoc("block/bedrock")).compile()
		.setVerticalSlab(DVSaSBlocks.CUT_COPPER_SLAB, "block/slabs/cut_copper").setVerticalStairs(DVSaSBlocks.CUT_COPPER_STAIRS, "block/stairs/cut_copper").setAllSides(this.mcLoc("block/cut_copper")).compile()
		.setVerticalSlab(DVSaSBlocks.EXPOSED_CUT_COPPER_SLAB, "block/slabs/exposed_cut_copper").setVerticalStairs(DVSaSBlocks.EXPOSED_CUT_COPPER_STAIRS, "block/stairs/exposed_cut_copper").setAllSides(this.mcLoc("block/exposed_cut_copper")).compile()
		.setVerticalSlab(DVSaSBlocks.WEATHERED_CUT_COPPER_SLAB, "block/slabs/weathered_cut_copper").setVerticalStairs(DVSaSBlocks.WEATHERED_CUT_COPPER_STAIRS, "block/stairs/weathered_cut_copper").setAllSides(this.mcLoc("block/weathered_cut_copper")).compile()
		.setVerticalSlab(DVSaSBlocks.OXIDIZED_CUT_COPPER_SLAB, "block/slabs/oxidized_cut_copper").setVerticalStairs(DVSaSBlocks.OXIDIZED_CUT_COPPER_STAIRS, "block/stairs/oxidized_cut_copper").setAllSides(this.mcLoc("block/oxidized_cut_copper")).compile()
		.setVerticalSlab(DVSaSBlocks.WAXED_CUT_COPPER_SLAB, "block/slabs/waxed_cut_copper").setVerticalStairs(DVSaSBlocks.WAXED_CUT_COPPER_STAIRS, "block/stairs/waxed_cut_copper").setAllSides(this.mcLoc("block/cut_copper")).compile()
		.setVerticalSlab(DVSaSBlocks.WAXED_EXPOSED_CUT_COPPER_SLAB, "block/slabs/waxed_exposed_cut_copper").setVerticalStairs(DVSaSBlocks.WAXED_EXPOSED_CUT_COPPER_STAIRS, "block/stairs/waxed_exposed_cut_copper").setAllSides(this.mcLoc("block/exposed_cut_copper")).compile()
		.setVerticalSlab(DVSaSBlocks.WAXED_WEATHERED_CUT_COPPER_SLAB, "block/slabs/waxed_weathered_cut_copper").setVerticalStairs(DVSaSBlocks.WAXED_WEATHERED_CUT_COPPER_STAIRS, "block/stairs/waxed_weathered_cut_copper").setAllSides(this.mcLoc("block/weathered_cut_copper")).compile()
		.setVerticalSlab(DVSaSBlocks.WAXED_OXIDIZED_CUT_COPPER_SLAB, "block/slabs/waxed_oxidized_cut_copper").setVerticalStairs(DVSaSBlocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS, "block/stairs/waxed_oxidized_cut_copper").setAllSides(this.mcLoc("block/oxidized_cut_copper")).compile()
		.setVerticalSlab(DVSaSBlocks.OAK_SLAB, "block/slabs/oak").setVerticalStairs(DVSaSBlocks.OAK_STAIRS, "block/stairs/oak").setAllSides(this.mcLoc("block/oak_planks")).compile()
		.setVerticalSlab(DVSaSBlocks.SPRUCE_SLAB, "block/slabs/spruce").setVerticalStairs(DVSaSBlocks.SPRUCE_STAIRS, "block/stairs/spruce").setAllSides(this.mcLoc("block/spruce_planks")).compile()
		.setVerticalSlab(DVSaSBlocks.BIRCH_SLAB, "block/slabs/birch").setVerticalStairs(DVSaSBlocks.BIRCH_STAIRS, "block/stairs/birch").setAllSides(this.mcLoc("block/birch_planks")).compile()
		.setVerticalSlab(DVSaSBlocks.JUNGLE_SLAB, "block/slabs/jungle").setVerticalStairs(DVSaSBlocks.JUNGLE_STAIRS, "block/stairs/jungle").setAllSides(this.mcLoc("block/jungle_planks")).compile()
		.setVerticalSlab(DVSaSBlocks.ACACIA_SLAB, "block/slabs/acacia").setVerticalStairs(DVSaSBlocks.ACACIA_STAIRS, "block/stairs/acacia").setAllSides(this.mcLoc("block/acacia_planks")).compile()
		.setVerticalSlab(DVSaSBlocks.DARK_OAK_SLAB, "block/slabs/dark_oak").setVerticalStairs(DVSaSBlocks.DARK_OAK_STAIRS, "block/stairs/dark_oak").setAllSides(this.mcLoc("block/dark_oak_planks")).compile()
		.setVerticalSlab(DVSaSBlocks.CRIMSON_SLAB, "block/slabs/crimson").setVerticalStairs(DVSaSBlocks.CRIMSON_STAIRS, "block/stairs/crimson").setAllSides(this.mcLoc("block/crimson_planks")).compile()
		.setVerticalSlab(DVSaSBlocks.WARPED_SLAB, "block/slabs/warped").setVerticalStairs(DVSaSBlocks.WARPED_STAIRS, "block/stairs/warped").setAllSides(this.mcLoc("block/warped_planks")).compile()
		.setVerticalSlab(DVSaSBlocks.PETRIFIED_OAK_SLAB, "block/slabs/petrified_oak").setAllSides(this.mcLoc("block/oak_planks")).compile()
		.setVerticalSlab(DVSaSBlocks.COBBLESTONE_SLAB, "block/slabs/cobblestone").setVerticalStairs(DVSaSBlocks.COBBLESTONE_STAIRS, "block/stairs/cobblestone").setAllSides(this.mcLoc("block/cobblestone")).compile()
		.setVerticalSlab(DVSaSBlocks.MOSSY_COBBLESTONE_SLAB, "block/slabs/mossy_cobblestone").setVerticalStairs(DVSaSBlocks.MOSSY_COBBLESTONE_STAIRS, "block/stairs/mossy_cobblestone").setAllSides(this.mcLoc("block/mossy_cobblestone")).compile()
		.setVerticalSlab(DVSaSBlocks.STONE_SLAB, "block/slabs/stone").setVerticalStairs(DVSaSBlocks.STONE_STAIRS, "block/stairs/stone").setAllSides(this.mcLoc("block/stone")).compile()
		.setVerticalSlab(DVSaSBlocks.SMOOTH_STONE_SLAB, "block/slabs/smooth_stone").setAllSides(this.mcLoc("block/smooth_stone")).compile()
		.setVerticalSlab(DVSaSBlocks.STONE_BRICK_SLAB, "block/slabs/stone_brick").setVerticalStairs(DVSaSBlocks.STONE_BRICK_STAIRS, "block/stairs/stone_brick").setAllSides(this.mcLoc("block/stone_bricks")).compile()
		.setVerticalSlab(DVSaSBlocks.MOSSY_STONE_BRICK_SLAB, "block/slabs/mossy_stone_brick").setVerticalStairs(DVSaSBlocks.MOSSY_STONE_BRICK_STAIRS, "block/stairs/mossy_stone_brick").setAllSides(this.mcLoc("block/mossy_stone_bricks")).compile()
		.setVerticalSlab(DVSaSBlocks.GRANITE_SLAB, "block/slabs/granite").setVerticalStairs(DVSaSBlocks.GRANITE_STAIRS, "block/stairs/granite").setAllSides(this.mcLoc("block/granite")).compile()
		.setVerticalSlab(DVSaSBlocks.POLISHED_GRANITE_SLAB, "block/slabs/polished_granite").setVerticalStairs(DVSaSBlocks.POLISHED_GRANITE_STAIRS, "block/stairs/polished_granite").setAllSides(this.mcLoc("block/polished_granite")).compile()
		.setVerticalSlab(DVSaSBlocks.DIORITE_SLAB, "block/slabs/diorite").setVerticalStairs(DVSaSBlocks.DIORITE_STAIRS, "block/stairs/diorite").setAllSides(this.mcLoc("block/diorite")).compile()
		.setVerticalSlab(DVSaSBlocks.POLISHED_DIORITE_SLAB, "block/slabs/polished_diorite").setVerticalStairs(DVSaSBlocks.POLISHED_DIORITE_STAIRS, "block/stairs/polished_diorite").setAllSides(this.mcLoc("block/polished_diorite")).compile()
		.setVerticalSlab(DVSaSBlocks.ANDESITE_SLAB, "block/slabs/andesite").setVerticalStairs(DVSaSBlocks.ANDESITE_STAIRS, "block/stairs/andesite").setAllSides(this.mcLoc("block/andesite")).compile()
		.setVerticalSlab(DVSaSBlocks.POLISHED_ANDESITE_SLAB, "block/slabs/polished_andesite").setVerticalStairs(DVSaSBlocks.POLISHED_ANDESITE_STAIRS, "block/stairs/polished_andesite").setAllSides(this.mcLoc("block/polished_andesite")).compile()
		.setVerticalSlab(DVSaSBlocks.COBBLED_DEEPSLATE_SLAB, "block/slabs/cobbled_deepslate").setVerticalStairs(DVSaSBlocks.COBBLED_DEEPSLATE_STAIRS, "block/stairs/cobbled_deepslate").setAllSides(this.mcLoc("block/cobbled_deepslate")).compile()
		.setVerticalSlab(DVSaSBlocks.POLISHED_DEEPSLATE_SLAB, "block/slabs/polished_deepslate").setVerticalStairs(DVSaSBlocks.POLISHED_DEEPSLATE_STAIRS, "block/stairs/polished_deepslate").setAllSides(this.mcLoc("block/polished_deepslate")).compile()
		.setVerticalSlab(DVSaSBlocks.DEEPSLATE_BRICK_SLAB, "block/slabs/deepslate_brick").setVerticalStairs(DVSaSBlocks.DEEPSLATE_BRICK_STAIRS, "block/stairs/deepslate_brick").setAllSides(this.mcLoc("block/deepslate_bricks")).compile()
		.setVerticalSlab(DVSaSBlocks.DEEPSLATE_TILE_SLAB, "block/slabs/deepslate_tile").setVerticalStairs(DVSaSBlocks.DEEPSLATE_TILE_STAIRS, "block/stairs/deepslate_tile").setAllSides(this.mcLoc("block/deepslate_tiles")).compile()
		.setVerticalSlab(DVSaSBlocks.BLACKSTONE_SLAB, "block/slabs/blackstone").setVerticalStairs(DVSaSBlocks.BLACKSTONE_STAIRS, "block/stairs/blackstone").setAllSides(this.mcLoc("block/blackstone")).compile()
		.setVerticalSlab(DVSaSBlocks.POLISHED_BLACKSTONE_SLAB, "block/slabs/polished_blackstone").setVerticalStairs(DVSaSBlocks.POLISHED_BLACKSTONE_STAIRS, "block/stairs/polished_blackstone").setAllSides(this.mcLoc("block/polished_blackstone")).compile()
		.setVerticalSlab(DVSaSBlocks.POLISHED_BLACKSTONE_BRICK_SLAB, "block/slabs/polished_blackstone_brick").setVerticalStairs(DVSaSBlocks.POLISHED_BLACKSTONE_BRICK_STAIRS, "block/stairs/polished_blackstone_brick").setAllSides(this.mcLoc("block/polished_blackstone_bricks")).compile()
		.setVerticalSlab(DVSaSBlocks.SANDSTONE_SLAB, "block/slabs/sandstone").setVerticalStairs(DVSaSBlocks.SANDSTONE_STAIRS, "block/stairs/sandstone").setPillar(this.mcLoc("block/sandstone"), this.mcLoc("block/sandstone_top"), this.mcLoc("block/sandstone_bottom")).compile()
		.setVerticalSlab(DVSaSBlocks.SMOOTH_SANDSTONE_SLAB, "block/slabs/smooth_sandstone").setVerticalStairs(DVSaSBlocks.SMOOTH_SANDSTONE_STAIRS, "block/stairs/smooth_sandstone").setAllSides(this.mcLoc("block/sandstone_top")).compile()
		.setVerticalSlab(DVSaSBlocks.CUT_SANDSTONE_SLAB, "block/slabs/cut_sandstone").setColumn(this.mcLoc("block/cut_sandstone"), this.mcLoc("block/sandstone_top")).compile()
		.setVerticalSlab(DVSaSBlocks.RED_SANDSTONE_SLAB, "block/slabs/red_sandstone").setVerticalStairs(DVSaSBlocks.RED_SANDSTONE_STAIRS, "block/stairs/red_sandstone").setPillar(this.mcLoc("block/red_sandstone"), this.mcLoc("block/red_sandstone_top"), this.mcLoc("block/red_sandstone_bottom")).compile()
		.setVerticalSlab(DVSaSBlocks.SMOOTH_RED_SANDSTONE_SLAB, "block/slabs/smooth_red_sandstone").setVerticalStairs(DVSaSBlocks.SMOOTH_RED_SANDSTONE_STAIRS, "block/stairs/smooth_red_sandstone").setAllSides(this.mcLoc("block/red_sandstone_top")).compile()
		.setVerticalSlab(DVSaSBlocks.CUT_RED_SANDSTONE_SLAB, "block/slabs/cut_red_sandstone").setColumn(this.mcLoc("block/cut_red_sandstone"), this.mcLoc("block/red_sandstone_top")).compile()
		.setVerticalSlab(DVSaSBlocks.BRICK_SLAB, "block/slabs/brick").setVerticalStairs(DVSaSBlocks.BRICK_STAIRS, "block/stairs/brick").setAllSides(this.mcLoc("block/bricks")).compile()
		.setVerticalSlab(DVSaSBlocks.NETHER_BRICK_SLAB, "block/slabs/nether_brick").setVerticalStairs(DVSaSBlocks.NETHER_BRICK_STAIRS, "block/stairs/nether_brick").setAllSides(this.mcLoc("block/nether_bricks")).compile()
		.setVerticalSlab(DVSaSBlocks.RED_NETHER_BRICK_SLAB, "block/slabs/red_nether_brick").setVerticalStairs(DVSaSBlocks.RED_NETHER_BRICK_STAIRS, "block/stairs/red_nether_brick").setAllSides(this.mcLoc("block/red_nether_bricks")).compile()
		.setVerticalSlab(DVSaSBlocks.QUARTZ_SLAB, "block/slabs/quartz").setVerticalStairs(DVSaSBlocks.QUARTZ_STAIRS, "block/stairs/quartz").setColumn(this.mcLoc("block/quartz_block_side"), this.mcLoc("block/quartz_block_top")).compile()
		.setVerticalSlab(DVSaSBlocks.SMOOTH_QUARTZ_SLAB, "block/slabs/smooth_quartz").setVerticalStairs(DVSaSBlocks.SMOOTH_QUARTZ_STAIRS, "block/stairs/smooth_quartz").setAllSides(this.mcLoc("block/quartz_block_bottom")).compile()
		.setVerticalSlab(DVSaSBlocks.PRISMARINE_SLAB, "block/slabs/prismarine").setVerticalStairs(DVSaSBlocks.PRISMARINE_STAIRS, "block/stairs/prismarine").setAllSides(this.mcLoc("block/prismarine")).compile()
		.setVerticalSlab(DVSaSBlocks.PRISMARINE_BRICK_SLAB, "block/slabs/prismarine_brick").setVerticalStairs(DVSaSBlocks.PRISMARINE_BRICK_STAIRS, "block/stairs/prismarine_brick").setAllSides(this.mcLoc("block/prismarine_bricks")).compile()
		.setVerticalSlab(DVSaSBlocks.DARK_PRISMARINE_SLAB, "block/slabs/dark_prismarine").setVerticalStairs(DVSaSBlocks.DARK_PRISMARINE_STAIRS, "block/stairs/dark_prismarine").setAllSides(this.mcLoc("block/dark_prismarine")).compile()
		.setVerticalSlab(DVSaSBlocks.END_STONE_BRICK_SLAB, "block/slabs/end_stone_brick").setVerticalStairs(DVSaSBlocks.END_STONE_BRICK_STAIRS, "block/stairs/end_stone_brick").setAllSides(this.mcLoc("block/end_stone_bricks")).compile()
		.setVerticalSlab(DVSaSBlocks.PURPUR_SLAB, "block/slabs/purpur").setVerticalStairs(DVSaSBlocks.PURPUR_STAIRS, "block/stairs/purpur").setAllSides(this.mcLoc("block/purpur_block")).compile();
	}
}