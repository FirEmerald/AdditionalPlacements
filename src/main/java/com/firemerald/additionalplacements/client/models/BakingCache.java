package com.firemerald.additionalplacements.client.models;

import net.minecraft.client.renderer.block.model.UnbakedBlockStateModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class BakingCache {
    private record BakeKey(UnbakedBlockStateModel model, ModelBaker baker) {}

    private static final Map<BakeKey, BakedModel> BAKED_CACHE = new HashMap<>();

    public static synchronized BakedModel bake(UnbakedBlockStateModel model, ModelBaker baker) {
        //TODO enforce a cache in ModelBakery somehow?
        return BAKED_CACHE.computeIfAbsent(new BakeKey(model, baker), key -> Unwrapper.unwrap(key.model.bake(key.baker)));
    }

    private record SimpleUnbakedKey(ResourceLocation modelLocation, ModelState modelState) {}

    private static final Map<SimpleUnbakedKey, BakedModel> SIMPLE_UNBAKED_CACHE = new HashMap<>();

    public static synchronized BakedModel getSimpleUnbaked(ResourceLocation modelLocation, ModelState modelState, ModelBaker baker) {
        return SIMPLE_UNBAKED_CACHE.computeIfAbsent(new SimpleUnbakedKey(modelLocation, modelState), key -> Unwrapper.unwrap(baker.bake(key.modelLocation, key.modelState)));
    }

    public static void clearCache() {
        BAKED_CACHE.clear();
        SIMPLE_UNBAKED_CACHE.clear();
    }
}
