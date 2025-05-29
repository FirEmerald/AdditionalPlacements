package com.firemerald.additionalplacements.client.models;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BakingCache {
    private record BakeKey(UnbakedModel model, ModelState modelState, Function<Material, TextureAtlasSprite> sprites, ModelBaker baker) {}

    private static final Map<BakeKey, BakedModel> BAKED_CACHE = new HashMap<>();

    public static synchronized BakedModel bake(UnbakedModel model, ModelState modelState, Function<Material, TextureAtlasSprite> sprites, ModelBaker baker) {
        //TODO enforce a cache in ModelBakery somehow?
        return BAKED_CACHE.computeIfAbsent(new BakeKey(model, modelState, sprites, baker), key -> Unwrapper.unwrap(key.model.bake(key.baker, sprites, modelState)));
    }

    public static BakedModel bake(UnbakedModel model, Function<Material, TextureAtlasSprite> sprites, ModelBaker baker) {
        return bake(model, BlockModelRotation.X0_Y0, sprites, baker);
    }

    public static void clearCache() {
        BAKED_CACHE.clear();
    }
}
