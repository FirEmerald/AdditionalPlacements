package com.firemerald.additionalplacements.mixin;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.firemerald.additionalplacements.client.ClientModEvents;

import net.fabricmc.fabric.impl.resource.loader.ModResourcePackCreator;
import net.minecraft.server.packs.repository.Pack;

@Mixin(ModResourcePackCreator.class)
public class MixinModResourcePackCreator
{
	@Inject(method="loadPacks", at = @At("HEAD"))
	public void loadPacks(Consumer<Pack> consumer, CallbackInfo info)
	{
		consumer.accept(ClientModEvents.GENERATED_RESOURCES_PACK);
	}
}