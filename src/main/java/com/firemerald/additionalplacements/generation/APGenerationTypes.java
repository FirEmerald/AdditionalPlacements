package com.firemerald.additionalplacements.generation;

import java.util.function.Function;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.*;
import com.firemerald.additionalplacements.block.interfaces.ISimpleRotationBlock;
import com.firemerald.additionalplacements.config.BlockBlacklist;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;

public class APGenerationTypes {
	public static final SimpleRotatableGenerationType<SlabBlock, VerticalSlabBlock>
	SLAB                    = get(SlabBlock.class                 , "slab"                   , "Slabs"                   , 
			new SimpleRotatableGenerationType.Builder<SlabBlock, VerticalSlabBlock>()
			.blacklistModelRotation(new BlockBlacklist.Builder()
					.blockBlacklist(
							"minecraft:sandstone_slab", 
							"minecraft:cut_sandstone_slab", 
							"minecraft:red_sandstone_slab", 
							"minecraft:cut_red_sandstone_slab")
					.build())
			.blacklistTextureRotation(new BlockBlacklist.Builder()
					.blockBlacklist("minecraft:smooth_stone_slab")
					.build())
			.constructor(VerticalSlabBlock::of));
	public static final VerticalStairsGenerationType<StairBlock, VerticalStairBlock>
	STAIRS                  = get(StairBlock.class                , "stairs"                 , "Stairs"                  , 
			new VerticalStairsGenerationType.Builder<StairBlock, VerticalStairBlock>()
			.blacklistModelRotation(new BlockBlacklist.Builder()
					.blockBlacklist(
							"minecraft:sandstone_stairs", 
							"minecraft:red_sandstone_stairs")
					.build())
			.constructor(VerticalStairBlock::of));
	public static final SimpleRotatableGenerationType<CarpetBlock, AdditionalCarpetBlock>
	CARPET                  = get(CarpetBlock.class               , "carpet"                 , "Carpets"                 , AdditionalCarpetBlock::of);
	public static final SimpleRotatableGenerationType<PressurePlateBlock, AdditionalPressurePlateBlock>
	PRESSURE_PLATE          = get(PressurePlateBlock.class        , "pressure_plate"         , "Regular pressure plates" , AdditionalPressurePlateBlock::of);
	public static final SimpleRotatableGenerationType<WeightedPressurePlateBlock, AdditionalWeightedPressurePlateBlock>
	WEIGHTED_PRESSURE_PLATE = get(WeightedPressurePlateBlock.class, "weighted_pressure_plate", "Weighted pressure plates", AdditionalWeightedPressurePlateBlock::of);
	
	private static <T extends Block, U extends AdditionalPlacementBlock<T> & ISimpleRotationBlock> SimpleRotatableGenerationType<T, U> get(Class<T> clazz, String name, String description, Function<T, U> constructor) {
		return Registration.registerType(clazz, new ResourceLocation(AdditionalPlacementsMod.MOD_ID, name), description, new SimpleRotatableGenerationType.Builder<T, U>().constructor(constructor));
	}
	
	private static <T extends Block, U extends AdditionalPlacementBlock<T>, V extends GenerationType<T, U>> V get(Class<T> clazz, String name, String description, GenerationTypeConstructor<V> typeConstructor) {
		return Registration.registerType(clazz, new ResourceLocation(AdditionalPlacementsMod.MOD_ID, name), description, typeConstructor);
	}
	
	public static void init() {}
}
