package com.firemerald.additionalplacements.common;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class AdditionalPlacementsBlockTags
{
	private static final IntPredicate SEPERATOR = c -> { return c == '_' || c == ' ' || c == '/' || c == '.'; };
	private static Map<String, Map<TagKey<Block>, TagKey<Block>>> remappedTags = new HashMap<>();

	public static Set<TagKey<Block>> remap(Stream<TagKey<Block>> tags, String typeName, String typeNamePlural)
	{
		Map<TagKey<Block>, TagKey<Block>> mapped = remappedTags.computeIfAbsent(typeName, key -> new HashMap<>());
		return tags.map(tag -> mapped.computeIfAbsent(tag, name -> {
			ResourceLocation loc = tag.location();
			int beginPlural = loc.getPath().toLowerCase(Locale.ENGLISH).indexOf(typeNamePlural.toLowerCase(Locale.ENGLISH));
			if (beginPlural < 0)
			{
				int begin = loc.getPath().toLowerCase(Locale.ENGLISH).indexOf(typeName.toLowerCase(Locale.ENGLISH));
				if (begin < 0) return tag;
				else return remap(begin, tag, typeName);
			}
			else return remap(beginPlural, tag, typeNamePlural);
		})).collect(Collectors.toSet());
	}

	private static TagKey<Block> remap(int begin, TagKey<Block> tag, String typeName)
	{
		ResourceLocation loc = tag.location();
		String path = loc.getPath();
		if ((begin > 0 //check char before
				&& !SEPERATOR.test(path.charAt(begin - 1))) || (begin + typeName.length() < path.length() //check char after
				&& !SEPERATOR.test(path.charAt(begin + typeName.length())))) return tag;
		return BlockTags.create(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, loc.getNamespace() + "/" + path.substring(0, begin) + "vertical_" + path.substring(begin)));
	}}
