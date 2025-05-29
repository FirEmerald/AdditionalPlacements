package com.firemerald.additionalplacements.client.models;

import java.util.*;
import java.util.function.Function;

import net.minecraft.client.renderer.RenderType;
import org.apache.commons.lang3.tuple.Pair;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.util.BlockRotation;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BlockModelUtils
{
	public static BlockState getModeledState(BlockState state)
	{
		if (state != null && state.getBlock() instanceof AdditionalPlacementBlock<?> block) return block.getModelState(state);
		else return state;
	}

	public static BakedQuad retexture(BakedQuad jsonBakedQuad, TextureAtlasSprite newSprite, int newTintIndex, int vertexSize, int uvOffset)
	{
		return new BakedQuad(
				updateVertices(
						jsonBakedQuad.getVertices(),
						jsonBakedQuad.getSprite(),
						newSprite,
						vertexSize,
						uvOffset
						),
				newTintIndex,
				jsonBakedQuad.getDirection(),
				newSprite,
				jsonBakedQuad.isShade()
				);
	}

	public static final float[] ZERO_POINT = {0, 0, 0};

	public static float getFaceSize(int[] vertices, int vertexSize, int posOffset)
	{
		float[] first = newVertex(vertices, 0, posOffset);
		float[] prev = new float[3];
		float[] cur = newVertex(vertices, vertexSize, first, posOffset);
		float size = 0;
		for (int vertexIndex = vertexSize * 2; vertexIndex < vertices.length; vertexIndex += vertexSize)
		{
			float[] tmp = prev;
			prev = cur;
			cur = getVertex(vertices, vertexIndex, first, posOffset, tmp);
			size += getArea(prev, cur);
		}
		return size;
	}

	public static float[] newVertex(int[] vertices, int vertexIndex, int posOffset)
	{
		return newVertex(vertices, vertexIndex, ZERO_POINT, posOffset);
	}

	public static float[] newVertex(int[] vertices, int vertexIndex, float[] origin, int posOffset)
	{
		return new float[] {
			Float.intBitsToFloat(vertices[vertexIndex + posOffset]) - origin[0],
			Float.intBitsToFloat(vertices[vertexIndex + posOffset + 1]) - origin[1],
			Float.intBitsToFloat(vertices[vertexIndex + posOffset + 2]) - origin[2]
		};
	}

	public static float[] getVertex(int[] vertices, int vertexIndex, int posOffset, float[] des)
	{
		return getVertex(vertices, vertexIndex, ZERO_POINT, posOffset, des);
	}

	public static float[] getVertex(int[] vertices, int vertexIndex, float[] origin, int posOffset, float[] des)
	{
		des[0] = Float.intBitsToFloat(vertices[vertexIndex + posOffset]) - origin[0];
		des[1] = Float.intBitsToFloat(vertices[vertexIndex + posOffset + 1]) - origin[1];
		des[2] = Float.intBitsToFloat(vertices[vertexIndex + posOffset + 2]) - origin[2];
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

	public static int[] updateVertices(int[] vertices, TextureAtlasSprite oldSprite, TextureAtlasSprite newSprite, int vertexSize, int uvOffset)
	{
		int[] updatedVertices = vertices.clone();
		for (int vertexIndex = uvOffset; vertexIndex < vertices.length; vertexIndex += vertexSize)
		{
			updatedVertices[vertexIndex] = changeUVertexElementSprite(oldSprite, newSprite, vertices[vertexIndex]);
			updatedVertices[vertexIndex + 1] = changeVVertexElementSprite(oldSprite, newSprite, vertices[vertexIndex + 1]);
	    }
		return updatedVertices;
	}

	private static int changeUVertexElementSprite(TextureAtlasSprite oldSprite, TextureAtlasSprite newSprite, int vertex)
	{
		return Float.floatToRawIntBits(newSprite.getU(oldSprite.getUOffset(Float.intBitsToFloat(vertex))));
	}

	private static int changeVVertexElementSprite(TextureAtlasSprite oldSprite, TextureAtlasSprite newSprite, int vertex)
	{
		return Float.floatToRawIntBits(newSprite.getV(oldSprite.getVOffset(Float.intBitsToFloat(vertex))));
	}

	public static int[] copyVertices(int[] originalData) {
		int[] newData = new int[originalData.length];
		System.arraycopy(originalData, 0, newData, 0, originalData.length); //direct copy
		return newData;
	}

	public static int[] copyVertices(int[] originalData, int vertexSize, int shiftLeft) {
		int[] newData = new int[originalData.length];
		//shiftLeft %= originalData.length / vertexSize;
		if (shiftLeft == 0) {
			System.arraycopy(originalData, 0, newData, 0, originalData.length); //direct copy
		} else {
			int lengthLeft = shiftLeft * vertexSize;
			int lengthRight = originalData.length - lengthLeft;
			System.arraycopy(originalData, lengthLeft, newData, 0, lengthRight); //copy [middle to end] to [start to middle]
			System.arraycopy(originalData, 0, newData, lengthRight, lengthLeft); //copy [start to middle] to [middle to end]
		}
		return newData;
	}

	public static Pair<TextureAtlasSprite, Integer> getSidedTexture(Direction fromSide, Function<Direction, List<BakedQuad>> getQuads, int vertexSize, int posOffset) {
		Map<Pair<TextureAtlasSprite, Integer>, Double> weights = new HashMap<>();
		List<BakedQuad> referenceQuads = getQuads.apply(fromSide);
		if (fromSide != null && (referenceQuads.isEmpty() || referenceQuads.stream().noneMatch(quad -> quad.getDirection() == fromSide))) //no valid culled sides
			referenceQuads = getQuads.apply(null); //all quads for this render type
		if (!referenceQuads.isEmpty()) {
			referenceQuads.forEach(referredBakedQuad -> {
				if (fromSide == null || referredBakedQuad.getDirection() == fromSide) { //only for quads facing the correct side
					Pair<TextureAtlasSprite, Integer> tex = Pair.of(referredBakedQuad.getSprite(), referredBakedQuad.getTintIndex());
					weights.merge(tex, (double) BlockModelUtils.getFaceSize(referredBakedQuad.getVertices(), vertexSize, posOffset), Double::sum);
				}
			});
			return weights.entrySet().stream().max((e1, e2) -> (int) Math.signum(e2.getValue() - e1.getValue())).map(Map.Entry::getKey).orElse(
					Pair.of(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(MissingTextureAtlasSprite.getLocation()), -1)
			);
		}
		else return Pair.of(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(MissingTextureAtlasSprite.getLocation()), -1);
	}

	public static List<BakedQuad> retexturedQuads(Direction side, Function<Direction, List<BakedQuad>> getOurQuads, Function<Direction, List<BakedQuad>> getTheirQuads, RenderType renderType)
	{
		VertexFormat format = renderType == null ? DefaultVertexFormat.BLOCK : renderType.format();
		int vertexSize = format.getIntegerSize();
		int posOffset = format.getOffset(format.getElements().indexOf(DefaultVertexFormat.ELEMENT_POSITION)) / 4;
		int uvOffset = format.getOffset(format.getElements().indexOf(DefaultVertexFormat.ELEMENT_UV)) / 4;
		@SuppressWarnings("unchecked")
		Pair<TextureAtlasSprite, Integer>[] textures = new Pair[6];
		List<BakedQuad> originalQuads = getOurQuads.apply(side);
		List<BakedQuad> bakedQuads = new ArrayList<>(originalQuads.size());
		for (BakedQuad originalQuad : originalQuads)
		{
			Direction modelSide = originalQuad.getDirection();
			int dirIndex = modelSide.get3DDataValue();
			Pair<TextureAtlasSprite, Integer> texture = textures[dirIndex];
			if (texture == null) texture = textures[dirIndex] = getSidedTexture(modelSide, getTheirQuads, vertexSize, posOffset);
    		bakedQuads.add(retexture(originalQuad, texture.getLeft(), texture.getRight(), vertexSize, uvOffset));
		}
		return bakedQuads;
	}

	public static List<BakedQuad> rotatedQuads(BlockRotation rotation, boolean rotateTex, Direction side, Function<Direction, List<BakedQuad>> getQuads, RenderType renderType)
	{
		VertexFormat format = renderType == null ? DefaultVertexFormat.BLOCK : renderType.format();
		int vertexSize = format.getIntegerSize();
		int posOffset = format.getOffset(format.getElements().indexOf(DefaultVertexFormat.ELEMENT_POSITION)) / 4;
		int uvOffset = format.getOffset(format.getElements().indexOf(DefaultVertexFormat.ELEMENT_UV)) / 4;
		List<BakedQuad> originalQuads = getQuads.apply(rotation.unapply(side));
		List<BakedQuad> bakedQuads = new ArrayList<>(originalQuads.size());
		for (BakedQuad originalQuad : originalQuads)
		{
    		bakedQuads.add(new BakedQuad(
    				rotation.applyVertices(originalQuad.getDirection(), originalQuad.getVertices(), vertexSize, posOffset, uvOffset, rotateTex, originalQuad.getSprite()),
    				originalQuad.getTintIndex(),
    				rotation.apply(originalQuad.getDirection()),
    				originalQuad.getSprite(),
    				originalQuad.isShade()
    				));
		}
		return bakedQuads;
	}
}