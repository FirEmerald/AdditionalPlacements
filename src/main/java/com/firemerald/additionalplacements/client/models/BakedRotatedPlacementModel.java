package com.firemerald.additionalplacements.client.models;

import com.firemerald.additionalplacements.util.BlockRotation;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.util.RandomSource;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class BakedRotatedPlacementModel extends PlacementModelWrapper
{
	private record ModelKey(BlockStateModel theirModel, BlockRotation modelRotation, boolean rotatesTexture) {}

	private static final Map<ModelKey, BakedRotatedPlacementModel> MODEL_CACHE = new HashMap<>();

	public static synchronized BakedRotatedPlacementModel of(BlockStateModel theirModel, BlockRotation modelRotation, boolean rotatesTexture) {
		return MODEL_CACHE.computeIfAbsent(new ModelKey(theirModel, modelRotation, rotatesTexture), BakedRotatedPlacementModel::new);
	}

	public static void clearCache() {
		MODEL_CACHE.clear();
	}

	private final BlockRotation modelRotation;
	public final boolean rotatesTexture;

	private BakedRotatedPlacementModel(ModelKey key)
	{
		super(key.theirModel);
		this.modelRotation = key.modelRotation;
		this.rotatesTexture = key.rotatesTexture;
	}

	@Override
	public Stream<BlockModelPart> wrapParts(RandomSource random) {
		return wrapped.collectParts(random).stream().map(toWrap -> new RotatedBlockModelPart(toWrap, modelRotation, rotatesTexture));
	}
}
