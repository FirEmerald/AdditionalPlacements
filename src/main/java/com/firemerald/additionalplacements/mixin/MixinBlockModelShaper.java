package com.firemerald.additionalplacements.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.client.models.BakedRotatedBlockModel;
import com.firemerald.additionalplacements.client.models.BakedRetexturedBlockModel;
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
			boolean rotatesModel = block.rotatesModel(state);
			BakedModel modelModel = ((BlockModelShaper) (Object) this).getBlockModel(rotatesModel ? block.getUnrotatedModelState(state) : block.getModelState(state));
			ci.setReturnValue(rotatesModel ? 
					BakedRotatedBlockModel.of(modelModel, block.getRotation(state), block.rotatesTexture(state)) : 
						BakedRetexturedBlockModel.of(modelModel, ((BakedPlacementBlockModel) ci.getReturnValue()).originalModel()));
		}
    }
}