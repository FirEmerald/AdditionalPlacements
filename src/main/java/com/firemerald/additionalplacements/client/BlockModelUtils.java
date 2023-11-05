package com.firemerald.additionalplacements.client;

import java.util.ArrayList;
import java.util.List;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public class BlockModelUtils
{
	public static BlockState getModeledState(BlockState state)
	{
		if (state.getBlock() instanceof AdditionalPlacementBlock) return ((AdditionalPlacementBlock<?>) state.getBlock()).getModelState(state);
		else return null;
	}

	public static final BakedModel getBakedModel(BlockState state)
	{
		return Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
	}

	public static final List<BakedQuad> getBakedQuads(BlockState referredState, Direction side, RandomSource rand)
	{
		BakedModel referredBakedModel = getBakedModel(referredState);
		List<BakedQuad> referredBakedQuads = new ArrayList<>();
		for (BakedQuad referredBakedQuad : referredBakedModel.getQuads(referredState, side, rand))
		{
			if (referredBakedQuad.getDirection() == side) referredBakedQuads.add(referredBakedQuad);
		}
		for (BakedQuad referredBakedQuad : referredBakedModel.getQuads(referredState, null, rand))
		{
			if (referredBakedQuad.getDirection() == side) referredBakedQuads.add(referredBakedQuad);
		}
		return referredBakedQuads;
	}

	public static final BakedQuad getNewBakedQuad(BakedQuad jsonBakedQuad, TextureAtlasSprite newSprite, int newTintIndex, Direction orientation)
	{
		return new BakedQuad(
				updateVertices(
						jsonBakedQuad.getVertices(),
						jsonBakedQuad.getSprite(),
						newSprite
						),
				newTintIndex,
				orientation,
				newSprite,
				jsonBakedQuad.isShade()
				);
	}

	public static final int X_OFFSET = 0;
	public static final int Y_OFFSET = 1;
	public static final int Z_OFFSET = 2;
	public static final int U_OFFSET = 4;
	public static final int V_OFFSET = 5;
	public static final int VERTEX_SIZE = DefaultVertexFormat.BLOCK.getIntegerSize();
	public static final float[] ZERO_POINT = {0, 0, 0};

	public static final float getFaceSize(int[] vertices)
	{
		float[] first = newVertex(vertices, 0);
		float[] prev = new float[3];
		float[] cur = newVertex(vertices, VERTEX_SIZE, first);
		float size = 0;
		for (int vertexIndex = VERTEX_SIZE * 2; vertexIndex < vertices.length; vertexIndex += VERTEX_SIZE)
		{
			float[] tmp = prev;
			prev = cur;
			cur = getVertex(vertices, vertexIndex, first, tmp);
			size += getArea(prev, cur);
		}
		return size;
	}

	public static float[] newVertex(int[] vertices, int vertexIndex)
	{
		return newVertex(vertices, vertexIndex, ZERO_POINT);
	}

	public static float[] newVertex(int[] vertices, int vertexIndex, float[] origin)
	{
		return new float[] {
			Float.intBitsToFloat(vertices[vertexIndex + X_OFFSET]) - origin[0],
			Float.intBitsToFloat(vertices[vertexIndex + Y_OFFSET]) - origin[1],
			Float.intBitsToFloat(vertices[vertexIndex + Z_OFFSET]) - origin[2]
		};
	}

	public static float[] getVertex(int[] vertices, int vertexIndex, float[] des)
	{
		return getVertex(vertices, vertexIndex, ZERO_POINT, des);
	}

	public static float[] getVertex(int[] vertices, int vertexIndex, float[] origin, float[] des)
	{
		des[0] = Float.intBitsToFloat(vertices[vertexIndex + X_OFFSET]) - origin[0];
		des[1] = Float.intBitsToFloat(vertices[vertexIndex + Y_OFFSET]) - origin[1];
		des[2] = Float.intBitsToFloat(vertices[vertexIndex + Z_OFFSET]) - origin[2];
		return des;
	}

	public static float getArea(float[] ab, float[] ac)
	{
		return .5f * Mth.sqrt(
				Mth.square(ab[0] * ac[1] - ab[1] * ac[0]) +
				Mth.square(ab[1] * ac[2] - ab[2] * ac[1]) +
				Mth.square(ab[2] * ac[0] - ab[0] * ac[2])
				);
	}

	public static final int[] updateVertices(int[] vertices, TextureAtlasSprite oldSprite, TextureAtlasSprite newSprite)
	{
		int[] updatedVertices = vertices.clone();
		for (int vertexIndex = 0; vertexIndex < vertices.length; vertexIndex += VERTEX_SIZE)
		{
			updatedVertices[vertexIndex + U_OFFSET] = changeUVertexElementSprite(oldSprite, newSprite, updatedVertices[vertexIndex + U_OFFSET]);
			updatedVertices[vertexIndex + V_OFFSET] = changeVVertexElementSprite(oldSprite, newSprite, updatedVertices[vertexIndex + V_OFFSET]);
	    }
		return updatedVertices;
	}

	private static final int changeUVertexElementSprite(TextureAtlasSprite oldSprite, TextureAtlasSprite newSprite, int vertex)
	{
		return Float.floatToRawIntBits(newSprite.getU(oldSprite.getUOffset(Float.intBitsToFloat(vertex))));
	}

	private static final int changeVVertexElementSprite(TextureAtlasSprite oldSprite, TextureAtlasSprite newSprite, int vertex)
	{
		return Float.floatToRawIntBits(newSprite.getV(oldSprite.getVOffset(Float.intBitsToFloat(vertex))));
	}
}