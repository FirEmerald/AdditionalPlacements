package com.firemerald.additionalplacements.client.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.block.interfaces.IPlacementBlock;
import com.firemerald.additionalplacements.client.BlockModelUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public class BakedPlacementBlockModel implements BakedModel
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
		AdditionalPlacementsMod.LOGGER.info("", new Exception());
		return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(MissingTextureAtlasSprite.getLocation());
	}

	public TextureAtlasSprite getParticleIcon(Function<BlockState, BakedModel> getModel, BlockState state)
	{
		AdditionalPlacementsMod.LOGGER.info(state.toString(), new Exception());
	    if (state.getBlock() instanceof AdditionalPlacementBlock) return getModel.apply(((AdditionalPlacementBlock<?>) state.getBlock()).getModelState(state)).getParticleIcon();
	    else return getParticleIcon();
	}

	@Override
	public ItemOverrides getOverrides()
	{
		return ItemOverrides.EMPTY;
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand)
	{
	    if (state.getBlock() instanceof AdditionalPlacementBlock)
	    {
	    	BlockState modelState = ((AdditionalPlacementBlock<?>) state.getBlock()).getModelState(state);
		    if (modelState != null)
		    {
		    	ModelKey modelKey = new ModelKey(modelState, side);
		    	if (!bakedQuadsCache.containsKey(modelKey))
		    	{
		    		Function<Direction, Direction> transformSide;
		    		if (side != null && state.getBlock() instanceof IPlacementBlock) transformSide = ((IPlacementBlock<?>) state.getBlock()).getModelDirectionFunction(state, rand);
		    		else transformSide = Function.identity();
	    			List<BakedQuad> bakedQuads = new ArrayList<>();
	    			for (BakedQuad jsonBakedQuad : model.getQuads(state, side, rand)) //finds sprite-tint pair that occurs over the highest area in this direction and applies it to the quad
	    			{
	    				Direction orientation = jsonBakedQuad.getDirection();
	    	    		Direction modelSide = orientation == null ? null : transformSide.apply(orientation);
	    		    	ModelKey reorientedModelKey = new ModelKey(modelState, modelSide);
	    				Pair<TextureAtlasSprite, Integer> texture;
	    				if (PlacementBlockModelLoader.TEXTURE_CACHE.containsKey(reorientedModelKey)) texture = PlacementBlockModelLoader.TEXTURE_CACHE.get(reorientedModelKey);
	    				else
	    				{
	    					Map<Pair<TextureAtlasSprite, Integer>, Double> weights = new HashMap<>();
	        				for (BakedQuad referredBakedQuad : BlockModelUtils.getBakedQuads(modelState, modelSide, rand))
	        				{
	        					Pair<TextureAtlasSprite, Integer> tex = Pair.of(referredBakedQuad.getSprite(), referredBakedQuad.getTintIndex());
	        					weights.put(tex, (weights.containsKey(tex) ? weights.get(tex) : 0) + BlockModelUtils.getFaceSize(referredBakedQuad.getVertices()));
	        				}
	    					texture = weights.entrySet().stream().max((e1, e2) -> (int) Math.signum(e2.getValue() - e1.getValue())).map(Map.Entry::getKey).orElse(null);
	    					PlacementBlockModelLoader.TEXTURE_CACHE.put(reorientedModelKey, texture);
	    				}
	    				if (texture != null) bakedQuads.add(BlockModelUtils.getNewBakedQuad(jsonBakedQuad, texture.getLeft(), texture.getRight(), orientation));
	    				else AdditionalPlacementsMod.LOGGER.warn(modelState + " has no texture for " + modelSide + ". No faces will be generated for " + orientation + ".");
	    			}
	    			bakedQuadsCache.put(modelKey, bakedQuads);
		    	}
		    	return bakedQuadsCache.get(modelKey);
		    }
	    }
	    return model.getQuads(state, side, rand);
	}

	@Override
	public ItemTransforms getTransforms()
	{
		return ItemTransforms.NO_TRANSFORMS;
	}

}
