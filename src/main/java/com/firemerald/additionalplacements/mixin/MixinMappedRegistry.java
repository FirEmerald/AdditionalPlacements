package com.firemerald.additionalplacements.mixin;

import java.util.function.Function;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.*;
import com.firemerald.additionalplacements.block.interfaces.IPlacementBlock;
import com.mojang.serialization.Lifecycle;

import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;

@Mixin(MappedRegistry.class)
public class MixinMappedRegistry
{
	@Inject(method = "registerMapping(ILnet/minecraft/resources/ResourceKey;Ljava/lang/Object;Lcom/mojang/serialization/Lifecycle;)Lnet/minecraft/core/Holder$Reference;", at = @At("RETURN"))
    private void onRegisterMapping(int id, ResourceKey<?> key, Object value, Lifecycle lifecycle, CallbackInfoReturnable<Holder.Reference<?>> ci)
    {
		if ((Object) this == BuiltInRegistries.BLOCK && AdditionalPlacementsMod.dynamicRegistration)
		{
			if (value instanceof SlabBlock)
			{
				if (AdditionalPlacementsMod.COMMON_CONFIG.generateSlabs.get()) tryAdd((SlabBlock) value, key, VerticalSlabBlock::of);
			}
			else if (value instanceof StairBlock)
			{
				if (AdditionalPlacementsMod.COMMON_CONFIG.generateStairs.get()) tryAdd((StairBlock) value, key, VerticalStairBlock::of);
			}
			else if (value instanceof CarpetBlock)
			{
				if (AdditionalPlacementsMod.COMMON_CONFIG.generateCarpets.get()) tryAdd((CarpetBlock) value, key, AdditionalCarpetBlock::of);
			}
			else if (value instanceof PressurePlateBlock)
			{
				if (AdditionalPlacementsMod.COMMON_CONFIG.generatePressurePlates.get()) tryAdd((PressurePlateBlock) value, key, AdditionalPressurePlateBlock::of);
			}
			else if (value instanceof WeightedPressurePlateBlock)
			{
				if (AdditionalPlacementsMod.COMMON_CONFIG.generateWeightedPressurePlates.get()) tryAdd((WeightedPressurePlateBlock) value, key, AdditionalWeightedPressurePlateBlock::of);
			}
		}
    }
	
	private static <T extends Block, U extends AdditionalPlacementBlock<T>> void tryAdd(T block, ResourceKey<?> key, Function<T, U> construct)
	{
		if (!((IPlacementBlock<?>) block).hasAdditionalStates())
		{
			ResourceLocation name = key.location();
			if (AdditionalPlacementsMod.COMMON_CONFIG.isValidForGeneration(name))
			{
				Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()), construct.apply(block));
			}
		}
	}
}
