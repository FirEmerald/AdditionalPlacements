package com.firemerald.additionalplacements.client.models;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.util.BlockRotation;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class UnbakedRotatedPlacementModel implements BlockStateModel.UnbakedRoot
{
	private record ModelKey(BlockStateModel.UnbakedRoot theirModel) {}

	private static final Map<ModelKey, UnbakedRotatedPlacementModel> MODEL_CACHE = new HashMap<>();

	public static UnbakedRotatedPlacementModel of(BlockStateModel.UnbakedRoot theirModel) {
		return MODEL_CACHE.computeIfAbsent(new ModelKey(theirModel), UnbakedRotatedPlacementModel::new);
	}

	public static void clearCache() {
		MODEL_CACHE.clear();
	}

	public final BlockStateModel.UnbakedRoot theirModel;

	private UnbakedRotatedPlacementModel(ModelKey key)
	{
		this.theirModel = key.theirModel;
	}

	@Override
	public void resolveDependencies(@NotNull Resolver resolver) {
	}

	@Override
	public @NotNull BlockStateModel bake(BlockState state, @NotNull ModelBaker baker) {
		AdditionalPlacementBlock<?> block = (AdditionalPlacementBlock<?>) state.getBlock();
		BlockState theirState = block.getModelState(state);
		BlockRotation theirModelRotation = block.getRotation(state);
		boolean rotatesTexture = block.rotatesTexture(state);
		BlockStateModel theirModel = BakingCache.bake(this.theirModel, theirState, baker);
		return BakedRotatedPlacementModel.of(theirModel, theirModelRotation, rotatesTexture);
	}

	@Override
	public @NotNull Object visualEqualityGroup(BlockState state) {
		BlockState theirState = ((AdditionalPlacementBlock<?>) state.getBlock()).getModelState(state);
		return theirModel.visualEqualityGroup(theirState);
	}
}
