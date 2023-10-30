package com.firemerald.additionalplacements.datagen;

import javax.annotation.Nonnull;

import com.firemerald.additionalplacements.client.models.PlacementBlockModelLoader;
import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import io.github.fabricators_of_create.porting_lib.models.generators.CustomLoaderBuilder;
import io.github.fabricators_of_create.porting_lib.models.generators.ModelBuilder;
import net.minecraft.resources.ResourceLocation;

public class DynamicModelLoaderBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T>
{
	public DynamicModelLoaderBuilder(T parent, ExistingFileHelper existingFileHelper)
	{
		super(PlacementBlockModelLoader.ID, parent, existingFileHelper);
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
