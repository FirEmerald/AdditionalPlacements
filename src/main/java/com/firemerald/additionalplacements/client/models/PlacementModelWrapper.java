package com.firemerald.additionalplacements.client.models;

import java.util.List;
import java.util.stream.Stream;

import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.DelegateBlockStateModel;
import org.jetbrains.annotations.NotNull;

import net.minecraft.util.RandomSource;

public abstract class PlacementModelWrapper extends DelegateBlockStateModel {
    public PlacementModelWrapper(BlockStateModel originalModel)
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

    protected abstract Stream<BlockModelPart> wrapParts(RandomSource random);

    @Override
    public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockModelPart> parts) {
        //TODO assumes that the added parts are equivalent to collectParts(RandomSource). Additional code may be needed to account for potential other cases.
        wrapParts(level, pos, state, random).forEach(parts::add);
    }

    @Override
    public @NotNull List<BlockModelPart> collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random) {
        return wrapParts(level, pos, state, random).toList();
    }

    protected abstract Stream<BlockModelPart> wrapParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random);
}
