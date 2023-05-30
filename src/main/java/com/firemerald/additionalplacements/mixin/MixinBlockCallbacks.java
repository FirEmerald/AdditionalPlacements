package com.firemerald.additionalplacements.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.*;
import com.firemerald.additionalplacements.block.interfaces.IPlacementBlock;

import net.minecraft.block.*;
import net.minecraft.util.ResourceLocation;
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
					if (AdditionalPlacementsMod.COMMON_CONFIG.isValidForGeneration(name))
					{
						owner.register(new VerticalSlabBlock((SlabBlock) block).setRegistryName(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()));
					}
				}
			}
			else if (block instanceof StairsBlock)
			{
				if (AdditionalPlacementsMod.COMMON_CONFIG.generateStairs.get() && !((IPlacementBlock<?>) block).hasAdditionalStates())
				{
					ResourceLocation name = block.getRegistryName();
					if (AdditionalPlacementsMod.COMMON_CONFIG.isValidForGeneration(name))
					{
						owner.register(new VerticalStairBlock((StairsBlock) block).setRegistryName(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()));
					}
				}
			}
			else if (block instanceof CarpetBlock)
			{
				if (AdditionalPlacementsMod.COMMON_CONFIG.generateCarpets.get() && !((IPlacementBlock<?>) block).hasAdditionalStates())
				{
					ResourceLocation name = block.getRegistryName();
					if (AdditionalPlacementsMod.COMMON_CONFIG.isValidForGeneration(name))
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
					if (AdditionalPlacementsMod.COMMON_CONFIG.isValidForGeneration(name))
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
					if (AdditionalPlacementsMod.COMMON_CONFIG.isValidForGeneration(name))
					{
						owner.register(new AdditionalWeightedPressurePlateBlock((WeightedPressurePlateBlock) block).setRegistryName(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()));
					}
				}
			}
		}
    }
}
