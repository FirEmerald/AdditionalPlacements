package com.firemerald.additionalplacements.client.models;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class PlacementBlockModelLoader implements IModelLoader<PlacementBlockModel>
{
	public static final ResourceLocation ID = new ResourceLocation(AdditionalPlacementsMod.MOD_ID, "placement_block_loader");
	public static final Map<ModelKey, Pair<TextureAtlasSprite, Integer>> TEXTURE_CACHE = new HashMap<>();

	@Override
	public void onResourceManagerReload(ResourceManager manager)
	{
		TEXTURE_CACHE.clear();
	}

	@Override
	public PlacementBlockModel read(JsonDeserializationContext deserializationContext, JsonObject modelContents)
	{
		return new PlacementBlockModel(new ResourceLocation(modelContents.get("model").getAsString()));
	}

}
