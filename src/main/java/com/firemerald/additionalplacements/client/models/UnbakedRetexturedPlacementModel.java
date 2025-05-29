package com.firemerald.additionalplacements.client.models;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class UnbakedRetexturedPlacementModel implements UnbakedModel
{
	private record ModelKey(ResourceLocation ourModelLocation, ModelState ourModelState, ResourceLocation theirModelLocation, UnbakedModel theirModel) {}

	private static final Map<ModelKey, UnbakedRetexturedPlacementModel> MODEL_CACHE = new HashMap<>();

	public static UnbakedRetexturedPlacementModel of(ResourceLocation ourModelLocation, ModelState ourModelState, ResourceLocation theirModelLocation, UnbakedModel theirModel) {
		return MODEL_CACHE.computeIfAbsent(new ModelKey(ourModelLocation, ourModelState, theirModelLocation, theirModel), UnbakedRetexturedPlacementModel::new);
	}

	public static void clearCache() {
		MODEL_CACHE.clear();
	}

	public final ResourceLocation ourModelLocation;
	public final ModelState ourModelState;
	public final ResourceLocation theirModelLocation;
	public final UnbakedModel theirModel;
	private UnbakedModel ourModel;

	private UnbakedRetexturedPlacementModel(ModelKey key)
	{
		this.ourModelLocation = key.ourModelLocation;
		this.ourModelState = key.ourModelState;
		this.theirModelLocation = key.theirModelLocation;
		this.theirModel = key.theirModel;
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return List.of(ourModelLocation);
	}

	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> function) {
		ourModel = function.apply(ourModelLocation);
	}

	@Override
	public BakedModel bake(ModelBaker baker, Function<Material, TextureAtlasSprite> sprites, ModelState modelState, ResourceLocation modelLocation) {
		return BakedRetexturedPlacementModel.of(BakingCache.bake(ourModel, ourModelState, sprites, ourModelLocation, baker), BakingCache.bake(theirModel, sprites, theirModelLocation, baker));
	}
}
