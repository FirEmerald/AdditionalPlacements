package com.firemerald.additionalplacements.client.models;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.TriState;
import net.minecraft.world.level.block.state.BlockState;
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

    @Override
    public @NotNull RenderType getRenderType(@NotNull BlockState state) {
        return wrapped.getRenderType(state);
    }

    @Override
    public @NotNull TriState ambientOcclusion() {
        return wrapped.ambientOcclusion();
    }
}
