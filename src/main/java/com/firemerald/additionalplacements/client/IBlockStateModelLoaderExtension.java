package com.firemerald.additionalplacements.client;

import java.util.Map;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;

public interface IBlockStateModelLoaderExtension {
	void setTopLevelModels(Map<ModelResourceLocation, UnbakedModel> topLevelModels);
}
