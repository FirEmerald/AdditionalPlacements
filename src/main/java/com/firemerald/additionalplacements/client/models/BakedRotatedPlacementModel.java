package com.firemerald.additionalplacements.client.models;

import com.firemerald.additionalplacements.util.BlockRotation;
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

public class BakedRotatedPlacementModel extends PlacementModelWrapper
{
	private record ModelKey(BakedModel theirModel, BlockRotation modelRotation, boolean rotatesTexture) {}

	private static final Map<ModelKey, BakedRotatedPlacementModel> MODEL_CACHE = new HashMap<>();

	public static synchronized BakedRotatedPlacementModel of(BakedModel theirModel, BlockRotation modelRotation, boolean rotatesTexture) {
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
	@Deprecated
	public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand)
	{
		BlockState modelState = BlockModelUtils.getModeledState(state);
		return BlockModelUtils.rotatedQuads(modelRotation, rotatesTexture, side, dir -> originalModel.getQuads(modelState, dir, rand), null);
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData extraData, RenderType renderType)
	{
		BlockState modelState = BlockModelUtils.getModeledState(state);
		return BlockModelUtils.rotatedQuads(modelRotation, rotatesTexture, side, dir -> originalModel.getQuads(modelState, dir, rand, extraData, renderType), renderType);
	}
}
