package com.firemerald.additionalplacements.client.models;

import com.firemerald.additionalplacements.util.BlockRotation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RotatedBlockModelPart extends WrappedBlockModelPart {
    public final BlockRotation modelRotation;
    public final boolean rotatesTexture;

    public RotatedBlockModelPart(BlockModelPart wrapped, BlockRotation modelRotation, boolean rotatesTexture) {
        super(wrapped);
        this.modelRotation = modelRotation;
        this.rotatesTexture = rotatesTexture;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable Direction direction) {
        return BlockModelUtils.rotatedQuads(wrapped, modelRotation, rotatesTexture, direction);
    }
}
