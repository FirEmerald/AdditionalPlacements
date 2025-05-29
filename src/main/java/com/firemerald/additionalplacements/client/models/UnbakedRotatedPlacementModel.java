package com.firemerald.additionalplacements.client.models;

import com.firemerald.additionalplacements.util.BlockRotation;
import net.minecraft.client.renderer.block.model.UnbakedBlockStateModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class UnbakedRotatedPlacementModel implements UnbakedBlockStateModel
{
	private record ModelKey(UnbakedBlockStateModel theirModel, BlockRotation theirModelRotation, boolean rotatesTexture) {}

	private static final Map<ModelKey, UnbakedRotatedPlacementModel> MODEL_CACHE = new HashMap<>();

	public static UnbakedRotatedPlacementModel of(UnbakedBlockStateModel theirModel, BlockRotation theirModelRotation, boolean rotatesTexture) {
		return MODEL_CACHE.computeIfAbsent(new ModelKey(theirModel, theirModelRotation, rotatesTexture), UnbakedRotatedPlacementModel::new);
	}

	public static void clearCache() {
		MODEL_CACHE.clear();
	}

	public final UnbakedBlockStateModel theirModel;
	public final BlockRotation theirModelRotation;
	public final boolean rotatesTexture;

	private UnbakedRotatedPlacementModel(ModelKey key)
	{
		this.theirModel = key.theirModel;
		this.theirModelRotation = key.theirModelRotation;
		this.rotatesTexture = key.rotatesTexture;
	}

	@Override
	public void resolveDependencies(Resolver resolver) {
	}

	@Override
	public BakedModel bake(ModelBaker baker) {
		return BakedRotatedPlacementModel.of(BakingCache.bake(theirModel, baker), theirModelRotation, rotatesTexture);
	}

	@Override
	public Object visualEqualityGroup(BlockState state) {
		return theirModel.visualEqualityGroup(BlockModelUtils.getModeledState(state));
	}
}
