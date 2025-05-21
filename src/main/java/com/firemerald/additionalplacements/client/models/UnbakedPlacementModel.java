package com.firemerald.additionalplacements.client.models;

import java.util.HashMap;
import java.util.Map;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.util.BlockRotation;

import net.minecraft.client.renderer.block.model.UnbakedBlockStateModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class UnbakedPlacementModel implements UnbakedBlockStateModel
{
	private record ModelKey(AdditionalPlacementBlock<?> block, ResourceLocation ourModelLocation, ModelState ourModelRotation, UnbakedBlockStateModel theirModel, BlockRotation theirModelRotation) {}

	private static final Map<ModelKey, UnbakedPlacementModel> MODEL_CACHE = new HashMap<>();

	public static UnbakedPlacementModel of(AdditionalPlacementBlock<?> block, ResourceLocation ourModelLocation, ModelState ourModelRotation, UnbakedBlockStateModel theirModel, BlockRotation theirModelRotation) {
		return MODEL_CACHE.computeIfAbsent(new ModelKey(block, ourModelLocation, ourModelRotation, theirModel, theirModelRotation), UnbakedPlacementModel::new);
	}

	public static void clearCache() {
		MODEL_CACHE.clear();
	}

	public final AdditionalPlacementBlock<?> block;
	public final ResourceLocation ourModelLocation;
	public final ModelState ourModelRotation;
	public final UnbakedBlockStateModel theirModel;
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
	public void resolveDependencies(Resolver resolver) {
		resolver.resolve(ourModelLocation);
	}

	@Override
	public BakedModel bake(ModelBaker baker) {
		return BakedPlacementModel.of(baker, ourModelRotation, block, ourModelLocation, theirModel, theirModelRotation);
	}

	@Override
	public Object visualEqualityGroup(BlockState state) {
		return theirModel.visualEqualityGroup(block.getModelState(state));
	}
}
