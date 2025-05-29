package com.firemerald.additionalplacements.client.models;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraftforge.client.ChunkRenderTypeSet;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;

import javax.annotation.Nonnull;

public abstract class PlacementModelWrapper extends BakedModelWrapper<BakedModel>
{
	public PlacementModelWrapper(BakedModel model)
	{
		super(model);
	}

    @Override
    public abstract List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand);

    @Override
    public abstract List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, @Nullable ModelData extraData, @Nullable RenderType renderType);

    @Override
    public boolean useAmbientOcclusion(BlockState state) {
        return originalModel.useAmbientOcclusion(BlockModelUtils.getModeledState(state));
    }

    public boolean useAmbientOcclusion(BlockState state, RenderType renderType) {
        return originalModel.useAmbientOcclusion(BlockModelUtils.getModeledState(state), renderType);
    }

    @Nonnull
    @Override
    public ModelData getModelData(@Nonnull BlockAndTintGetter level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull ModelData modelData) {
        return originalModel.getModelData(level, pos, BlockModelUtils.getModeledState(state), modelData);
    }

    @Override
    public ChunkRenderTypeSet getRenderTypes(@Nonnull BlockState state, @Nonnull RandomSource rand, @Nonnull ModelData data) {
        return originalModel.getRenderTypes(BlockModelUtils.getModeledState(state), rand, data);
    }
}
