package com.firemerald.additionalplacements.client.models;

import java.util.List;

import javax.annotation.Nonnull;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.client.BlockModelUtils;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;

public class BakedPlacementBlockModel extends BakedModelWrapper<BakedModel>
{
	public BakedPlacementBlockModel(BakedModel model)
	{
		super(model);
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData extraData, RenderType renderType)
	{
		BlockState modelState = extraData.get(BlockModelUtils.MODEL_STATE);
		if (modelState == null) modelState = BlockModelUtils.getModeledState(state);
		if (state.getBlock() instanceof AdditionalPlacementBlock) {
			AdditionalPlacementBlock<?> block = (AdditionalPlacementBlock<?>) state.getBlock();
			if (block.rotatesModel(state)) return BlockModelUtils.rotatedQuads(modelState, BlockModelUtils::getBakedModel, block.getRotation(state), block.rotatesTexture(state), side, rand, extraData, renderType);
		}
		return BlockModelUtils.retexturedQuads(state, modelState, BlockModelUtils::getBakedModel, originalModel, side, rand, extraData, renderType);
	}

    @Override
    public boolean useAmbientOcclusion(BlockState state)
    {
    	BlockState modelState = BlockModelUtils.getModeledState(state);
        return BlockModelUtils.getBakedModel(modelState).useAmbientOcclusion(modelState);
    }

    @Override
    public boolean useAmbientOcclusion(BlockState state, RenderType renderType)
    {
    	BlockState modelState = BlockModelUtils.getModeledState(state);
        return BlockModelUtils.getBakedModel(modelState).useAmbientOcclusion(modelState, renderType);
    }

	@Override
	public TextureAtlasSprite getParticleIcon(ModelData extraData)
	{
		BlockState modelState = extraData.get(BlockModelUtils.MODEL_STATE);
		if (modelState != null) return BlockModelUtils.getBakedModel(modelState).getParticleIcon(BlockModelUtils.getModelData(modelState, extraData));
		else return getParticleIcon();
	}

	@Override
	public @Nonnull ModelData getModelData(@Nonnull BlockAndTintGetter level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull ModelData modelData)
    {
		return ModelData.builder().with(BlockModelUtils.MODEL_STATE, BlockModelUtils.getModeledState(state)).build();
    }

	@Override
	public ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource rand, ModelData extraData)
	{
    	BlockState modelState = BlockModelUtils.getModeledState(state);
		return BlockModelUtils.getBakedModel(modelState).getRenderTypes(modelState, rand, BlockModelUtils.getModelData(modelState, extraData));
	}
	
	public BakedModel originalModel() {
		return originalModel;
	}
}
