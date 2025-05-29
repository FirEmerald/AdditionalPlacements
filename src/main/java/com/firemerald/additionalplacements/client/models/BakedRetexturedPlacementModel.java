package com.firemerald.additionalplacements.client.models;

import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class BakedRetexturedPlacementModel extends PlacementModelWrapper
{
	private record ModelKey(BlockStateModel ourModel, BlockStateModel theirModel) {}

	private static final Map<ModelKey, BakedRetexturedPlacementModel> MODEL_CACHE = new HashMap<>();

	public static synchronized BakedRetexturedPlacementModel of(BlockStateModel ourModel, BlockStateModel theirModel) {
		return MODEL_CACHE.computeIfAbsent(new ModelKey(ourModel, theirModel), BakedRetexturedPlacementModel::new);
	}

	public static void clearCache() {
		MODEL_CACHE.clear();
	}

	private final BlockStateModel ourModel;

	private BakedRetexturedPlacementModel(ModelKey key)
	{
		super(key.theirModel);
		this.ourModel = key.ourModel;
	}
	
	@Override
	@Deprecated
	public Stream<BlockModelPart> wrapParts(RandomSource random) {
		List<BlockModelPart> wrappedParts = delegate.collectParts(random);
		return ourModel.collectParts(random).stream().map(toWrap -> new RetexturedBlockModelPart(toWrap, wrappedParts));
	}

	@Override
	protected Stream<BlockModelPart> wrapParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random) {
		List<BlockModelPart> wrappedParts = delegate.collectParts(level, pos, BlockModelUtils.getModeledState(state), random);
		return ourModel.collectParts(level, pos, state, random).stream().map(toWrap -> new RetexturedBlockModelPart(toWrap, wrappedParts));
	}
}
