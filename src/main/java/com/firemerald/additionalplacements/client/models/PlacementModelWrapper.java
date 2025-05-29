package com.firemerald.additionalplacements.client.models;

import java.util.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.IModelData;

public abstract class PlacementModelWrapper extends BakedModelWrapper<IBakedModel>
{
	public PlacementModelWrapper(IBakedModel originalModel)
	{
		super(originalModel);
	}

    @Override
    public abstract List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand);

    @Override
    public abstract List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand, @Nullable IModelData extraData);

    @Override
    public boolean isAmbientOcclusion(BlockState state) {
        return originalModel.isAmbientOcclusion(BlockModelUtils.getModeledState(state));
    }

    @Nonnull
    @Override
    public IModelData getModelData(@Nonnull IBlockDisplayReader level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData modelData) {
        return originalModel.getModelData(level, pos, BlockModelUtils.getModeledState(state), modelData);
    }
}
