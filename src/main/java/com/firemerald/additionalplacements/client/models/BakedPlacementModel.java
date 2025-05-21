package com.firemerald.additionalplacements.client.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.util.BlockRotation;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;

public class BakedPlacementModel extends PlacementModelWrapper
{
	private record ModelKey(AdditionalPlacementBlock<?> block, BakedModel ourModel, BakedModel theirModel, BlockRotation modelRotation) {}

	private static final Map<ModelKey, BakedPlacementModel> MODEL_CACHE = new HashMap<>();

	public static BakedPlacementModel of(ModelBakery bakery, ModelState modelTransform, AdditionalPlacementBlock<?> block, ResourceLocation ourModelLocation, ResourceLocation theirModelLocation, BlockRotation modelRotation, Function<Material, TextureAtlasSprite> sprites) {
		return of(block,
				Unwrapper.unwrap(bakery.bake(ourModelLocation, modelTransform, sprites)),
				Unwrapper.unwrap(bakery.bake(theirModelLocation, BlockModelRotation.X0_Y0, sprites)),
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
	public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand, IModelData extraData) {
		BlockState modelState = block.getModelState(state);
		if (block.rotatesModel(state))
			return BlockModelUtils.rotatedQuads(modelState, unused -> originalModel, modelRotation, block.rotatesTexture(state), side, rand, extraData);
		else
			return BlockModelUtils.retexturedQuads(state, modelState, unused -> originalModel, ourModel, side, rand, extraData);
	}

    @Override
    public boolean useAmbientOcclusion(BlockState state) {
        return originalModel.useAmbientOcclusion(state == null ? null : block.getModelState(state));
    }

    @Nonnull
    @Override
    public IModelData getModelData(@Nonnull BlockAndTintGetter level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData modelData)
    {
        return originalModel.getModelData(level, pos, block.getModelState(state), modelData);
    }
}
