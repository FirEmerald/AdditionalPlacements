package com.firemerald.additionalplacements.client.models;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.IModelData;

public abstract class PlacementModelWrapper extends BakedModelWrapper<BakedModel>
{
	public PlacementModelWrapper(BakedModel originalModel)
	{
		super(originalModel);
	}

    @Override
    public abstract List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand);

    @Override
    public abstract List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand, @Nullable IModelData extraData);

    @Override
    public boolean useAmbientOcclusion(BlockState state) {
        return originalModel.useAmbientOcclusion(BlockModelUtils.getModeledState(state));
    }

    @Nonnull
    @Override
    public IModelData getModelData(@Nonnull BlockAndTintGetter level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData modelData) {
        return originalModel.getModelData(level, pos, BlockModelUtils.getModeledState(state), modelData);
    }
}
