package com.firemerald.additionalplacements.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.firemerald.additionalplacements.client.models.PlacementBlockModelLoader;

import io.github.fabricators_of_create.porting_lib.model.ModelLoaderRegistry;

@Mixin(ModelLoaderRegistry.class)
public class MixinModelLoaderRegistry
{
	@Inject(method = "init", at = @At("RETURN"), remap = false)
	private static void onInit(CallbackInfo info)
	{
    	ModelLoaderRegistry.registerLoader(PlacementBlockModelLoader.ID, new PlacementBlockModelLoader());
	}
}