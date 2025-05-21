package com.firemerald.additionalplacements.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.firemerald.additionalplacements.client.IModelBakerExtensions;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;

@Mixin(ModelBaker.class)
public interface MixinModelBaker extends IModelBakerExtensions {
	@Override
    default BakedModel apBakeUncached(UnbakedModel model, ModelState state) {
		throw new IllegalStateException("ModelBaker not implementing IModelBakerExtensions found. Generally caused by calling PlacementBlockModel.bake with a ModelBaker that isn't an instance of ModelBakery.ModelBakerImpl");
	}
}
