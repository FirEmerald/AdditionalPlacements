package com.firemerald.additionalplacements.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.client.models.BakedParticleDeferredBlockModel;
import com.firemerald.additionalplacements.client.models.BakedPlacementBlockModel;

import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(BlockModelShaper.class)
public class MixinBlockModelShaper
{
	@Inject(method = "getBlockModel", at = @At("RETURN"), cancellable = true)
    public void onGetBlockModel(BlockState state, CallbackInfoReturnable<BakedModel> ci)
    {
		if (state.getBlock() instanceof AdditionalPlacementBlock && ci.getReturnValue() instanceof BakedPlacementBlockModel)
		{
			AdditionalPlacementBlock<?> block = (AdditionalPlacementBlock<?>) state.getBlock();
			BlockState modelState = block.getModelState(state);
			ci.setReturnValue(BakedParticleDeferredBlockModel.of(ci.getReturnValue(), ((BlockModelShaper) (Object) this).getBlockModel(modelState).getParticleIcon()));
		}
    }
}