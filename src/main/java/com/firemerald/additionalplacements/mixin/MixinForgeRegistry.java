package com.firemerald.additionalplacements.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.generation.Registration;

import net.minecraft.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mixin(value = ForgeRegistry.class, remap = false)
public class MixinForgeRegistry
{
	@SuppressWarnings("unchecked")
	@Inject(method = "register(Lnet/minecraftforge/registries/IForgeRegistryEntry;)V", at = @At("RETURN"), remap = false)
	private <V extends IForgeRegistryEntry<V>> void register(V value, CallbackInfo ci)
    {
		if (this == ForgeRegistries.BLOCKS && AdditionalPlacementsMod.dynamicRegistration)
		{
			Block block = (Block) value;
			Registration.tryApply(block, block.getRegistryName(), (blockId, obj) -> ((IForgeRegistry<Block>) this).register(obj));
		}
    }
}
