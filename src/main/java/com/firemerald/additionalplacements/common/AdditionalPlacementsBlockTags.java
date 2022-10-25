package com.firemerald.additionalplacements.common;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class AdditionalPlacementsBlockTags
{
	public static final TagKey<Block> VERTICAL_SLABS = create("vertical_slabs");
	public static final TagKey<Block> VERTICAL_WOODEN_SLABS = create("vertical_wooden_slabs");
	public static final TagKey<Block> VERTICAL_STAIRS = create("vertical_stairs");
	public static final TagKey<Block> VERTICAL_WOODEN_STAIRS = create("vertical_wooden_stairs");
	public static final TagKey<Block> ADDITIONAL_WOOL_CARPETS = create("additional_wool_carpets");
	public static final TagKey<Block> ADDITIONAL_PRESSURE_PLATES = create("additional_pressure_plates");
	public static final TagKey<Block> ADDITIONAL_WOODEN_PRESSURE_PLATES = create("additional_wooden_pressure_plates");
	public static final TagKey<Block> ADDITIONAL_STONE_PRESSURE_PLATES = create("additional_stone_pressure_plates");

	private static TagKey<Block> create(String name)
	{
		return BlockTags.create(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, name));
	}
}
