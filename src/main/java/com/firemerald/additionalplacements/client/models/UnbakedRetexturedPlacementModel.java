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
	private record ModelKey(ResourceLocation ourModelLocation, ModelState ourModelState, UnbakedModel theirModel) {}

	private static final Map<ModelKey, UnbakedRetexturedPlacementModel> MODEL_CACHE = new HashMap<>();

	public static UnbakedRetexturedPlacementModel of(ResourceLocation ourModelLocation, ModelState ourModelState, UnbakedModel theirModel) {
		return MODEL_CACHE.computeIfAbsent(new ModelKey(ourModelLocation, ourModelState, theirModel), UnbakedRetexturedPlacementModel::new);
	}

	public static void clearCache() {
		MODEL_CACHE.clear();
	}

	public final ResourceLocation ourModelLocation;
	public final ModelState ourModelState;
	public final UnbakedModel theirModel;
	private UnbakedModel ourModel;

	private UnbakedRetexturedPlacementModel(ModelKey key)
	{
		this.ourModelLocation = key.ourModelLocation;
		this.ourModelState = key.ourModelState;
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
	public BakedModel bake(ModelBaker baker, Function<Material, TextureAtlasSprite> sprites, ModelState modelState) {
		return BakedRetexturedPlacementModel.of(BakingCache.bake(ourModel, ourModelState, sprites, baker), BakingCache.bake(theirModel, sprites, baker));
	}
}
