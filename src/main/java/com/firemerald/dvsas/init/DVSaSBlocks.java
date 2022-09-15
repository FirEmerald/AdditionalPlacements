package com.firemerald.dvsas.init;

import java.util.function.Supplier;

import com.firemerald.dvsas.DVSaSMod;
import com.firemerald.dvsas.block.VerticalSlabBlock;
import com.firemerald.dvsas.block.VerticalStairBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegistryObject;

@ObjectHolder(DVSaSMod.MOD_ID)
public class DVSaSBlocks
{
	@ObjectHolder(RegistryNames.BEDROCK_SLAB)
	public static final SlabBlock BEDROCK_SLAB = null;
	@ObjectHolder(RegistryNames.BEDROCK_STAIRS)
	public static final StairBlock BEDROCK_STAIRS = null;
	@ObjectHolder(RegistryNames.VERTICAL_BEDROCK_SLAB)
	public static final VerticalSlabBlock VERTICAL_BEDROCK_SLAB = null;
	@ObjectHolder(RegistryNames.VERTICAL_BEDROCK_STAIRS)
	public static final VerticalStairBlock VERTICAL_BEDROCK_STAIRS = null;
	
	@ObjectHolder(RegistryNames.CUT_COPPER_SLAB)
	public static final VerticalSlabBlock CUT_COPPER_SLAB = null;
	@ObjectHolder(RegistryNames.CUT_COPPER_STAIRS)
	public static final VerticalStairBlock CUT_COPPER_STAIRS = null;
	@ObjectHolder(RegistryNames.EXPOSED_CUT_COPPER_SLAB)
	public static final VerticalSlabBlock EXPOSED_CUT_COPPER_SLAB = null;
	@ObjectHolder(RegistryNames.EXPOSED_CUT_COPPER_STAIRS)
	public static final VerticalStairBlock EXPOSED_CUT_COPPER_STAIRS = null;
	@ObjectHolder(RegistryNames.WEATHERED_CUT_COPPER_SLAB)
	public static final VerticalSlabBlock WEATHERED_CUT_COPPER_SLAB = null;
	@ObjectHolder(RegistryNames.WEATHERED_CUT_COPPER_STAIRS)
	public static final VerticalStairBlock WEATHERED_CUT_COPPER_STAIRS = null;
	@ObjectHolder(RegistryNames.OXIDIZED_CUT_COPPER_SLAB)
	public static final VerticalSlabBlock OXIDIZED_CUT_COPPER_SLAB = null;
	@ObjectHolder(RegistryNames.OXIDIZED_CUT_COPPER_STAIRS)
	public static final VerticalStairBlock OXIDIZED_CUT_COPPER_STAIRS = null;
	@ObjectHolder(RegistryNames.WAXED_CUT_COPPER_SLAB)
	public static final VerticalSlabBlock WAXED_CUT_COPPER_SLAB = null;
	@ObjectHolder(RegistryNames.WAXED_CUT_COPPER_STAIRS)
	public static final VerticalStairBlock WAXED_CUT_COPPER_STAIRS = null;
	@ObjectHolder(RegistryNames.WAXED_EXPOSED_CUT_COPPER_SLAB)
	public static final VerticalSlabBlock WAXED_EXPOSED_CUT_COPPER_SLAB = null;
	@ObjectHolder(RegistryNames.WAXED_EXPOSED_CUT_COPPER_STAIRS)
	public static final VerticalStairBlock WAXED_EXPOSED_CUT_COPPER_STAIRS = null;
	@ObjectHolder(RegistryNames.WAXED_WEATHERED_CUT_COPPER_SLAB)
	public static final VerticalSlabBlock WAXED_WEATHERED_CUT_COPPER_SLAB = null;
	@ObjectHolder(RegistryNames.WAXED_WEATHERED_CUT_COPPER_STAIRS)
	public static final VerticalStairBlock WAXED_WEATHERED_CUT_COPPER_STAIRS = null;
	@ObjectHolder(RegistryNames.WAXED_OXIDIZED_CUT_COPPER_SLAB)
	public static final VerticalSlabBlock WAXED_OXIDIZED_CUT_COPPER_SLAB = null;
	@ObjectHolder(RegistryNames.WAXED_OXIDIZED_CUT_COPPER_STAIRS)
	public static final VerticalStairBlock WAXED_OXIDIZED_CUT_COPPER_STAIRS = null;
	@ObjectHolder(RegistryNames.OAK_SLAB)
	public static final VerticalSlabBlock OAK_SLAB = null;
	@ObjectHolder(RegistryNames.OAK_STAIRS)
	public static final VerticalStairBlock OAK_STAIRS = null;
	@ObjectHolder(RegistryNames.SPRUCE_SLAB)
	public static final VerticalSlabBlock SPRUCE_SLAB = null;
	@ObjectHolder(RegistryNames.SPRUCE_STAIRS)
	public static final VerticalStairBlock SPRUCE_STAIRS = null;
	@ObjectHolder(RegistryNames.BIRCH_SLAB)
	public static final VerticalSlabBlock BIRCH_SLAB = null;
	@ObjectHolder(RegistryNames.BIRCH_STAIRS)
	public static final VerticalStairBlock BIRCH_STAIRS = null;
	@ObjectHolder(RegistryNames.JUNGLE_SLAB)
	public static final VerticalSlabBlock JUNGLE_SLAB = null;
	@ObjectHolder(RegistryNames.JUNGLE_STAIRS)
	public static final VerticalStairBlock JUNGLE_STAIRS = null;
	@ObjectHolder(RegistryNames.ACACIA_SLAB)
	public static final VerticalSlabBlock ACACIA_SLAB = null;
	@ObjectHolder(RegistryNames.ACACIA_STAIRS)
	public static final VerticalStairBlock ACACIA_STAIRS = null;
	@ObjectHolder(RegistryNames.DARK_OAK_SLAB)
	public static final VerticalSlabBlock DARK_OAK_SLAB = null;
	@ObjectHolder(RegistryNames.DARK_OAK_STAIRS)
	public static final VerticalStairBlock DARK_OAK_STAIRS = null;
	@ObjectHolder(RegistryNames.CRIMSON_SLAB)
	public static final VerticalSlabBlock CRIMSON_SLAB = null;
	@ObjectHolder(RegistryNames.CRIMSON_STAIRS)
	public static final VerticalStairBlock CRIMSON_STAIRS = null;
	@ObjectHolder(RegistryNames.WARPED_SLAB)
	public static final VerticalSlabBlock WARPED_SLAB = null;
	@ObjectHolder(RegistryNames.WARPED_STAIRS)
	public static final VerticalStairBlock WARPED_STAIRS = null;
	@ObjectHolder(RegistryNames.PETRIFIED_OAK_SLAB)
	public static final VerticalSlabBlock PETRIFIED_OAK_SLAB = null;
	@ObjectHolder(RegistryNames.COBBLESTONE_SLAB)
	public static final VerticalSlabBlock COBBLESTONE_SLAB = null;
	@ObjectHolder(RegistryNames.COBBLESTONE_STAIRS)
	public static final VerticalStairBlock COBBLESTONE_STAIRS = null;
	@ObjectHolder(RegistryNames.MOSSY_COBBLESTONE_SLAB)
	public static final VerticalSlabBlock MOSSY_COBBLESTONE_SLAB = null;
	@ObjectHolder(RegistryNames.MOSSY_COBBLESTONE_STAIRS)
	public static final VerticalStairBlock MOSSY_COBBLESTONE_STAIRS = null;
	@ObjectHolder(RegistryNames.STONE_SLAB)
	public static final VerticalSlabBlock STONE_SLAB = null;
	@ObjectHolder(RegistryNames.STONE_STAIRS)
	public static final VerticalStairBlock STONE_STAIRS = null;
	@ObjectHolder(RegistryNames.SMOOTH_STONE_SLAB)
	public static final VerticalSlabBlock SMOOTH_STONE_SLAB = null;
	@ObjectHolder(RegistryNames.STONE_BRICK_SLAB)
	public static final VerticalSlabBlock STONE_BRICK_SLAB = null;
	@ObjectHolder(RegistryNames.STONE_BRICK_STAIRS)
	public static final VerticalStairBlock STONE_BRICK_STAIRS = null;
	@ObjectHolder(RegistryNames.MOSSY_STONE_BRICK_SLAB)
	public static final VerticalSlabBlock MOSSY_STONE_BRICK_SLAB = null;
	@ObjectHolder(RegistryNames.MOSSY_STONE_BRICK_STAIRS)
	public static final VerticalStairBlock MOSSY_STONE_BRICK_STAIRS = null;
	@ObjectHolder(RegistryNames.GRANITE_SLAB)
	public static final VerticalSlabBlock GRANITE_SLAB = null;
	@ObjectHolder(RegistryNames.GRANITE_STAIRS)
	public static final VerticalStairBlock GRANITE_STAIRS = null;
	@ObjectHolder(RegistryNames.POLISHED_GRANITE_SLAB)
	public static final VerticalSlabBlock POLISHED_GRANITE_SLAB = null;
	@ObjectHolder(RegistryNames.POLISHED_GRANITE_STAIRS)
	public static final VerticalStairBlock POLISHED_GRANITE_STAIRS = null;
	@ObjectHolder(RegistryNames.DIORITE_SLAB)
	public static final VerticalSlabBlock DIORITE_SLAB = null;
	@ObjectHolder(RegistryNames.DIORITE_STAIRS)
	public static final VerticalStairBlock DIORITE_STAIRS = null;
	@ObjectHolder(RegistryNames.POLISHED_DIORITE_SLAB)
	public static final VerticalSlabBlock POLISHED_DIORITE_SLAB = null;
	@ObjectHolder(RegistryNames.POLISHED_DIORITE_STAIRS)
	public static final VerticalStairBlock POLISHED_DIORITE_STAIRS = null;
	@ObjectHolder(RegistryNames.ANDESITE_SLAB)
	public static final VerticalSlabBlock ANDESITE_SLAB = null;
	@ObjectHolder(RegistryNames.ANDESITE_STAIRS)
	public static final VerticalStairBlock ANDESITE_STAIRS = null;
	@ObjectHolder(RegistryNames.POLISHED_ANDESITE_SLAB)
	public static final VerticalSlabBlock POLISHED_ANDESITE_SLAB = null;
	@ObjectHolder(RegistryNames.POLISHED_ANDESITE_STAIRS)
	public static final VerticalStairBlock POLISHED_ANDESITE_STAIRS = null;
	@ObjectHolder(RegistryNames.COBBLED_DEEPSLATE_SLAB)
	public static final VerticalSlabBlock COBBLED_DEEPSLATE_SLAB = null;
	@ObjectHolder(RegistryNames.COBBLED_DEEPSLATE_STAIRS)
	public static final VerticalStairBlock COBBLED_DEEPSLATE_STAIRS = null;
	@ObjectHolder(RegistryNames.POLISHED_DEEPSLATE_SLAB)
	public static final VerticalSlabBlock POLISHED_DEEPSLATE_SLAB = null;
	@ObjectHolder(RegistryNames.POLISHED_DEEPSLATE_STAIRS)
	public static final VerticalStairBlock POLISHED_DEEPSLATE_STAIRS = null;
	@ObjectHolder(RegistryNames.DEEPSLATE_BRICK_SLAB)
	public static final VerticalSlabBlock DEEPSLATE_BRICK_SLAB = null;
	@ObjectHolder(RegistryNames.DEEPSLATE_BRICK_STAIRS)
	public static final VerticalStairBlock DEEPSLATE_BRICK_STAIRS = null;
	@ObjectHolder(RegistryNames.DEEPSLATE_TILE_SLAB)
	public static final VerticalSlabBlock DEEPSLATE_TILE_SLAB = null;
	@ObjectHolder(RegistryNames.DEEPSLATE_TILE_STAIRS)
	public static final VerticalStairBlock DEEPSLATE_TILE_STAIRS = null;
	@ObjectHolder(RegistryNames.BLACKSTONE_SLAB)
	public static final VerticalSlabBlock BLACKSTONE_SLAB = null;
	@ObjectHolder(RegistryNames.BLACKSTONE_STAIRS)
	public static final VerticalStairBlock BLACKSTONE_STAIRS = null;
	@ObjectHolder(RegistryNames.POLISHED_BLACKSTONE_SLAB)
	public static final VerticalSlabBlock POLISHED_BLACKSTONE_SLAB = null;
	@ObjectHolder(RegistryNames.POLISHED_BLACKSTONE_STAIRS)
	public static final VerticalStairBlock POLISHED_BLACKSTONE_STAIRS = null;
	@ObjectHolder(RegistryNames.POLISHED_BLACKSTONE_BRICK_SLAB)
	public static final VerticalSlabBlock POLISHED_BLACKSTONE_BRICK_SLAB = null;
	@ObjectHolder(RegistryNames.POLISHED_BLACKSTONE_BRICK_STAIRS)
	public static final VerticalStairBlock POLISHED_BLACKSTONE_BRICK_STAIRS = null;
	@ObjectHolder(RegistryNames.SANDSTONE_SLAB)
	public static final VerticalSlabBlock SANDSTONE_SLAB = null;
	@ObjectHolder(RegistryNames.SANDSTONE_STAIRS)
	public static final VerticalStairBlock SANDSTONE_STAIRS = null;
	@ObjectHolder(RegistryNames.SMOOTH_SANDSTONE_SLAB)
	public static final VerticalSlabBlock SMOOTH_SANDSTONE_SLAB = null;
	@ObjectHolder(RegistryNames.SMOOTH_SANDSTONE_STAIRS)
	public static final VerticalStairBlock SMOOTH_SANDSTONE_STAIRS = null;
	@ObjectHolder(RegistryNames.CUT_SANDSTONE_SLAB)
	public static final VerticalSlabBlock CUT_SANDSTONE_SLAB = null;
	@ObjectHolder(RegistryNames.RED_SANDSTONE_SLAB)
	public static final VerticalSlabBlock RED_SANDSTONE_SLAB = null;
	@ObjectHolder(RegistryNames.RED_SANDSTONE_STAIRS)
	public static final VerticalStairBlock RED_SANDSTONE_STAIRS = null;
	@ObjectHolder(RegistryNames.SMOOTH_RED_SANDSTONE_SLAB)
	public static final VerticalSlabBlock SMOOTH_RED_SANDSTONE_SLAB = null;
	@ObjectHolder(RegistryNames.SMOOTH_RED_SANDSTONE_STAIRS)
	public static final VerticalStairBlock SMOOTH_RED_SANDSTONE_STAIRS = null;
	@ObjectHolder(RegistryNames.CUT_RED_SANDSTONE_SLAB)
	public static final VerticalSlabBlock CUT_RED_SANDSTONE_SLAB = null;
	@ObjectHolder(RegistryNames.BRICK_SLAB)
	public static final VerticalSlabBlock BRICK_SLAB = null;
	@ObjectHolder(RegistryNames.BRICK_STAIRS)
	public static final VerticalStairBlock BRICK_STAIRS = null;
	@ObjectHolder(RegistryNames.NETHER_BRICK_SLAB)
	public static final VerticalSlabBlock NETHER_BRICK_SLAB = null;
	@ObjectHolder(RegistryNames.NETHER_BRICK_STAIRS)
	public static final VerticalStairBlock NETHER_BRICK_STAIRS = null;
	@ObjectHolder(RegistryNames.RED_NETHER_BRICK_SLAB)
	public static final VerticalSlabBlock RED_NETHER_BRICK_SLAB = null;
	@ObjectHolder(RegistryNames.RED_NETHER_BRICK_STAIRS)
	public static final VerticalStairBlock RED_NETHER_BRICK_STAIRS = null;
	@ObjectHolder(RegistryNames.QUARTZ_SLAB)
	public static final VerticalSlabBlock QUARTZ_SLAB = null;
	@ObjectHolder(RegistryNames.QUARTZ_STAIRS)
	public static final VerticalStairBlock QUARTZ_STAIRS = null;
	@ObjectHolder(RegistryNames.SMOOTH_QUARTZ_SLAB)
	public static final VerticalSlabBlock SMOOTH_QUARTZ_SLAB = null;
	@ObjectHolder(RegistryNames.SMOOTH_QUARTZ_STAIRS)
	public static final VerticalStairBlock SMOOTH_QUARTZ_STAIRS = null;
	@ObjectHolder(RegistryNames.PRISMARINE_SLAB)
	public static final VerticalSlabBlock PRISMARINE_SLAB = null;
	@ObjectHolder(RegistryNames.PRISMARINE_STAIRS)
	public static final VerticalStairBlock PRISMARINE_STAIRS = null;
	@ObjectHolder(RegistryNames.PRISMARINE_BRICK_SLAB)
	public static final VerticalSlabBlock PRISMARINE_BRICK_SLAB = null;
	@ObjectHolder(RegistryNames.PRISMARINE_BRICK_STAIRS)
	public static final VerticalStairBlock PRISMARINE_BRICK_STAIRS = null;
	@ObjectHolder(RegistryNames.DARK_PRISMARINE_SLAB)
	public static final VerticalSlabBlock DARK_PRISMARINE_SLAB = null;
	@ObjectHolder(RegistryNames.DARK_PRISMARINE_STAIRS)
	public static final VerticalStairBlock DARK_PRISMARINE_STAIRS = null;
	@ObjectHolder(RegistryNames.END_STONE_BRICK_SLAB)
	public static final VerticalSlabBlock END_STONE_BRICK_SLAB = null;
	@ObjectHolder(RegistryNames.END_STONE_BRICK_STAIRS)
	public static final VerticalStairBlock END_STONE_BRICK_STAIRS = null;
	@ObjectHolder(RegistryNames.PURPUR_SLAB)
	public static final VerticalSlabBlock PURPUR_SLAB = null;
	@ObjectHolder(RegistryNames.PURPUR_STAIRS)
	public static final VerticalStairBlock PURPUR_STAIRS = null;

