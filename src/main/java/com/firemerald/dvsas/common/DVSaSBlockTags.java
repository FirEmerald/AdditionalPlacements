package com.firemerald.dvsas.common;

import com.firemerald.dvsas.DVSaSMod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class DVSaSBlockTags
{
	public static final TagKey<Block> VERTICAL_SLABS = create("vertical_slabs");
	public static final TagKey<Block> VERTICAL_WOODEN_SLABS = create("vertical_wooden_slabs");
	public static final TagKey<Block> VERTICAL_STAIRS = create("vertical_stairs");
	public static final TagKey<Block> VERTICAL_WOODEN_STAIRS = create("vertical_wooden_stairs");

	private static TagKey<Block> create(String name)
	{
		return BlockTags.create(new ResourceLocation(DVSaSMod.MOD_ID, name));
	}
}
