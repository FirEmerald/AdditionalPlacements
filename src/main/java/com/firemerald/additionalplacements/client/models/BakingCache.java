package com.firemerald.additionalplacements.client.models;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BakingCache {
    private record BakeKey(UnbakedModel model, ModelState modelState, Function<Material, TextureAtlasSprite> sprites, ResourceLocation modelLoc, ModelBaker baker) {}

    private static final Map<BakeKey, BakedModel> BAKED_CACHE = new HashMap<>();

    public static synchronized BakedModel bake(UnbakedModel model, ModelState modelState, Function<Material, TextureAtlasSprite> sprites, ResourceLocation modelLoc, ModelBaker baker) {
        //TODO enforce a cache in ModelBakery somehow?
        return BAKED_CACHE.computeIfAbsent(new BakeKey(model, modelState, sprites, modelLoc, baker), key -> Unwrapper.unwrap(key.model.bake(key.baker, key.sprites, key.modelState, key.modelLoc)));
    }

    public static BakedModel bake(UnbakedModel model, Function<Material, TextureAtlasSprite> sprites, ResourceLocation modelLoc, ModelBaker baker) {
        return bake(model, BlockModelRotation.X0_Y0, sprites, modelLoc, baker);
    }

    public static void clearCache() {
        BAKED_CACHE.clear();
    }
}
