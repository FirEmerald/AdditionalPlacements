package com.firemerald.dvsas.client.models;

import com.firemerald.dvsas.DVSaSMod;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class VerticalBlockModelLoader implements IModelLoader<VerticalBlockModel>
{
	public static final ResourceLocation ID = new ResourceLocation(DVSaSMod.MOD_ID, "vertical_block_loader");

	@Override
	public void onResourceManagerReload(ResourceManager manager)
	{
		//TODO reset model texture cache
	}

	@Override
	public VerticalBlockModel read(JsonDeserializationContext deserializationContext, JsonObject modelContents)
	{
		return new VerticalBlockModel(new ResourceLocation(modelContents.get("model").getAsString()));
	}

}
