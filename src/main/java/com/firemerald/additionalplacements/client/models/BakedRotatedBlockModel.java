package com.firemerald.additionalplacements.client.models;

import java.util.*;

import org.apache.commons.lang3.tuple.Triple;

import com.firemerald.additionalplacements.client.BlockModelUtils;
import com.firemerald.additionalplacements.util.BlockRotation;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;

public class BakedRotatedBlockModel extends BakedModelWrapper<BakedModel>
{
	private static final Map<Triple<BakedModel, BlockRotation, Boolean>, BakedRotatedBlockModel> CACHE = new HashMap<>();

	public synchronized static BakedRotatedBlockModel of(BakedModel model, BlockRotation rotation, boolean rotateTex)
	{
		return CACHE.computeIfAbsent(Triple.of(model, rotation, rotateTex), k -> new BakedRotatedBlockModel(model, rotation, rotateTex));
	}

	public static void clearCache()
	{
		CACHE.clear();
	}
	
	private final BlockRotation rotation;
	private final boolean rotateTex;

	private BakedRotatedBlockModel(BakedModel model, BlockRotation rotation, boolean rotateTex)
	{
		super(model);
		this.rotation = rotation;
		this.rotateTex = rotateTex;
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData extraData, RenderType renderType)
	{
		return BlockModelUtils.rotatedQuads(BlockModelUtils.getModeledState(state), unused -> originalModel, rotation, rotateTex, side, rand, extraData, renderType);
	}
}
