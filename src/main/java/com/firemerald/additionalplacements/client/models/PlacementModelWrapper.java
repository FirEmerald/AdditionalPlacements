package com.firemerald.additionalplacements.client.models;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class PlacementModelWrapper extends ForwardingBakedModel
{
	protected PlacementModelWrapper(BakedModel originalModel)
	{
		this.wrapped = originalModel;
	}

	@Override
	public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
		context.bakedModelConsumer().accept(this, state);
	}

	@Override
	public boolean isVanillaAdapter() {
		return true;
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
		context.bakedModelConsumer().accept(this, null);
	}

	@Override
	public abstract List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, Random random);
}
