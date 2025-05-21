package com.firemerald.additionalplacements.client.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.util.BlockRotation;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.UnbakedBlockStateModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.common.util.TriState;

public class BakedPlacementModel extends PlacementModelWrapper
{
	private record ModelKey(AdditionalPlacementBlock<?> block, BakedModel ourModel, BakedModel theirModel, BlockRotation modelRotation) {}

	private static final Map<ModelKey, BakedPlacementModel> MODEL_CACHE = new HashMap<>();

	public static BakedPlacementModel of(ModelBaker bakery, ModelState modelTransform, AdditionalPlacementBlock<?> block, ResourceLocation ourModelLocation, UnbakedBlockStateModel theirModel, BlockRotation modelRotation) {
		return of(block,
				Unwrapper.unwrap(bakery.bake(ourModelLocation, modelTransform)),
				Unwrapper.unwrap(theirModel.bake(bakery)),
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
			return BlockModelUtils.rotatedQuads(modelState, unused -> parent, modelRotation, block.rotatesTexture(state), side, rand, extraData, renderType);
		else
			return BlockModelUtils.retexturedQuads(state, modelState, unused -> parent, ourModel, side, rand, extraData, renderType);
	}

    @Override
    public TriState useAmbientOcclusion(BlockState state, ModelData extraData, RenderType renderType)
    {
        return parent.useAmbientOcclusion(state == null ? null : block.getModelState(state), extraData, renderType);
    }

    @NotNull
    @Override
    public ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData modelData)
    {
        return parent.getModelData(level, pos, block.getModelState(state), modelData);
    }

    @Override
    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data)
    {
        return parent.getRenderTypes(block.getModelState(state), rand, data);
    }
}
