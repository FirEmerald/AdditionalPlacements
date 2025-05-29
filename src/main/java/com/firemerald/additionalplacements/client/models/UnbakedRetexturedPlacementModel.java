package com.firemerald.additionalplacements.client.models;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import java.util.*;
import java.util.function.Function;

public class UnbakedRetexturedPlacementModel implements IUnbakedModel
{
    private static final class ModelKey {
        private final ResourceLocation ourModelLocation;
        private final IModelTransform ourModelState;
        private final ResourceLocation theirModelLocation;
        private final IUnbakedModel theirModel;

        private ModelKey(ResourceLocation ourModelLocation, IModelTransform ourModelState, ResourceLocation theirModelLocation, IUnbakedModel theirModel) {
            this.ourModelLocation = ourModelLocation;
            this.ourModelState = ourModelState;
            this.theirModelLocation = theirModelLocation;
            this.theirModel = theirModel;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            ModelKey that = (ModelKey) obj;
            return Objects.equals(this.ourModelLocation, that.ourModelLocation) &&
                    Objects.equals(this.ourModelState, that.ourModelState) &&
                    Objects.equals(this.theirModelLocation, that.theirModelLocation) &&
                    Objects.equals(this.theirModel, that.theirModel);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ourModelLocation, ourModelState, theirModelLocation, theirModel);
        }

        @Override
        public String toString() {
            return "ModelKey[ourModelLocation=" + ourModelLocation + ", ourModelState=" + ourModelState + ", theirModelLocation=" + theirModelLocation + ", theirModel=" + theirModel + ']';
        }
    }

	private static final Map<ModelKey, UnbakedRetexturedPlacementModel> MODEL_CACHE = new HashMap<>();

	public static UnbakedRetexturedPlacementModel of(ResourceLocation ourModelLocation, IModelTransform ourModelState, ResourceLocation theirModelLocation, IUnbakedModel theirModel) {
		return MODEL_CACHE.computeIfAbsent(new ModelKey(ourModelLocation, ourModelState, theirModelLocation, theirModel), UnbakedRetexturedPlacementModel::new);
	}

	public static void clearCache() {
		MODEL_CACHE.clear();
	}

	public final ResourceLocation ourModelLocation;
	public final IModelTransform ourModelState;
	public final ResourceLocation theirModelLocation;
	public final IUnbakedModel theirModel;
	private IUnbakedModel ourModel;

	private UnbakedRetexturedPlacementModel(ModelKey key)
	{
		this.ourModelLocation = key.ourModelLocation;
		this.ourModelState = key.ourModelState;
		this.theirModelLocation = key.theirModelLocation;
		this.theirModel = key.theirModel;
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return Collections.singleton(ourModelLocation);
	}

	@Override
	public Collection<RenderMaterial> getMaterials(Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
		return (ourModel = modelGetter.apply(ourModelLocation)).getMaterials(modelGetter, missingTextureErrors);
	}

	@Override
	public IBakedModel bake(ModelBakery baker, Function<RenderMaterial, TextureAtlasSprite> sprites, IModelTransform modelState, ResourceLocation modelLocation) {
		return BakedRetexturedPlacementModel.of(BakingCache.bake(ourModel, ourModelState, sprites, ourModelLocation, baker), BakingCache.bake(theirModel, sprites, theirModelLocation, baker));
	}
}
