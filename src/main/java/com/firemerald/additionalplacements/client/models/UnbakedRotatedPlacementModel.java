package com.firemerald.additionalplacements.client.models;

import com.firemerald.additionalplacements.util.BlockRotation;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;

import java.util.*;
import java.util.function.Function;

public class UnbakedRotatedPlacementModel implements UnbakedModel
{
	private record ModelKey(ResourceLocation theirModelLocation, UnbakedModel theirModel, BlockRotation theirModelRotation, boolean rotatesTexture) {}

	private static final Map<ModelKey, UnbakedRotatedPlacementModel> MODEL_CACHE = new HashMap<>();

	public static UnbakedRotatedPlacementModel of(ResourceLocation theirModelLocation, UnbakedModel theirModel, BlockRotation theirModelRotation, boolean rotatesTexture) {
		return MODEL_CACHE.computeIfAbsent(new ModelKey(theirModelLocation, theirModel, theirModelRotation, rotatesTexture), UnbakedRotatedPlacementModel::new);
	}

	public static void clearCache() {
		MODEL_CACHE.clear();
	}

	public final ResourceLocation theirModelLocation;
	public final UnbakedModel theirModel;
	public final BlockRotation theirModelRotation;
	public final boolean rotatesTexture;

	private UnbakedRotatedPlacementModel(ModelKey key)
	{
		this.theirModelLocation = key.theirModelLocation;
		this.theirModel = key.theirModel;
		this.theirModelRotation = key.theirModelRotation;
		this.rotatesTexture = key.rotatesTexture;
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return List.of();
	}

	@Override
	public Collection<Material> getMaterials(Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
		return List.of();
	}

	@Override
	public BakedModel bake(ModelBakery baker, Function<Material, TextureAtlasSprite> sprites, ModelState modelState, ResourceLocation modelLocation) {
		return BakedRotatedPlacementModel.of(BakingCache.bake(theirModel, sprites, theirModelLocation, baker), theirModelRotation, rotatesTexture);
	}
}
