package com.firemerald.dvsas.datagen;

import javax.annotation.Nonnull;

import com.firemerald.dvsas.client.models.VerticalBlockModelLoader;
import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class DynamicModelLoaderBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T>
{
	public DynamicModelLoaderBuilder(T parent, ExistingFileHelper existingFileHelper)
	{
		super(VerticalBlockModelLoader.ID, parent, existingFileHelper);
	}

    private ResourceLocation model = null;

    public DynamicModelLoaderBuilder<T> setModel(@Nonnull ResourceLocation model)
    {
    	this.model = model;
    	return this;
    }

    @Override
    public JsonObject toJson(JsonObject json)
    {
        json = super.toJson(json);
        Preconditions.checkNotNull(model, "model must not be null");
        json.addProperty("model", model.toString());
        return json;
    }
}