	public static void registerBlocks(IEventBus eventBus)
	{
		DeferredRegister<Block> blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, DVSaSMod.MOD_ID);
		if (DVSaSMod.TEST_MODE)
		{
			RegistryObject<SlabBlock> bedrockSlab = blocks.register(RegistryNames.BEDROCK_SLAB, () -> new SlabBlock(Properties.copy(Blocks.BEDROCK)));
			RegistryObject<StairBlock> bedrockStairs = blocks.register(RegistryNames.BEDROCK_STAIRS, () -> new StairBlock(() -> Blocks.BEDROCK.defaultBlockState(), Properties.copy(Blocks.BEDROCK)));
			registerSlabAndStairs(blocks, RegistryNames.VERTICAL_BEDROCK_SLAB, RegistryNames.VERTICAL_BEDROCK_STAIRS, bedrockSlab, bedrockStairs);
		}
		
		registerSlabAndStairs(blocks, RegistryNames.CUT_COPPER_SLAB, RegistryNames.CUT_COPPER_STAIRS, () -> (SlabBlock) Blocks.CUT_COPPER_SLAB, () -> (StairBlock) Blocks.CUT_COPPER_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.EXPOSED_CUT_COPPER_SLAB, RegistryNames.EXPOSED_CUT_COPPER_STAIRS, () -> (SlabBlock) Blocks.EXPOSED_CUT_COPPER_SLAB, () -> (StairBlock) Blocks.EXPOSED_CUT_COPPER_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.WEATHERED_CUT_COPPER_SLAB, RegistryNames.WEATHERED_CUT_COPPER_STAIRS, () -> (SlabBlock) Blocks.WEATHERED_CUT_COPPER_SLAB, () -> (StairBlock) Blocks.WEATHERED_CUT_COPPER_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.OXIDIZED_CUT_COPPER_SLAB, RegistryNames.OXIDIZED_CUT_COPPER_STAIRS, () -> (SlabBlock) Blocks.OXIDIZED_CUT_COPPER_SLAB, () -> (StairBlock) Blocks.OXIDIZED_CUT_COPPER_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.WAXED_CUT_COPPER_SLAB, RegistryNames.WAXED_CUT_COPPER_STAIRS, () -> (SlabBlock) Blocks.WAXED_CUT_COPPER_SLAB, () -> (StairBlock) Blocks.WAXED_CUT_COPPER_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.WAXED_EXPOSED_CUT_COPPER_SLAB, RegistryNames.WAXED_EXPOSED_CUT_COPPER_STAIRS, () -> (SlabBlock) Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB, () -> (StairBlock) Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.WAXED_WEATHERED_CUT_COPPER_SLAB, RegistryNames.WAXED_WEATHERED_CUT_COPPER_STAIRS, () -> (SlabBlock) Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB, () -> (StairBlock) Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.WAXED_OXIDIZED_CUT_COPPER_SLAB, RegistryNames.WAXED_OXIDIZED_CUT_COPPER_STAIRS, () -> (SlabBlock) Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB, () -> (StairBlock) Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.OAK_SLAB, RegistryNames.OAK_STAIRS, () -> (SlabBlock) Blocks.OAK_SLAB, () -> (StairBlock) Blocks.OAK_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.SPRUCE_SLAB, RegistryNames.SPRUCE_STAIRS, () -> (SlabBlock) Blocks.SPRUCE_SLAB, () -> (StairBlock) Blocks.SPRUCE_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.BIRCH_SLAB, RegistryNames.BIRCH_STAIRS, () -> (SlabBlock) Blocks.BIRCH_SLAB, () -> (StairBlock) Blocks.BIRCH_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.JUNGLE_SLAB, RegistryNames.JUNGLE_STAIRS, () -> (SlabBlock) Blocks.JUNGLE_SLAB, () -> (StairBlock) Blocks.JUNGLE_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.ACACIA_SLAB, RegistryNames.ACACIA_STAIRS, () -> (SlabBlock) Blocks.ACACIA_SLAB, () -> (StairBlock) Blocks.ACACIA_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.DARK_OAK_SLAB, RegistryNames.DARK_OAK_STAIRS, () -> (SlabBlock) Blocks.DARK_OAK_SLAB, () -> (StairBlock) Blocks.DARK_OAK_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.CRIMSON_SLAB, RegistryNames.CRIMSON_STAIRS, () -> (SlabBlock) Blocks.CRIMSON_SLAB, () -> (StairBlock) Blocks.CRIMSON_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.WARPED_SLAB, RegistryNames.WARPED_STAIRS, () -> (SlabBlock) Blocks.WARPED_SLAB, () -> (StairBlock) Blocks.WARPED_STAIRS);
		registerSlab(blocks, RegistryNames.PETRIFIED_OAK_SLAB, () -> (SlabBlock) Blocks.PETRIFIED_OAK_SLAB);
		registerSlabAndStairs(blocks, RegistryNames.COBBLESTONE_SLAB, RegistryNames.COBBLESTONE_STAIRS, () -> (SlabBlock) Blocks.COBBLESTONE_SLAB, () -> (StairBlock) Blocks.COBBLESTONE_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.MOSSY_COBBLESTONE_SLAB, RegistryNames.MOSSY_COBBLESTONE_STAIRS, () -> (SlabBlock) Blocks.MOSSY_COBBLESTONE_SLAB, () -> (StairBlock) Blocks.MOSSY_COBBLESTONE_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.STONE_SLAB, RegistryNames.STONE_STAIRS, () -> (SlabBlock) Blocks.STONE_SLAB, () -> (StairBlock) Blocks.STONE_STAIRS);
		registerSlab(blocks, RegistryNames.SMOOTH_STONE_SLAB, () -> (SlabBlock) Blocks.SMOOTH_STONE_SLAB);
		registerSlabAndStairs(blocks, RegistryNames.STONE_BRICK_SLAB, RegistryNames.STONE_BRICK_STAIRS, () -> (SlabBlock) Blocks.STONE_BRICK_SLAB, () -> (StairBlock) Blocks.STONE_BRICK_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.MOSSY_STONE_BRICK_SLAB, RegistryNames.MOSSY_STONE_BRICK_STAIRS, () -> (SlabBlock) Blocks.MOSSY_STONE_BRICK_SLAB, () -> (StairBlock) Blocks.MOSSY_STONE_BRICK_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.GRANITE_SLAB, RegistryNames.GRANITE_STAIRS, () -> (SlabBlock) Blocks.GRANITE_SLAB, () -> (StairBlock) Blocks.GRANITE_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.POLISHED_GRANITE_SLAB, RegistryNames.POLISHED_GRANITE_STAIRS, () -> (SlabBlock) Blocks.POLISHED_GRANITE_SLAB, () -> (StairBlock) Blocks.POLISHED_GRANITE_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.DIORITE_SLAB, RegistryNames.DIORITE_STAIRS, () -> (SlabBlock) Blocks.DIORITE_SLAB, () -> (StairBlock) Blocks.DIORITE_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.POLISHED_DIORITE_SLAB, RegistryNames.POLISHED_DIORITE_STAIRS, () -> (SlabBlock) Blocks.POLISHED_DIORITE_SLAB, () -> (StairBlock) Blocks.POLISHED_DIORITE_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.ANDESITE_SLAB, RegistryNames.ANDESITE_STAIRS, () -> (SlabBlock) Blocks.ANDESITE_SLAB, () -> (StairBlock) Blocks.ANDESITE_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.POLISHED_ANDESITE_SLAB, RegistryNames.POLISHED_ANDESITE_STAIRS, () -> (SlabBlock) Blocks.POLISHED_ANDESITE_SLAB, () -> (StairBlock) Blocks.POLISHED_ANDESITE_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.COBBLED_DEEPSLATE_SLAB, RegistryNames.COBBLED_DEEPSLATE_STAIRS, () -> (SlabBlock) Blocks.COBBLED_DEEPSLATE_SLAB, () -> (StairBlock) Blocks.COBBLED_DEEPSLATE_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.POLISHED_DEEPSLATE_SLAB, RegistryNames.POLISHED_DEEPSLATE_STAIRS, () -> (SlabBlock) Blocks.POLISHED_DEEPSLATE_SLAB, () -> (StairBlock) Blocks.POLISHED_DEEPSLATE_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.DEEPSLATE_BRICK_SLAB, RegistryNames.DEEPSLATE_BRICK_STAIRS, () -> (SlabBlock) Blocks.DEEPSLATE_BRICK_SLAB, () -> (StairBlock) Blocks.DEEPSLATE_BRICK_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.DEEPSLATE_TILE_SLAB, RegistryNames.DEEPSLATE_TILE_STAIRS, () -> (SlabBlock) Blocks.DEEPSLATE_TILE_SLAB, () -> (StairBlock) Blocks.DEEPSLATE_TILE_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.BLACKSTONE_SLAB, RegistryNames.BLACKSTONE_STAIRS, () -> (SlabBlock) Blocks.BLACKSTONE_SLAB, () -> (StairBlock) Blocks.BLACKSTONE_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.POLISHED_BLACKSTONE_SLAB, RegistryNames.POLISHED_BLACKSTONE_STAIRS, () -> (SlabBlock) Blocks.POLISHED_BLACKSTONE_SLAB, () -> (StairBlock) Blocks.POLISHED_BLACKSTONE_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.POLISHED_BLACKSTONE_BRICK_SLAB, RegistryNames.POLISHED_BLACKSTONE_BRICK_STAIRS, () -> (SlabBlock) Blocks.POLISHED_BLACKSTONE_BRICK_SLAB, () -> (StairBlock) Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.SANDSTONE_SLAB, RegistryNames.SANDSTONE_STAIRS, () -> (SlabBlock) Blocks.SANDSTONE_SLAB, () -> (StairBlock) Blocks.SANDSTONE_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.SMOOTH_SANDSTONE_SLAB, RegistryNames.SMOOTH_SANDSTONE_STAIRS, () -> (SlabBlock) Blocks.SMOOTH_SANDSTONE_SLAB, () -> (StairBlock) Blocks.SMOOTH_SANDSTONE_STAIRS);
		registerSlab(blocks, RegistryNames.CUT_SANDSTONE_SLAB, () -> (SlabBlock) Blocks.CUT_SANDSTONE_SLAB);
		registerSlabAndStairs(blocks, RegistryNames.RED_SANDSTONE_SLAB, RegistryNames.RED_SANDSTONE_STAIRS, () -> (SlabBlock) Blocks.RED_SANDSTONE_SLAB, () -> (StairBlock) Blocks.RED_SANDSTONE_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.SMOOTH_RED_SANDSTONE_SLAB, RegistryNames.SMOOTH_RED_SANDSTONE_STAIRS, () -> (SlabBlock) Blocks.SMOOTH_RED_SANDSTONE_SLAB, () -> (StairBlock) Blocks.SMOOTH_RED_SANDSTONE_STAIRS);
		registerSlab(blocks, RegistryNames.CUT_RED_SANDSTONE_SLAB, () -> (SlabBlock) Blocks.CUT_RED_SANDSTONE_SLAB);
		registerSlabAndStairs(blocks, RegistryNames.BRICK_SLAB, RegistryNames.BRICK_STAIRS, () -> (SlabBlock) Blocks.BRICK_SLAB, () -> (StairBlock) Blocks.BRICK_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.NETHER_BRICK_SLAB, RegistryNames.NETHER_BRICK_STAIRS, () -> (SlabBlock) Blocks.NETHER_BRICK_SLAB, () -> (StairBlock) Blocks.NETHER_BRICK_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.RED_NETHER_BRICK_SLAB, RegistryNames.RED_NETHER_BRICK_STAIRS, () -> (SlabBlock) Blocks.RED_NETHER_BRICK_SLAB, () -> (StairBlock) Blocks.RED_NETHER_BRICK_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.QUARTZ_SLAB, RegistryNames.QUARTZ_STAIRS, () -> (SlabBlock) Blocks.QUARTZ_SLAB, () -> (StairBlock) Blocks.QUARTZ_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.SMOOTH_QUARTZ_SLAB, RegistryNames.SMOOTH_QUARTZ_STAIRS, () -> (SlabBlock) Blocks.SMOOTH_QUARTZ_SLAB, () -> (StairBlock) Blocks.SMOOTH_QUARTZ_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.PRISMARINE_SLAB, RegistryNames.PRISMARINE_STAIRS, () -> (SlabBlock) Blocks.PRISMARINE_SLAB, () -> (StairBlock) Blocks.PRISMARINE_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.PRISMARINE_BRICK_SLAB, RegistryNames.PRISMARINE_BRICK_STAIRS, () -> (SlabBlock) Blocks.PRISMARINE_BRICK_SLAB, () -> (StairBlock) Blocks.PRISMARINE_BRICK_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.DARK_PRISMARINE_SLAB, RegistryNames.DARK_PRISMARINE_STAIRS, () -> (SlabBlock) Blocks.DARK_PRISMARINE_SLAB, () -> (StairBlock) Blocks.DARK_PRISMARINE_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.END_STONE_BRICK_SLAB, RegistryNames.END_STONE_BRICK_STAIRS, () -> (SlabBlock) Blocks.END_STONE_BRICK_SLAB, () -> (StairBlock) Blocks.END_STONE_BRICK_STAIRS);
		registerSlabAndStairs(blocks, RegistryNames.PURPUR_SLAB, RegistryNames.PURPUR_STAIRS, () -> (SlabBlock) Blocks.PURPUR_SLAB, () -> (StairBlock) Blocks.PURPUR_STAIRS);
		blocks.register(eventBus);
	}

	public static void registerSlabAndStairs(DeferredRegister<Block> blocks, String slabName, String stairName, Supplier<SlabBlock> slabModel, Supplier<StairBlock> stairModel)
	{
		registerSlab(blocks, slabName, slabModel);
		registerStairs(blocks, stairName, stairModel);
	}

	public static void registerSlab(DeferredRegister<Block> blocks, String slabName, Supplier<SlabBlock> model)
	{
		blocks.register(slabName, () -> new VerticalSlabBlock(model.get()));
	}

	public static void registerStairs(DeferredRegister<Block> blocks, String stairName, Supplier<StairBlock> model)
	{
		blocks.register(stairName, () -> new VerticalStairBlock(model.get()));
	}
}
