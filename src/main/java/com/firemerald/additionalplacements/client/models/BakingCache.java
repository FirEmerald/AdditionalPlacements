package com.firemerald.additionalplacements.client.models;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelBaker;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelBakerWrapper;

import java.util.HashMap;
import java.util.Map;

public class BakingCache {
    private record BakeKey(BlockStateModel.UnbakedRoot model, BlockState state, ModelBaker baker) {}

    private static final Map<BakeKey, BlockStateModel> BAKED_CACHE = new HashMap<>();

    public static synchronized BlockStateModel bake(BlockStateModel.UnbakedRoot model, BlockState state, ModelBaker baker) {
        //TODO enforce a cache in ModelBakery somehow?
        return BAKED_CACHE.computeIfAbsent(new BakeKey(model, state, baker), key -> Unwrapper.unwrap(key.model.bake(key.state, key.baker)));
    }

    private record SimpleUnbakedKey(ResourceLocation modelLocation, ModelState modelState) {}

    private static final Map<SimpleUnbakedKey, BlockStateModel> SIMPLE_UNBAKED_CACHE = new HashMap<>();

    public static synchronized BlockStateModel getSimpleUnbaked(ResourceLocation modelLocation, ModelState modelState, ModelBaker baker) {
        return SIMPLE_UNBAKED_CACHE.computeIfAbsent(new SimpleUnbakedKey(modelLocation, modelState), key -> Unwrapper.unwrap(new StandaloneModelBakerWrapper<>(key.modelLocation, StandaloneModelBaker.blockStateModel(key.modelState)).bake(baker)));
    }

    public static void clearCache() {
        BAKED_CACHE.clear();
        SIMPLE_UNBAKED_CACHE.clear();
    }
}
