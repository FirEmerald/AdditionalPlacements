package com.firemerald.dvsas.client.models;

import java.util.*;

import javax.annotation.Nonnull;

import com.firemerald.dvsas.client.VerticalBlockModelUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;

public class BakedVerticalBlockModel implements IDynamicBakedModel
{
	public final BakedModel model;
	private final Map<ModelKey, List<BakedQuad>> bakedQuadsCache = new HashMap<>();

	public BakedVerticalBlockModel(BakedModel model)
	{
		this.model = model;
	}

	@Override
	public boolean useAmbientOcclusion()
	{
		return true;
	}

	@Override
	public boolean isGui3d()
	{
		return false;
	}

	@Override
	public boolean usesBlockLight()
	{
		return true;
	}

	@Override
	public boolean isCustomRenderer()
	{
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public TextureAtlasSprite getParticleIcon()
	{
		return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(MissingTextureAtlasSprite.getLocation());
	}

	@Override
	public TextureAtlasSprite getParticleIcon(ModelData extraData)
	{
		BlockState modelState = extraData.get(VerticalBlockModelUtils.MODEL_STATE);
		if (modelState != null) return VerticalBlockModelUtils.getBakedModel(modelState).getParticleIcon(VerticalBlockModelUtils.getModelData(modelState, extraData));
		else return getParticleIcon();
	}


	@Override
	public @Nonnull ModelData getModelData(@Nonnull BlockAndTintGetter level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull ModelData modelData)
    {
		return ModelData.builder().with(VerticalBlockModelUtils.MODEL_STATE, VerticalBlockModelUtils.getModeledState(state)).build();
    }

	@Override
	public ItemOverrides getOverrides()
	{
		return ItemOverrides.EMPTY;
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData extraData, RenderType renderType)
	{
	    BlockState modelState = extraData.get(VerticalBlockModelUtils.MODEL_STATE);
	    if (modelState != null)
	    {
	    	ModelKey verticalSlabModelKey = new ModelKey(modelState, side, renderType);
	    	if (!bakedQuadsCache.containsKey(verticalSlabModelKey))
	    	{
	    		ModelData modelData = VerticalBlockModelUtils.getModelData(modelState, extraData);
    			List<BakedQuad> bakedQuads = new ArrayList<>();
    			for (BakedQuad jsonBakedQuad : model.getQuads(state, side, rand, modelData, renderType))
    			{
    				Direction orientation = jsonBakedQuad.getDirection();
    				for (BakedQuad referredBakedQuad : VerticalBlockModelUtils.getBakedQuads(modelState, orientation, rand, modelData, renderType))
    				{
    					if (!VerticalBlockModelUtils.isInternalFace(referredBakedQuad.getVertices()))
    					{
    						bakedQuads.add(VerticalBlockModelUtils.getNewBakedQuad(jsonBakedQuad, referredBakedQuad, orientation));
    						break;
    					}
    				}
    			}
    			bakedQuadsCache.put(verticalSlabModelKey, bakedQuads);
	    	}
	    	return bakedQuadsCache.get(verticalSlabModelKey);
	    }
	    return model.getQuads(state, side, rand, extraData, renderType);
	}

	@Override
	public ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource rand, ModelData extraData)
	{
	    BlockState modelState = extraData.get(VerticalBlockModelUtils.MODEL_STATE);
	    if (modelState != null)
	    {
    		BakedModel referredBakedModel = VerticalBlockModelUtils.getBakedModel(modelState);
    		if (referredBakedModel != null)
    		{
        		ModelData modelData = VerticalBlockModelUtils.getModelData(modelState, extraData);
        		return referredBakedModel.getRenderTypes(modelState, rand, modelData);
    		}
	    }
	    return model.getRenderTypes(state, rand, extraData);
	}
}
