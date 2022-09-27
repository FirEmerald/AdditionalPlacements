package com.firemerald.dvsas.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.firemerald.dvsas.DVSaSMod;
import com.firemerald.dvsas.block.IVerticalBlock;
import com.firemerald.dvsas.block.VerticalSlabBlock;
import com.firemerald.dvsas.block.VerticalStairBlock;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraftforge.registries.IForgeRegistryInternal;
import net.minecraftforge.registries.RegistryManager;

@Mixin(targets = "net.minecraftforge.registries.GameData$BlockCallbacks", remap = false)
public class MixinBlockCallbacks
{
	@Inject(method = "onAdd", at = @At("HEAD"), remap = false)
    private void onAdd(IForgeRegistryInternal<Block> owner, RegistryManager stage, int id, Block block, @Nullable Block oldBlock, CallbackInfo ci)
    {
		if (DVSaSMod.dynamicRegistration)
		{
			if (block instanceof SlabBlock)
			{
				if (DVSaSMod.COMMON_CONFIG.generateSlabs.get() && !((IVerticalBlock) block).hasVertical())
				{
					ResourceLocation name = block.getRegistryName();
					if (!DVSaSMod.COMMON_CONFIG.blacklist.get().contains(name.toString()))
					{
						owner.register(new VerticalSlabBlock((SlabBlock) block).setRegistryName(DVSaSMod.MOD_ID, name.getNamespace() + "." + name.getPath()));
					}
				}
			}
			else if (block instanceof StairBlock)
			{
				if (DVSaSMod.COMMON_CONFIG.generateStairs.get() && !((IVerticalBlock) block).hasVertical())
				{
					ResourceLocation name = block.getRegistryName();
					if (!DVSaSMod.COMMON_CONFIG.blacklist.get().contains(name.toString()))
					{
						owner.register(new VerticalStairBlock((StairBlock) block).setRegistryName(DVSaSMod.MOD_ID, name.getNamespace() + "." + name.getPath()));
					}
				}
			}
		}
    }
}
