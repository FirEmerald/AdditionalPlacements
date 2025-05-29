package com.firemerald.additionalplacements.client.models;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperBlockStateModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public abstract class PlacementModelWrapper extends WrapperBlockStateModel
{
	protected PlacementModelWrapper(BlockStateModel originalModel)
	{
		super(originalModel);
	}

	@Override
	public void collectParts(RandomSource random, List<BlockModelPart> parts) {
		//TODO assumes that the added parts are equivalent to collectParts(RandomSource). Additional code may be needed to account for potential other cases.
		wrapParts(random).forEach(parts::add);
	}

	@Override
	public @NotNull List<BlockModelPart> collectParts(RandomSource random) {
		return wrapParts(random).toList();
	}

	@Override
	public void emitQuads(QuadEmitter emitter, BlockAndTintGetter blockView, BlockPos pos, BlockState state, RandomSource random, Predicate<@Nullable Direction> cullTest) {
		wrapParts(random).forEach(part -> part.emitQuads(emitter, cullTest));
	}

	protected abstract Stream<BlockModelPart> wrapParts(RandomSource random);
}
