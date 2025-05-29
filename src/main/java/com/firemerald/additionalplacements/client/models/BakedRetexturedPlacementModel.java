package com.firemerald.additionalplacements.client.models;

import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.util.RandomSource;

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
	public Stream<BlockModelPart> wrapParts(RandomSource random) {
		List<BlockModelPart> wrappedParts = wrapped.collectParts(random);
		return ourModel.collectParts(random).stream().map(toWrap -> new RetexturedBlockModelPart(toWrap, wrappedParts));
	}
}
