package com.firemerald.dvsas.common;

import java.util.LinkedHashMap;
import java.util.Map;

import com.firemerald.dvsas.DVSaSMod;
import com.firemerald.dvsas.block.IVerticalBlock;
import com.firemerald.dvsas.block.VerticalSlabBlock;
import com.firemerald.dvsas.block.VerticalStairBlock;
import com.firemerald.dvsas.datagen.ModelGenerator;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEventHandler
{
	@SubscribeEvent
	public static void onBlockRegistry(RegisterEvent event)
	{
		if (event.getForgeRegistry() != null && ForgeRegistries.BLOCKS.getRegistryKey().equals(event.getForgeRegistry().getRegistryKey()))
		{
			IForgeRegistry<Block> registry = event.getForgeRegistry();
			final Map<ResourceLocation, Block> created = new LinkedHashMap<>();
			boolean generateSlabs = DVSaSMod.COMMON_CONFIG.generateSlabs.get();
			boolean generateStairs = DVSaSMod.COMMON_CONFIG.generateStairs.get();
			registry.getEntries().forEach(entry -> {
				ResourceLocation name = entry.getKey().location();
				Block block = entry.getValue();
				if (block instanceof SlabBlock)
				{
					if (generateSlabs && !((IVerticalBlock) block).hasVertical() && !DVSaSMod.COMMON_CONFIG.blacklist.get().contains(name.toString())) created.put(new ResourceLocation(DVSaSMod.MOD_ID, name.getNamespace() + "." + name.getPath()), new VerticalSlabBlock((SlabBlock) block));
				}
				else if (block instanceof StairBlock)
				{
					if (generateStairs && !((IVerticalBlock) block).hasVertical() && !DVSaSMod.COMMON_CONFIG.blacklist.get().contains(name.toString())) created.put(new ResourceLocation(DVSaSMod.MOD_ID, name.getNamespace() + "." + name.getPath()), new VerticalStairBlock((StairBlock) block));
				}
			});
			created.forEach(registry::register);
			DVSaSMod.dynamicRegistration = true;
		}
	}

	@SubscribeEvent
	public static void onGatherData(GatherDataEvent event)
	{
		if (event.includeClient())
		{
			event.getGenerator().addProvider(true, new ModelGenerator(event.getGenerator(), DVSaSMod.MOD_ID, event.getExistingFileHelper()));
		}
	}
}