package com.firemerald.additionalplacements.client.models;

import com.firemerald.additionalplacements.util.BlockRotation;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.IModelData;

import java.util.*;

public class BakedRotatedPlacementModel extends PlacementModelWrapper
{
    private static final class ModelKey {
        private final IBakedModel theirModel;
        private final BlockRotation modelRotation;
        private final boolean rotatesTexture;

        private ModelKey(IBakedModel theirModel, BlockRotation modelRotation, boolean rotatesTexture) {
            this.theirModel = theirModel;
            this.modelRotation = modelRotation;
            this.rotatesTexture = rotatesTexture;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            ModelKey that = (ModelKey) obj;
            return Objects.equals(this.theirModel, that.theirModel) &&
                    Objects.equals(this.modelRotation, that.modelRotation) &&
                    this.rotatesTexture == that.rotatesTexture;
        }

        @Override
        public int hashCode() {
            return Objects.hash(theirModel, modelRotation, rotatesTexture);
        }

        @Override
        public String toString() {
            return "ModelKey[theirModel=" + theirModel + ", modelRotation=" + modelRotation + ", rotatesTexture=" + rotatesTexture + ']';
        }
    }

	private static final Map<ModelKey, BakedRotatedPlacementModel> MODEL_CACHE = new HashMap<>();

	public static synchronized BakedRotatedPlacementModel of(IBakedModel theirModel, BlockRotation modelRotation, boolean rotatesTexture) {
		return MODEL_CACHE.computeIfAbsent(new ModelKey(theirModel, modelRotation, rotatesTexture), BakedRotatedPlacementModel::new);
	}

	public static void clearCache() {
		MODEL_CACHE.clear();
	}

	private final BlockRotation modelRotation;
	public final boolean rotatesTexture;

	private BakedRotatedPlacementModel(ModelKey key)
	{
		super(key.theirModel);
		this.modelRotation = key.modelRotation;
		this.rotatesTexture = key.rotatesTexture;
	}

	@Override
	@Deprecated
	public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand)
	{
		BlockState modelState = BlockModelUtils.getModeledState(state);
		return BlockModelUtils.rotatedQuads(modelRotation, rotatesTexture, side, dir -> originalModel.getQuads(modelState, dir, rand), null);
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand, IModelData extraData)
	{
		BlockState modelState = BlockModelUtils.getModeledState(state);
		return BlockModelUtils.rotatedQuads(modelRotation, rotatesTexture, side, dir -> originalModel.getQuads(modelState, dir, rand, extraData), null);
	}
}
