package com.firemerald.additionalplacements.client.models;

import com.firemerald.additionalplacements.util.BlockRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class UnbakedRotatedPlacementModel implements UnbakedModel
{
	private record ModelKey(UnbakedModel theirModel, BlockRotation theirModelRotation, boolean rotatesTexture) {}

	private static final Map<ModelKey, UnbakedRotatedPlacementModel> MODEL_CACHE = new HashMap<>();

	public static UnbakedRotatedPlacementModel of(UnbakedModel theirModel, BlockRotation theirModelRotation, boolean rotatesTexture) {
		return MODEL_CACHE.computeIfAbsent(new ModelKey(theirModel, theirModelRotation, rotatesTexture), UnbakedRotatedPlacementModel::new);
	}

	public static void clearCache() {
		MODEL_CACHE.clear();
	}

	public final UnbakedModel theirModel;
	public final BlockRotation theirModelRotation;
	public final boolean rotatesTexture;

	private UnbakedRotatedPlacementModel(ModelKey key)
	{
		this.theirModel = key.theirModel;
		this.theirModelRotation = key.theirModelRotation;
		this.rotatesTexture = key.rotatesTexture;
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return List.of();
	}

	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> function) {}

	@Override
	public BakedModel bake(ModelBaker baker, Function<Material, TextureAtlasSprite> sprites, ModelState modelState) {
		return BakedRotatedPlacementModel.of(BakingCache.bake(theirModel, sprites, baker), theirModelRotation, rotatesTexture);
	}
}
