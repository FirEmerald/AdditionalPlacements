package com.firemerald.dvsas.client.models;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

import com.mojang.datafixers.util.Pair;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;

public class VerticalBlockModel implements IModelGeometry<VerticalBlockModel>
{
	public final ResourceLocation model;

	public VerticalBlockModel(ResourceLocation model)
	{
		this.model = model;
	}

	@Override
	public BakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation)
	{
		return new BakedVerticalBlockModel(bakery.getModel(model).bake(bakery, spriteGetter, modelTransform, modelLocation)); //TODO make better
	}

	@Override
	public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors)
	{
		UnbakedModel model = modelGetter.apply(this.model);
		if (model != null) return model.getMaterials(modelGetter, missingTextureErrors);
		else return Collections.emptyList();
	}

}
