package com.firemerald.additionalplacements.client;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;

public interface IModelBakerExtensions {
	BakedModel apBakeUncached(UnbakedModel model, ModelState state);
}
