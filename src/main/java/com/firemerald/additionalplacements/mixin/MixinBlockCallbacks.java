package com.firemerald.additionalplacements.mixin;

import java.util.function.Function;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.*;
import com.firemerald.additionalplacements.block.interfaces.IPlacementBlock;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraftforge.registries.IForgeRegistryInternal;
import net.minecraftforge.registries.RegistryManager;

@Mixin(targets = "net.minecraftforge.registries.GameData$BlockCallbacks", remap = false)
public class MixinBlockCallbacks
{
	@Inject(method = "onAdd", at = @At("HEAD"), remap = false)
	private void onAdd(IForgeRegistryInternal<Block> owner, RegistryManager stage, int id, ResourceKey<Block> key, Block block, @Nullable Block oldBlock, CallbackInfo ci)
    {
		if (AdditionalPlacementsMod.dynamicRegistration)
		{
			if (block instanceof SlabBlock)
			{
				if (AdditionalPlacementsMod.COMMON_CONFIG.generateSlabs.get()) tryAdd((SlabBlock) block, key, VerticalSlabBlock::of, owner);
			}
			else if (block instanceof StairBlock)
			{
				if (AdditionalPlacementsMod.COMMON_CONFIG.generateStairs.get()) tryAdd((StairBlock) block, key, VerticalStairBlock::of, owner);
			}
			else if (block instanceof CarpetBlock)
			{
				if (AdditionalPlacementsMod.COMMON_CONFIG.generateCarpets.get()) tryAdd((CarpetBlock) block, key, AdditionalCarpetBlock::of, owner);
			}
			else if (block instanceof PressurePlateBlock)
			{
				if (AdditionalPlacementsMod.COMMON_CONFIG.generatePressurePlates.get()) tryAdd((PressurePlateBlock) block, key, AdditionalPressurePlateBlock::of, owner);
			}
			else if (block instanceof WeightedPressurePlateBlock)
			{
				if (AdditionalPlacementsMod.COMMON_CONFIG.generateWeightedPressurePlates.get()) tryAdd((WeightedPressurePlateBlock) block, key, AdditionalWeightedPressurePlateBlock::of, owner);
			}
		}
    }

	private static <T extends Block, U extends AdditionalPlacementBlock<T>> void tryAdd(T block, ResourceKey<Block> key, Function<T, U> construct, IForgeRegistryInternal<Block> owner)
	{
		if (!((IPlacementBlock<?>) block).hasAdditionalStates())
		{
			ResourceLocation name = key.location();
			if (AdditionalPlacementsMod.COMMON_CONFIG.isValidForGeneration(name))
			{
				owner.register(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()), construct.apply(block));
			}
		}
	}
}
