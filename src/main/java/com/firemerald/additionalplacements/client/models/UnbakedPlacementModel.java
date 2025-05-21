package com.firemerald.additionalplacements.client.models;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.util.BlockRotation;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;

public class UnbakedPlacementModel implements UnbakedModel
{
	private record ModelKey(AdditionalPlacementBlock<?> block, ResourceLocation ourModelLocation, ModelState ourModelRotation, UnbakedModel theirModel, BlockRotation theirModelRotation) {}

	private static final Map<ModelKey, UnbakedPlacementModel> MODEL_CACHE = new HashMap<>();

	public static UnbakedPlacementModel of(AdditionalPlacementBlock<?> block, ResourceLocation ourModelLocation, ModelState ourModelRotation, UnbakedModel theirModel, BlockRotation theirModelRotation) {
		return MODEL_CACHE.computeIfAbsent(new ModelKey(block, ourModelLocation, ourModelRotation, theirModel, theirModelRotation), UnbakedPlacementModel::new);
	}

	public static void clearCache() {
		MODEL_CACHE.clear();
	}

	public final AdditionalPlacementBlock<?> block;
	public final ResourceLocation ourModelLocation;
	public final ModelState ourModelRotation;
	public final UnbakedModel theirModel;
	public final BlockRotation theirModelRotation;

	private UnbakedPlacementModel(ModelKey key)
	{
		this.block = key.block;
		this.ourModelLocation = key.ourModelLocation;
		this.ourModelRotation = key.ourModelRotation;
		this.theirModel = key.theirModel;
		this.theirModelRotation = key.theirModelRotation;
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return Collections.singleton(ourModelLocation);
	}

	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> resolver) {
		resolver.apply(ourModelLocation);
	}

	@Override
	public BakedModel bake(ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState state)
	{
		return BakedPlacementModel.of(baker, ourModelRotation, block, ourModelLocation, theirModel, theirModelRotation, spriteGetter);
	}
}
