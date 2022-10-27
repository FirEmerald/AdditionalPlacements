package com.firemerald.additionalplacements.mixin;

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
				if (AdditionalPlacementsMod.COMMON_CONFIG.generateSlabs.get() && !((IPlacementBlock<?>) block).hasAdditionalStates())
				{
					ResourceLocation name = key.location();
					if (AdditionalPlacementsMod.COMMON_CONFIG.isValidForGeneration(name))
					{
						if (block instanceof WeatheringCopper)
							owner.register(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()), new VerticalWeatheringSlabBlock<>((SlabBlock & WeatheringCopper) block));
						else
							owner.register(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()), new VerticalSlabBlock((SlabBlock) block));
					}
				}
			}
			else if (block instanceof StairBlock)
			{
				if (AdditionalPlacementsMod.COMMON_CONFIG.generateStairs.get() && !((IPlacementBlock<?>) block).hasAdditionalStates())
				{
					ResourceLocation name = key.location();
					if (AdditionalPlacementsMod.COMMON_CONFIG.isValidForGeneration(name))
					{
						if (block instanceof WeatheringCopper)
							owner.register(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()), new VerticalWeatheringStairBlock<>((StairBlock & WeatheringCopper) block));
						else
							owner.register(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()), new VerticalStairBlock((StairBlock) block));
					}
				}
			}
			else if (block instanceof CarpetBlock)
			{
				if (AdditionalPlacementsMod.COMMON_CONFIG.generateCarpets.get() && !((IPlacementBlock<?>) block).hasAdditionalStates())
				{
					ResourceLocation name = key.location();
					if (AdditionalPlacementsMod.COMMON_CONFIG.isValidForGeneration(name))
					{
						owner.register(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()), new AdditionalCarpetBlock((CarpetBlock) block));
					}
				}
			}
			else if (block instanceof PressurePlateBlock)
			{
				if (AdditionalPlacementsMod.COMMON_CONFIG.generatePressurePlates.get() && !((IPlacementBlock<?>) block).hasAdditionalStates())
				{
					ResourceLocation name = key.location();
					if (AdditionalPlacementsMod.COMMON_CONFIG.isValidForGeneration(name))
					{
						owner.register(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()), new AdditionalPressurePlateBlock((PressurePlateBlock) block));
					}
				}
			}
			else if (block instanceof WeightedPressurePlateBlock)
			{
				if (AdditionalPlacementsMod.COMMON_CONFIG.generateWeightedPressurePlates.get() && !((IPlacementBlock<?>) block).hasAdditionalStates())
				{
					ResourceLocation name = key.location();
					if (AdditionalPlacementsMod.COMMON_CONFIG.isValidForGeneration(name))
					{
						owner.register(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()), new AdditionalWeightedPressurePlateBlock((WeightedPressurePlateBlock) block));
					}
				}
			}
		}
    }
}
