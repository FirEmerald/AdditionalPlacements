package com.firemerald.additionalplacements.client.models;

import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

import com.firemerald.additionalplacements.client.BlockModelUtils;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.IModelData;

public class BakedRetexturedBlockModel extends BakedModelWrapper<BakedModel>
{
	private static final Map<Pair<BakedModel, BakedModel>, BakedRetexturedBlockModel> CACHE = new HashMap<>();

	public synchronized static BakedRetexturedBlockModel of(BakedModel originalModel, BakedModel newModel)
	{
		return CACHE.computeIfAbsent(Pair.of(originalModel, newModel), k -> new BakedRetexturedBlockModel(originalModel, newModel));
	}

	public static void clearCache()
	{
		CACHE.clear();
	}
	
	private final BakedModel newModel;

	private BakedRetexturedBlockModel(BakedModel originalModel, BakedModel newModel)
	{
		super(originalModel);
		this.newModel = newModel;
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand, IModelData extraData)
	{
		return BlockModelUtils.retexturedQuads(state, BlockModelUtils.getModeledState(state), (unused) -> originalModel, newModel, side, rand, extraData);
	}
}
