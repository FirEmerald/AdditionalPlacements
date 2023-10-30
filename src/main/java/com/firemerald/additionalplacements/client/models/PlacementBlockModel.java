package com.firemerald.additionalplacements.client.models;

import java.util.function.Function;

import io.github.fabricators_of_create.porting_lib.models.geometry.IUnbakedGeometry;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;

public class PlacementBlockModel implements IUnbakedGeometry<PlacementBlockModel>
{
	public final ResourceLocation model;
	private UnbakedModel unbaked;

	public PlacementBlockModel(ResourceLocation model)
	{
		this.model = model;
	}

	@Override
	public BakedModel bake(
			BlockModel context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter,
			ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation, boolean isGui3d)
	{
		return new BakedPlacementBlockModel(unbaked.bake(baker, spriteGetter, modelState, model)); //TODO make better
	}

	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, BlockModel context)
	{
		unbaked = modelGetter.apply(model);
	}
}
