package com.firemerald.additionalplacements.mixin;

import java.util.function.Function;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.firemerald.additionalplacements.block.interfaces.IPlacementBlock;
import com.simibubi.create.content.contraptions.StructureTransform;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(StructureTransform.class)
public class MixinStructureTransform
{
	@SuppressWarnings("unchecked")
	@Inject(target = @Desc(value = "apply", owner = StructureTransform.class, ret = BlockState.class, args = {BlockState.class}), at = @At("HEAD"), cancellable = true)
	public void apply(BlockState state, CallbackInfoReturnable<BlockState> ci)
	{
		if (state.getBlock() instanceof IPlacementBlock block)
		{
			if (block.hasAdditionalStates())
			{
				ci.setReturnValue(block.transform(state, (Function<Direction, Direction>) (dir -> ((StructureTransform) (Object) this).rotateFacing(((StructureTransform) (Object) this).mirrorFacing(dir)))));
			}
		}
	}
}