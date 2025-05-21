package com.firemerald.additionalplacements.client.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.util.BlockRotation;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public class BakedPlacementModel extends PlacementModelWrapper
{
	private record ModelKey(AdditionalPlacementBlock<?> block, BakedModel ourModel, BakedModel theirModel, BlockRotation modelRotation) {}

	private static final Map<ModelKey, BakedPlacementModel> MODEL_CACHE = new HashMap<>();

	public static BakedPlacementModel of(ModelBakery bakery, ModelState modelTransform, AdditionalPlacementBlock<?> block, ResourceLocation ourModelLocation, ResourceLocation theirModelLocation, BlockRotation modelRotation) {
		return of(block,
				Unwrapper.unwrap(bakery.bake(ourModelLocation, modelTransform)),
				Unwrapper.unwrap(bakery.bake(theirModelLocation, BlockModelRotation.X0_Y0)),
				modelRotation);
	}

	public static BakedPlacementModel of(AdditionalPlacementBlock<?> block, BakedModel ourModel, BakedModel theirModel, BlockRotation modelRotation) {
		return MODEL_CACHE.computeIfAbsent(new ModelKey(block, ourModel, theirModel, modelRotation), BakedPlacementModel::new);
	}

	public static void clearCache() {
		MODEL_CACHE.clear();
	}

	private final AdditionalPlacementBlock<?> block;
	private final BakedModel ourModel;
	private final BlockRotation modelRotation;

	private BakedPlacementModel(ModelKey key)
	{
		super(key.theirModel);
		this.block = key.block;
		this.ourModel = key.ourModel;
		this.modelRotation = key.modelRotation;
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand)
	{
		BlockState modelState = block.getModelState(state);
		if (block.rotatesModel(state))
			return BlockModelUtils.rotatedQuads(modelState, unused -> wrapped, modelRotation, block.rotatesTexture(state), side, rand);
		else
			return BlockModelUtils.retexturedQuads(state, modelState, unused -> wrapped, ourModel, side, rand);
	}
}
