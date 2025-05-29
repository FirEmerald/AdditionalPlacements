package com.firemerald.additionalplacements.client.models;

import net.minecraft.client.renderer.block.model.UnbakedBlockStateModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class UnbakedRetexturedPlacementModel implements UnbakedBlockStateModel
{
	private record ModelKey(ResourceLocation ourModelLocation, ModelState ourModelState, UnbakedBlockStateModel theirModel) {}

	private static final Map<ModelKey, UnbakedRetexturedPlacementModel> MODEL_CACHE = new HashMap<>();

	public static UnbakedRetexturedPlacementModel of(ResourceLocation ourModelLocation, ModelState ourModelState, UnbakedBlockStateModel theirModel) {
		return MODEL_CACHE.computeIfAbsent(new ModelKey(ourModelLocation, ourModelState, theirModel), UnbakedRetexturedPlacementModel::new);
	}

	public static void clearCache() {
		MODEL_CACHE.clear();
	}

	public final ResourceLocation ourModelLocation;
	public final ModelState ourModelState;
	public final UnbakedBlockStateModel theirModel;

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
	public BakedModel bake(ModelBaker baker) {
		return BakedRetexturedPlacementModel.of(BakingCache.getSimpleUnbaked(ourModelLocation, ourModelState, baker), BakingCache.bake(theirModel, baker));
	}

	@Override
	public Object visualEqualityGroup(BlockState state) {
		return theirModel.visualEqualityGroup(BlockModelUtils.getModeledState(state));
	}
}
