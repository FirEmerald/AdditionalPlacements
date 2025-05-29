package com.firemerald.additionalplacements.client.models;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RetexturedBlockModelPart extends WrappedBlockModelPart {
    public final List<BlockModelPart> originalModel;

    public RetexturedBlockModelPart(BlockModelPart wrapped, List<BlockModelPart> originalModel) {
        super(wrapped);
        this.originalModel = originalModel;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable Direction direction) {
        return BlockModelUtils.retexturedQuads(originalModel, wrapped, direction);
    }
}
