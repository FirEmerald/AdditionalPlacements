package com.firemerald.additionalplacements.client.models;

import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.jetbrains.annotations.NotNull;

public abstract class WrappedBlockModelPart implements BlockModelPart {
    public final BlockModelPart wrapped;

    public WrappedBlockModelPart(BlockModelPart wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return wrapped.useAmbientOcclusion();
    }

    @Override
    public @NotNull TextureAtlasSprite particleIcon() {
        return wrapped.particleIcon();
    }
}
