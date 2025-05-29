package com.firemerald.additionalplacements.client.models;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class UnbakedRetexturedPlacementModel implements BlockStateModel.UnbakedRoot
{
	private record ModelKey(ResourceLocation ourModelLocation, ModelState ourModelState, BlockStateModel.UnbakedRoot theirModel) {}

	private static final Map<ModelKey, UnbakedRetexturedPlacementModel> MODEL_CACHE = new HashMap<>();

	public static UnbakedRetexturedPlacementModel of(ResourceLocation ourModelLocation, ModelState ourModelState, BlockStateModel.UnbakedRoot theirModel) {
		return MODEL_CACHE.computeIfAbsent(new ModelKey(ourModelLocation, ourModelState, theirModel), UnbakedRetexturedPlacementModel::new);
	}

	public static void clearCache() {
		MODEL_CACHE.clear();
	}

	public final ResourceLocation ourModelLocation;
	public final ModelState ourModelState;
	public final BlockStateModel.UnbakedRoot theirModel;

	private UnbakedRetexturedPlacementModel(ModelKey key)
	{
		this.ourModelLocation = key.ourModelLocation;
		this.ourModelState = key.ourModelState;
		this.theirModel = key.theirModel;
	}

	@Override
	public void resolveDependencies(@NotNull Resolver resolver) {
		resolver.markDependency(ourModelLocation);
	}

	@Override
	public @NotNull BlockStateModel bake(BlockState state, @NotNull ModelBaker baker) {
		AdditionalPlacementBlock<?> block = (AdditionalPlacementBlock<?>) state.getBlock();
		BlockState theirState = block.getModelState(state);
		BlockStateModel ourModel = BakingCache.getSimpleUnbaked(ourModelLocation, ourModelState, baker);
		BlockStateModel theirModel = BakingCache.bake(this.theirModel, theirState, baker);
		return BakedRetexturedPlacementModel.of(ourModel, theirModel);
	}

	@Override
	public @NotNull Object visualEqualityGroup(BlockState state) {
		BlockState theirState = ((AdditionalPlacementBlock<?>) state.getBlock()).getModelState(state);
		return theirModel.visualEqualityGroup(theirState);
	}
}
