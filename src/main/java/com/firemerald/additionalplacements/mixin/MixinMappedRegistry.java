package com.firemerald.additionalplacements.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.generation.Registration;
import com.mojang.serialization.Lifecycle;

import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.*;

@Mixin(MappedRegistry.class)
public class MixinMappedRegistry
{
	@Inject(method = "registerMapping(ILnet/minecraft/resources/ResourceKey;Ljava/lang/Object;Lcom/mojang/serialization/Lifecycle;)Lnet/minecraft/core/Holder$Reference;", at = @At("RETURN"))
    private void onRegisterMapping(int id, ResourceKey<?> key, Object value, Lifecycle lifecycle, CallbackInfoReturnable<Holder.Reference<?>> ci)
    {
		if (this == BuiltInRegistries.BLOCK && AdditionalPlacementsMod.dynamicRegistration)
		{
			Registration.tryApply((Block) value, key.location(), (rl, block) -> Registry.register(BuiltInRegistries.BLOCK, rl, block));
		}
    }
}
