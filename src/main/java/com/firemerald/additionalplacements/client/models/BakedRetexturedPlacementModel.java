package com.firemerald.additionalplacements.client.models;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.IModelData;

import java.util.*;

public class BakedRetexturedPlacementModel extends PlacementModelWrapper
{
    private static final class ModelKey {
        private final IBakedModel ourModel;
        private final IBakedModel theirModel;

        private ModelKey(IBakedModel ourModel, IBakedModel theirModel) {
            this.ourModel = ourModel;
            this.theirModel = theirModel;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            ModelKey that = (ModelKey) obj;
            return Objects.equals(this.ourModel, that.ourModel) &&
                    Objects.equals(this.theirModel, that.theirModel);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ourModel, theirModel);
        }

        @Override
        public String toString() {
            return "ModelKey[ourModel=" + ourModel + ", theirModel=" + theirModel + ']';
        }
    }

	private static final Map<ModelKey, BakedRetexturedPlacementModel> MODEL_CACHE = new HashMap<>();

	public static synchronized BakedRetexturedPlacementModel of(IBakedModel ourModel, IBakedModel theirModel) {
		return MODEL_CACHE.computeIfAbsent(new ModelKey(ourModel, theirModel), BakedRetexturedPlacementModel::new);
	}

	public static void clearCache() {
		MODEL_CACHE.clear();
	}

	private final IBakedModel ourModel;

	private BakedRetexturedPlacementModel(ModelKey key)
	{
		super(key.theirModel);
		this.ourModel = key.ourModel;
	}

	@Override
	@Deprecated
	public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand)
	{
		BlockState modelState = BlockModelUtils.getModeledState(state);
		return BlockModelUtils.retexturedQuads(side, dir -> ourModel.getQuads(state, dir, rand), dir -> originalModel.getQuads(modelState, dir, rand), null);
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand, IModelData extraData)
	{
		BlockState modelState = BlockModelUtils.getModeledState(state);
		return BlockModelUtils.retexturedQuads(side, dir -> ourModel.getQuads(state, dir, rand, extraData), dir -> originalModel.getQuads(modelState, dir, rand, extraData), null);
	}
}
