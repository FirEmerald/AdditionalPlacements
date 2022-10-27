package com.firemerald.additionalplacements.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.*;
import com.firemerald.additionalplacements.block.interfaces.IPlacementBlock;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraftforge.registries.IForgeRegistryInternal;
import net.minecraftforge.registries.RegistryManager;

@Mixin(targets = "net.minecraftforge.registries.GameData$BlockCallbacks", remap = false)
public class MixinBlockCallbacks
{
	@Inject(method = "onAdd", at = @At("HEAD"), remap = false)
    private void onAdd(IForgeRegistryInternal<Block> owner, RegistryManager stage, int id, Block block, @Nullable Block oldBlock, CallbackInfo ci)
    {
		if (AdditionalPlacementsMod.dynamicRegistration)
		{
			if (block instanceof SlabBlock)
			{
				if (AdditionalPlacementsMod.COMMON_CONFIG.generateSlabs.get() && !((IPlacementBlock<?>) block).hasAdditionalStates())
				{
					ResourceLocation name = block.getRegistryName();
					if (!AdditionalPlacementsMod.COMMON_CONFIG.blacklist.get().contains(name.toString()))
					{
						if (block instanceof WeatheringCopper)
							owner.register(new VerticalWeatheringSlabBlock<>((SlabBlock & WeatheringCopper) block).setRegistryName(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()));
						else
							owner.register(new VerticalSlabBlock((SlabBlock) block).setRegistryName(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()));
					}
				}
			}
			else if (block instanceof StairBlock)
			{
				if (AdditionalPlacementsMod.COMMON_CONFIG.generateStairs.get() && !((IPlacementBlock<?>) block).hasAdditionalStates())
				{
					ResourceLocation name = block.getRegistryName();
					if (!AdditionalPlacementsMod.COMMON_CONFIG.blacklist.get().contains(name.toString()))
					{
						if (block instanceof WeatheringCopper)
							owner.register(new VerticalWeatheringStairBlock<>((StairBlock & WeatheringCopper) block).setRegistryName(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()));
						else
							owner.register(new VerticalStairBlock((StairBlock) block).setRegistryName(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()));
					}
				}
			}
			else if (block instanceof CarpetBlock)
			{
				if (AdditionalPlacementsMod.COMMON_CONFIG.generateCarpets.get() && !((IPlacementBlock<?>) block).hasAdditionalStates())
				{
					ResourceLocation name = block.getRegistryName();
					if (!AdditionalPlacementsMod.COMMON_CONFIG.blacklist.get().contains(name.toString()))
					{
						owner.register(new AdditionalCarpetBlock((CarpetBlock) block).setRegistryName(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()));
					}
				}
			}
			else if (block instanceof PressurePlateBlock)
			{
				if (AdditionalPlacementsMod.COMMON_CONFIG.generatePressurePlates.get() && !((IPlacementBlock<?>) block).hasAdditionalStates())
				{
					ResourceLocation name = block.getRegistryName();
					if (!AdditionalPlacementsMod.COMMON_CONFIG.blacklist.get().contains(name.toString()))
					{
						owner.register(new AdditionalPressurePlateBlock((PressurePlateBlock) block).setRegistryName(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()));
					}
				}
			}
			else if (block instanceof WeightedPressurePlateBlock)
			{
				if (AdditionalPlacementsMod.COMMON_CONFIG.generateWeightedPressurePlates.get() && !((IPlacementBlock<?>) block).hasAdditionalStates())
				{
					ResourceLocation name = block.getRegistryName();
					if (!AdditionalPlacementsMod.COMMON_CONFIG.blacklist.get().contains(name.toString()))
					{
						owner.register(new AdditionalWeightedPressurePlateBlock((WeightedPressurePlateBlock) block).setRegistryName(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()));
					}
				}
			}
		}
    }
}
