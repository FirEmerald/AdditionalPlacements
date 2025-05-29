package com.firemerald.additionalplacements.client.models;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BakedRetexturedPlacementModel extends PlacementModelWrapper
{
	private record ModelKey(BakedModel ourModel, BakedModel theirModel) {}

	private static final Map<ModelKey, BakedRetexturedPlacementModel> MODEL_CACHE = new HashMap<>();

	public static synchronized BakedRetexturedPlacementModel of(BakedModel ourModel, BakedModel theirModel) {
		return MODEL_CACHE.computeIfAbsent(new ModelKey(ourModel, theirModel), BakedRetexturedPlacementModel::new);
	}

	public static void clearCache() {
		MODEL_CACHE.clear();
	}

	private final BakedModel ourModel;

	private BakedRetexturedPlacementModel(ModelKey key)
	{
		super(key.theirModel);
		this.ourModel = key.ourModel;
	}

	@Override
	@Deprecated
	public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand)
	{
		BlockState modelState = BlockModelUtils.getModeledState(state);
		return BlockModelUtils.retexturedQuads(side, dir -> ourModel.getQuads(state, dir, rand), dir -> wrapped.getQuads(modelState, dir, rand), null);
	}
}
