package com.firemerald.additionalplacements.client.models;

import net.minecraft.client.renderer.block.model.UnbakedBlockStateModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class UnbakedRetexturedPlacementModel implements UnbakedBlockStateModel
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

	private UnbakedRetexturedPlacementModel(ModelKey key)
	{
		this.ourModelLocation = key.ourModelLocation;
		this.ourModelState = key.ourModelState;
		this.theirModel = key.theirModel;
	}

	@Override
	public void resolveDependencies(Resolver resolver) {
		resolver.resolve(ourModelLocation);
	}

	@Override
	public BakedModel bake(ModelBaker baker, Function<Material, TextureAtlasSprite> sprites, ModelState modelState) {
		return BakedRetexturedPlacementModel.of(BakingCache.getSimpleUnbaked(ourModelLocation, ourModelState, baker), BakingCache.bake(theirModel, sprites, baker));
	}

	@Override
	public Object visualEqualityGroup(BlockState state) {
		return theirModel instanceof UnbakedBlockStateModel model ? model.visualEqualityGroup(BlockModelUtils.getModeledState(state)) : this;
	}
}
