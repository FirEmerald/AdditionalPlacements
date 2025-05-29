package com.firemerald.additionalplacements.client.models;

import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class BakingCache {
    private static final class BakeKey {
        private final IUnbakedModel model;
        private final IModelTransform modelState;
        private final Function<RenderMaterial, TextureAtlasSprite> sprites;
        private final ResourceLocation modelLoc;
        private final ModelBakery baker;

        private BakeKey(IUnbakedModel model, IModelTransform modelState, Function<RenderMaterial, TextureAtlasSprite> sprites, ResourceLocation modelLoc, ModelBakery baker) {
            this.model = model;
            this.modelState = modelState;
            this.sprites = sprites;
            this.modelLoc = modelLoc;
            this.baker = baker;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            BakeKey that = (BakeKey) obj;
            return Objects.equals(this.model, that.model) &&
                    Objects.equals(this.modelState, that.modelState) &&
                    Objects.equals(this.sprites, that.sprites) &&
                    Objects.equals(this.modelLoc, that.modelLoc) &&
                    Objects.equals(this.baker, that.baker);
        }

        @Override
        public int hashCode() {
            return Objects.hash(model, modelState, sprites, modelLoc, baker);
        }

        @Override
        public String toString() {
            return "BakeKey[model=" + model + ", modelState=" + modelState + ", sprites=" + sprites + ", modelLoc=" + modelLoc + ", baker=" + baker + ']';
        }
    }

    private static final Map<BakeKey, IBakedModel> BAKED_CACHE = new HashMap<>();

    public static synchronized IBakedModel bake(IUnbakedModel model, IModelTransform modelState, Function<RenderMaterial, TextureAtlasSprite> sprites, ResourceLocation modelLoc, ModelBakery baker) {
        //TODO enforce a cache in ModelBakery somehow?
        return BAKED_CACHE.computeIfAbsent(new BakeKey(model, modelState, sprites, modelLoc, baker), key -> Unwrapper.unwrap(key.model.bake(key.baker, key.sprites, key.modelState, key.modelLoc)));
    }

    public static IBakedModel bake(IUnbakedModel model, Function<RenderMaterial, TextureAtlasSprite> sprites, ResourceLocation modelLoc, ModelBakery baker) {
        return bake(model, ModelRotation.X0_Y0, sprites, modelLoc, baker);
    }

    public static void clearCache() {
        BAKED_CACHE.clear();
    }
}
