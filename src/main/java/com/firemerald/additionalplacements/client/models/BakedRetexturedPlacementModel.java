package com.firemerald.additionalplacements.client.models;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand)
	{
		BlockState modelState = BlockModelUtils.getModeledState(state);
		return BlockModelUtils.retexturedQuads(side, dir -> ourModel.getQuads(state, dir, rand), dir -> parent.getQuads(modelState, dir, rand), null);
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData extraData, RenderType renderType)
	{
		BlockState modelState = BlockModelUtils.getModeledState(state);
		return BlockModelUtils.retexturedQuads(side, dir -> ourModel.getQuads(state, dir, rand, extraData, renderType), dir -> parent.getQuads(modelState, dir, rand, extraData, renderType), renderType);
	}
}
