package com.firemerald.additionalplacements.client.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.interfaces.IPlacementBlock;
import com.firemerald.additionalplacements.client.BlockModelUtils;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

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
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;

public class BakedPlacementBlockModel implements IDynamicBakedModel
{
	public final BakedModel model;
	private final Map<ModelKey, List<BakedQuad>> bakedQuadsCache = new HashMap<>();

	public BakedPlacementBlockModel(BakedModel model)
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
	public ItemOverrides getOverrides()
	{
		return ItemOverrides.EMPTY;
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData extraData, RenderType renderType)
	{
	    BlockState modelState = extraData.get(BlockModelUtils.MODEL_STATE);
	    if (modelState != null)
	    {
	    	ModelKey modelKey = new ModelKey(modelState, side);
	    	if (!bakedQuadsCache.containsKey(modelKey))
	    	{
	    		Function<Direction, Direction> transformSide;
	    		if (side != null && state.getBlock() instanceof IPlacementBlock) transformSide = ((IPlacementBlock<?>) state.getBlock()).getModelDirectionFunction(state, rand, extraData);
	    		else transformSide = Function.identity();
	    		ModelData modelData = BlockModelUtils.getModelData(modelState, extraData);
    			List<BakedQuad> bakedQuads = new ArrayList<>();
    			VertexFormat format = renderType.format();
    			int vertexSize = format.getIntegerSize();
    			int posOffset = format.getOffset(format.getElements().indexOf(DefaultVertexFormat.ELEMENT_POSITION)) / 4;
    			int uvOffset = format.getOffset(format.getElements().indexOf(DefaultVertexFormat.ELEMENT_UV)) / 4;
    			for (BakedQuad jsonBakedQuad : model.getQuads(state, side, rand, modelData, null)) //finds sprite-tint pair that occurs over the highest area in this direction and applies it to the quad
    			{
    				Direction orientation = jsonBakedQuad.getDirection();
    	    		Direction modelSide = orientation == null ? null : transformSide.apply(orientation);
    		    	ModelKey reorientedModelKey = new ModelKey(modelState, modelSide);
    				Pair<TextureAtlasSprite, Integer> texture;
    				if (PlacementBlockModelLoader.TEXTURE_CACHE.containsKey(reorientedModelKey)) texture = PlacementBlockModelLoader.TEXTURE_CACHE.get(reorientedModelKey);
    				else
    				{
    					Map<Pair<TextureAtlasSprite, Integer>, Double> weights = new HashMap<>();
        				for (BakedQuad referredBakedQuad : BlockModelUtils.getBakedQuads(modelState, modelSide, rand, modelData, renderType))
        				{
        					Pair<TextureAtlasSprite, Integer> tex = Pair.of(referredBakedQuad.getSprite(), referredBakedQuad.getTintIndex());
        					weights.put(tex, (weights.containsKey(tex) ? weights.get(tex) : 0) + BlockModelUtils.getFaceSize(referredBakedQuad.getVertices(), vertexSize, posOffset));
        				}
    					texture = weights.entrySet().stream().max((e1, e2) -> (int) Math.signum(e2.getValue() - e1.getValue())).map(Map.Entry::getKey).orElse(null);
    					PlacementBlockModelLoader.TEXTURE_CACHE.put(reorientedModelKey, texture);
    				}
    				if (texture != null) bakedQuads.add(BlockModelUtils.getNewBakedQuad(jsonBakedQuad, texture.getLeft(), texture.getRight(), orientation, vertexSize, uvOffset));
    				else AdditionalPlacementsMod.LOGGER.warn(modelState + " has no texture for " + modelSide + ". No faces will be generated for " + orientation + ".");
    			}
    			bakedQuadsCache.put(modelKey, bakedQuads);
	    	}
	    	return bakedQuadsCache.get(modelKey);
	    }
	    return model.getQuads(state, side, rand, extraData, renderType);
	}

	@Override
	public ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource rand, ModelData extraData)
	{
	    BlockState modelState = BlockModelUtils.getModeledState(state);
	    if (modelState != null)
	    {
    		BakedModel referredBakedModel = BlockModelUtils.getBakedModel(modelState);
    		if (referredBakedModel != null)
    		{
        		ModelData modelData = BlockModelUtils.getModelData(modelState, extraData);
        		return referredBakedModel.getRenderTypes(modelState, rand, modelData);
    		}
	    }
	    return model.getRenderTypes(state, rand, extraData);
	}
}
