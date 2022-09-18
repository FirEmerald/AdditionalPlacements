package com.firemerald.dvsas.client.models;

import com.firemerald.dvsas.DVSaSMod;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class VerticalBlockModelLoader implements IGeometryLoader<VerticalBlockModel>
{
	public static final ResourceLocation ID = new ResourceLocation(DVSaSMod.MOD_ID, "vertical_block_loader");

	@Override
	public VerticalBlockModel read(JsonObject modelContents, JsonDeserializationContext deserializationContext)
	{
		return new VerticalBlockModel(new ResourceLocation(modelContents.get("model").getAsString()));
	}
}
