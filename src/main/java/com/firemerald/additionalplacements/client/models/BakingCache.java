package com.firemerald.additionalplacements.client.models;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;

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

    private record SimpleUnbakedKey(ResourceLocation modelLocation, ModelState modelState, Function<Material, TextureAtlasSprite> sprites) {}

    private static final Map<SimpleUnbakedKey, BakedModel> SIMPLE_UNBAKED_CACHE = new HashMap<>();

    public static synchronized BakedModel getSimpleUnbaked(ResourceLocation modelLocation, ModelState modelState, Function<Material, TextureAtlasSprite> sprites, ModelBaker baker) {
        return SIMPLE_UNBAKED_CACHE.computeIfAbsent(new SimpleUnbakedKey(modelLocation, modelState, sprites), key -> Unwrapper.unwrap(baker.bake(key.modelLocation, key.modelState, sprites)));
    }

    public static void clearCache() {
        BAKED_CACHE.clear();
        SIMPLE_UNBAKED_CACHE.clear();
    }
}
