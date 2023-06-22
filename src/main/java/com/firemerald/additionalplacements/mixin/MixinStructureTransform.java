package com.firemerald.additionalplacements.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.firemerald.additionalplacements.block.interfaces.IPlacementBlock;
import com.simibubi.create.content.contraptions.components.structureMovement.StructureTransform;

import net.minecraft.block.BlockState;

@Mixin(StructureTransform.class)
public class MixinStructureTransform
{
	@Inject(target = @Desc(value = "apply", owner = StructureTransform.class, ret = BlockState.class, args = {BlockState.class}), at = @At("HEAD"), cancellable = true)
	public void apply(BlockState state, CallbackInfoReturnable<BlockState> ci)
	{
		if (state.getBlock() instanceof IPlacementBlock)
		{
			IPlacementBlock<?> block = (IPlacementBlock<?>) state.getBlock();
			if (block.hasAdditionalStates())
			{
				ci.setReturnValue(block.transform(state, ((StructureTransform) (Object) this)::transformFacing));
			}
		}
	}
}