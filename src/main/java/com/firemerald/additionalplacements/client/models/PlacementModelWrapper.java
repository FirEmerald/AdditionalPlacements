package com.firemerald.additionalplacements.client.models;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.client.renderer.block.model.BakedQuad;
import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.impl.renderer.VanillaModelEncoder;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.DelegateBakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public abstract class PlacementModelWrapper extends DelegateBakedModel
{
	protected PlacementModelWrapper(BakedModel originalModel)
	{
		super(originalModel);
	}

	@Override
	public void emitBlockQuads(QuadEmitter emitter, BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, Predicate<@Nullable Direction> cullTest) {
		VanillaModelEncoder.emitBlockQuads(emitter, this, state, randomSupplier, cullTest);
	}

	@Override
	public boolean isVanillaAdapter() {
		return true;
	}

	@Override
	public void emitItemQuads(QuadEmitter emitter, Supplier<RandomSource> randomSupplier) {
		VanillaModelEncoder.emitItemQuads(emitter, this, null, randomSupplier);
	}

	@Override
	public abstract List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random);
}
