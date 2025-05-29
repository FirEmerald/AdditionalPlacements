package com.firemerald.additionalplacements.client.models;

import com.firemerald.additionalplacements.util.BlockRotation;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import java.util.*;
import java.util.function.Function;

public class UnbakedRotatedPlacementModel implements IUnbakedModel
{
    private static final class ModelKey {
        private final ResourceLocation theirModelLocation;
        private final IUnbakedModel theirModel;
        private final BlockRotation theirModelRotation;
        private final boolean rotatesTexture;

        private ModelKey(ResourceLocation theirModelLocation, IUnbakedModel theirModel, BlockRotation theirModelRotation, boolean rotatesTexture) {
            this.theirModelLocation = theirModelLocation;
            this.theirModel = theirModel;
            this.theirModelRotation = theirModelRotation;
            this.rotatesTexture = rotatesTexture;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            ModelKey that = (ModelKey) obj;
            return Objects.equals(this.theirModelLocation, that.theirModelLocation) &&
                    Objects.equals(this.theirModel, that.theirModel) &&
                    Objects.equals(this.theirModelRotation, that.theirModelRotation) &&
                    this.rotatesTexture == that.rotatesTexture;
        }

        @Override
        public int hashCode() {
            return Objects.hash(theirModelLocation, theirModel, theirModelRotation, rotatesTexture);
        }

        @Override
        public String toString() {
            return "ModelKey[theirModelLocation=" + theirModelLocation + ", theirModel=" + theirModel + ", theirModelRotation=" + theirModelRotation + ", rotatesTexture=" + rotatesTexture + ']';
        }
    }

	private static final Map<ModelKey, UnbakedRotatedPlacementModel> MODEL_CACHE = new HashMap<>();

	public static UnbakedRotatedPlacementModel of(ResourceLocation theirModelLocation, IUnbakedModel theirModel, BlockRotation theirModelRotation, boolean rotatesTexture) {
		return MODEL_CACHE.computeIfAbsent(new ModelKey(theirModelLocation, theirModel, theirModelRotation, rotatesTexture), UnbakedRotatedPlacementModel::new);
	}

	public static void clearCache() {
		MODEL_CACHE.clear();
	}

	public final ResourceLocation theirModelLocation;
	public final IUnbakedModel theirModel;
	public final BlockRotation theirModelRotation;
	public final boolean rotatesTexture;

	private UnbakedRotatedPlacementModel(ModelKey key)
	{
		this.theirModelLocation = key.theirModelLocation;
		this.theirModel = key.theirModel;
		this.theirModelRotation = key.theirModelRotation;
		this.rotatesTexture = key.rotatesTexture;
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return Collections.emptyList();
	}

	@Override
	public Collection<RenderMaterial> getMaterials(Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
		return Collections.emptyList();
	}

	@Override
	public IBakedModel bake(ModelBakery baker, Function<RenderMaterial, TextureAtlasSprite> sprites, IModelTransform modelState, ResourceLocation modelLocation) {
		return BakedRotatedPlacementModel.of(BakingCache.bake(theirModel, sprites, theirModelLocation, baker), theirModelRotation, rotatesTexture);
	}
}
