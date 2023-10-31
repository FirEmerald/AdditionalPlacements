package com.firemerald.additionalplacements.common;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;

import net.minecraft.util.ResourceLocation;

public class AdditionalPlacementsBlockTags
{
	private static final IntPredicate SEPERATOR = c -> { return c == '_' || c == ' ' || c == '/' || c == '.'; };
	private static Map<String, Map<ResourceLocation, ResourceLocation>> remappedTags = new HashMap<>();

	public static Set<ResourceLocation> remap(Set<ResourceLocation> tags, String typeName, String typeNamePlural)
	{
		Map<ResourceLocation, ResourceLocation> mapped = remappedTags.computeIfAbsent(typeName, key -> new HashMap<>());
		return tags.stream().map(tag -> mapped.computeIfAbsent(tag, name -> {
			int beginPlural = tag.getPath().toLowerCase(Locale.ENGLISH).indexOf(typeNamePlural.toLowerCase(Locale.ENGLISH));
			if (beginPlural < 0)
			{
				int begin = tag.getPath().toLowerCase(Locale.ENGLISH).indexOf(typeName.toLowerCase(Locale.ENGLISH));
				if (begin < 0) return tag;
				else return remap(begin, tag, typeName);
			}
			else return remap(beginPlural, tag, typeNamePlural);
		})).collect(Collectors.toSet());
	}

	private static ResourceLocation remap(int begin, ResourceLocation tag, String typeName)
	{
		String path = tag.getPath();
		if ((begin > 0 //check char before
				&& !SEPERATOR.test(path.charAt(begin - 1))) || (begin + typeName.length() < path.length() //check char after
				&& !SEPERATOR.test(path.charAt(begin + typeName.length())))) return tag;
		return new ResourceLocation(AdditionalPlacementsMod.MOD_ID, tag.getNamespace() + "/" + path.substring(0, begin) + "vertical_" + path.substring(begin));
	}
}
