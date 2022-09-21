package com.firemerald.dvsas.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.firemerald.dvsas.DVSaSMod;
import com.firemerald.dvsas.block.VerticalBlock;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;

public class VerticalBlockModelUtils
{
	public static final ModelProperty<BlockState> MODEL_STATE = new ModelProperty<>();

	public static BlockState getModeledState(BlockState state)
	{
		if (state.getBlock() instanceof VerticalBlock) return ((VerticalBlock<?>) state.getBlock()).getModelState(state);
		else return null;
	}

	public static final BakedModel getBakedModel(BlockState state)
	{
		return Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
	}

	public static final IModelData getModelData(BlockState blockState, IModelData defaultData)
	{
		return blockState.hasBlockEntity() ? ((EntityBlock) blockState.getBlock()).newBlockEntity(new BlockPos(0, 0, 0), blockState).getModelData() : defaultData;
	}

	public static final List<BakedQuad> getBakedQuads(BlockState referredState, Direction side, Random rand, IModelData modelData)
	{
		BakedModel referredBakedModel = getBakedModel(referredState);
		IModelData referredModelData = getModelData(referredState, modelData);
		List<BakedQuad> referredBakedQuads = new ArrayList<>(referredBakedModel.getQuads(referredState, side, rand, referredModelData));
		for (BakedQuad referredBakedQuad : referredBakedModel.getQuads(referredState, null, rand, referredModelData))
		{
			if (referredBakedQuad.getDirection() == side) referredBakedQuads.add(referredBakedQuad);
		}
		if (referredBakedQuads.size() == 0)
		{
			DVSaSMod.LOGGER.warn("Block has no texture for " + side + " face. No texture will be generated for that face.");
		}
		return referredBakedQuads;
	}

	public static final BakedQuad getNewBakedQuad(BakedQuad jsonBakedQuad, BakedQuad referredBakedQuad, Direction orientation)
	{
		return new BakedQuad(
				updateVertices(
						jsonBakedQuad.getVertices(),
						referredBakedQuad.getVertices(),
						jsonBakedQuad.getSprite(),
						referredBakedQuad.getSprite()
						),
				referredBakedQuad.getTintIndex(),
				orientation,
				referredBakedQuad.getSprite(),
				jsonBakedQuad.isShade()
				);
	}

	public static final short X_OFFSET = 0;
	public static final short Y_OFFSET = 1;
	public static final short Z_OFFSET = 2;
	public static final short U_OFFSET = 4;
	public static final short V_OFFSET = 5;
	public static final int VERTEX_COUNT = DefaultVertexFormat.BLOCK.getVertexSize();
	public static final int VERTEX_SIZE = DefaultVertexFormat.BLOCK.getIntegerSize();

	public static final boolean isInternalFace(int[] vertices)
	{
		for (int vertexIndex = 0; vertexIndex < VERTEX_COUNT; vertexIndex += VERTEX_SIZE)
		{
			if (!isValidCoordinate(Float.intBitsToFloat(vertices[vertexIndex + X_OFFSET])) || !isValidCoordinate(Float.intBitsToFloat(vertices[vertexIndex + Y_OFFSET])) || !isValidCoordinate(Float.intBitsToFloat(vertices[vertexIndex + Z_OFFSET]))) return true;
		}
		return false;
	}

	public static boolean isValidCoordinate(float coord)
	{
		return isValid(coord) || isValid(coord - 0.5f) || isValid(coord - 1);
	}

	public static boolean isValid(float value)
	{
		return Math.abs(value) < .00001f;
	}

	public static final int[] updateVertices(int[] vertices, int[] referredVertices, TextureAtlasSprite oldSprite, TextureAtlasSprite newSprite)
	{
		int[] updatedVertices = vertices.clone();
		for (int vertexIndex = 0; vertexIndex < VERTEX_COUNT; vertexIndex += VERTEX_SIZE)
		{
			updatedVertices[vertexIndex + U_OFFSET] = changeUVertexElementSprite(oldSprite, newSprite, updatedVertices[vertexIndex + U_OFFSET]);
			updatedVertices[vertexIndex + V_OFFSET] = changeVVertexElementSprite(oldSprite, newSprite, updatedVertices[vertexIndex + V_OFFSET]);
	    }
		return updatedVertices;
	}

	private static final int changeUVertexElementSprite(TextureAtlasSprite oldSprite, TextureAtlasSprite newSprite, int vertex)
	{
		return Float.floatToRawIntBits(newSprite.getU(getUV(Float.intBitsToFloat(vertex), oldSprite.getU0(), oldSprite.getU1())));
	}

	private static final int changeVVertexElementSprite(TextureAtlasSprite oldSprite, TextureAtlasSprite newSprite, int vertex)
	{
		return Float.floatToRawIntBits(newSprite.getV(getUV(Float.intBitsToFloat(vertex), oldSprite.getV0(), oldSprite.getV1())));
	}

	private static final double getUV(float uv, float uv0, float uv1)
	{
		return (double) (uv - uv0) * 16.0F / (uv1 - uv0);
	}
}