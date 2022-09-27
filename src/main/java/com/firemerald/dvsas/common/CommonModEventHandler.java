package com.firemerald.dvsas.common;

import java.util.LinkedList;
import java.util.List;

import com.firemerald.dvsas.DVSaSMod;
import com.firemerald.dvsas.block.IVerticalBlock;
import com.firemerald.dvsas.block.VerticalSlabBlock;
import com.firemerald.dvsas.block.VerticalStairBlock;
import com.firemerald.dvsas.datagen.ModelGenerator;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEventHandler
{
	@SubscribeEvent
	public static void onBlockRegistry(RegistryEvent.Register<Block> event)
	{
		IForgeRegistry<Block> registry = event.getRegistry();
		final List<Block> created = new LinkedList<>();
		boolean generateSlabs = DVSaSMod.COMMON_CONFIG.generateSlabs.get();
		boolean generateStairs = DVSaSMod.COMMON_CONFIG.generateStairs.get();
		registry.getEntries().forEach(entry -> {
			ResourceLocation name = entry.getKey().location();
			Block block = entry.getValue();
			if (block instanceof SlabBlock)
			{
				if (generateSlabs && !((IVerticalBlock) block).hasVertical() && !DVSaSMod.COMMON_CONFIG.blacklist.get().contains(name.toString()))
					created.add(new VerticalSlabBlock((SlabBlock) block).setRegistryName(DVSaSMod.MOD_ID, name.getNamespace() + "." + name.getPath()));
			}
			else if (block instanceof StairBlock)
			{
				if (generateStairs && !((IVerticalBlock) block).hasVertical() && !DVSaSMod.COMMON_CONFIG.blacklist.get().contains(name.toString()))
					created.add(new VerticalStairBlock((StairBlock) block).setRegistryName(DVSaSMod.MOD_ID, name.getNamespace() + "." + name.getPath()));
			}
		});
		created.forEach(registry::register);
		DVSaSMod.dynamicRegistration = true;
	}
	
	@SubscribeEvent
	public static void onGatherData(GatherDataEvent event)
	{
		if (event.includeClient())
		{
			event.getGenerator().addProvider(new ModelGenerator(event.getGenerator(), DVSaSMod.MOD_ID, event.getExistingFileHelper()));
		}
	}
}