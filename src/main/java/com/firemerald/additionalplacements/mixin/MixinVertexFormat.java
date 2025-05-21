package com.firemerald.additionalplacements.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.firemerald.additionalplacements.client.IVertexFormatExtensions;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;

import it.unimi.dsi.fastutil.ints.IntList;

@Mixin(VertexFormat.class)
public class MixinVertexFormat implements IVertexFormatExtensions {
	@Final
	@Shadow
	private ImmutableList<VertexFormatElement> elements;
	@Final
	@Shadow
	private IntList offsets;

	@Override
	public int getIntOffset(VertexFormatElement element) {
		return offsets.getInt(elements.indexOf(element)) / 4;
	}
}
