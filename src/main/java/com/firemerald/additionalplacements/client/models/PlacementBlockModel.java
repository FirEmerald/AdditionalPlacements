package com.firemerald.additionalplacements.client.models;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

import com.mojang.datafixers.util.Pair;

import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;

public class PlacementBlockModel implements IModelGeometry<PlacementBlockModel>
{
	public final ResourceLocation model;

	public PlacementBlockModel(ResourceLocation model)
	{
		this.model = model;
	}

	@Override
	public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation)
	{
		return new BakedPlacementBlockModel(bakery.getModel(model).bake(bakery, spriteGetter, modelTransform, modelLocation)); //TODO make better
	}

	@Override
	public Collection<RenderMaterial> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors)
	{
		IUnbakedModel model = modelGetter.apply(this.model);
		if (model != null) return model.getMaterials(modelGetter, missingTextureErrors);
		else return Collections.emptyList();
	}
}
