package com.firemerald.additionalplacements.client.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.util.BlockRotation;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;

public class BakedPlacementModel extends PlacementModelWrapper
{
	private record ModelKey(AdditionalPlacementBlock<?> block, BakedModel ourModel, BakedModel theirModel, BlockRotation modelRotation) {}

	private static final Map<ModelKey, BakedPlacementModel> MODEL_CACHE = new HashMap<>();

	public static BakedPlacementModel of(ModelBaker bakery, ModelState modelTransform, AdditionalPlacementBlock<?> block, ResourceLocation ourModelLocation, ResourceLocation theirModelLocation, BlockRotation modelRotation, Function<Material, TextureAtlasSprite> sprites) {
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
	public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, @Nullable ModelData extraData, @Nullable RenderType renderType)
	{
		BlockState modelState = block.getModelState(state);
		if (block.rotatesModel(state))
			return BlockModelUtils.rotatedQuads(modelState, unused -> originalModel, modelRotation, block.rotatesTexture(state), side, rand, extraData, renderType);
		else
			return BlockModelUtils.retexturedQuads(state, modelState, unused -> originalModel, ourModel, side, rand, extraData, renderType);
	}

    @Override
    public boolean useAmbientOcclusion(BlockState state) {
        return originalModel.useAmbientOcclusion(state == null ? null : block.getModelState(state));
    }

    @Override
    public boolean useAmbientOcclusion(BlockState state, RenderType renderType) {
        return originalModel.useAmbientOcclusion(state == null ? null : block.getModelState(state), renderType);
    }

    @Nonnull
    @Override
    public ModelData getModelData(@Nonnull BlockAndTintGetter level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull ModelData modelData)
    {
        return originalModel.getModelData(level, pos, block.getModelState(state), modelData);
    }

    @Override
    public ChunkRenderTypeSet getRenderTypes(@Nonnull BlockState state, @Nonnull RandomSource rand, @Nonnull ModelData data)
    {
        return originalModel.getRenderTypes(block.getModelState(state), rand, data);
    }
}
