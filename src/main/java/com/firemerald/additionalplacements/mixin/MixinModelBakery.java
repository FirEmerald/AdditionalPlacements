package com.firemerald.additionalplacements.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.firemerald.additionalplacements.client.IBlockStateModelLoaderExtension;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;

@Mixin(ModelBakery.class)
public class MixinModelBakery {
	@Shadow
	@Final
    private Map<ModelResourceLocation, UnbakedModel> topLevelModels;

	@WrapOperation(method = "<init>(Lnet/minecraft/client/color/block/BlockColors;Lnet/minecraft/util/profiling/ProfilerFiller;Ljava/util/Map;Ljava/util/Map;)V", at = @At(value = "INVOKE", target = "net/minecraft/client/resources/model/BlockStateModelLoader.loadAllBlockStates()V"))
	public void loadAllBlockStates(BlockStateModelLoader blockstatemodelloader, Operation<Void> original)
	{
		((IBlockStateModelLoaderExtension) blockstatemodelloader).setTopLevelModels(topLevelModels);
		original.call(blockstatemodelloader);
	}
}
