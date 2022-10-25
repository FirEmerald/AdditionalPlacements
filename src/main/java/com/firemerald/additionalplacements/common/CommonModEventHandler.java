package com.firemerald.additionalplacements.common;

import java.util.LinkedList;
import java.util.List;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.*;
import com.firemerald.additionalplacements.block.interfaces.IPlacementBlock;
import com.firemerald.additionalplacements.datagen.ModelGenerator;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
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
		boolean generateSlabs = AdditionalPlacementsMod.COMMON_CONFIG.generateSlabs.get();
		boolean generateStairs = AdditionalPlacementsMod.COMMON_CONFIG.generateStairs.get();
		boolean generateCarpets = AdditionalPlacementsMod.COMMON_CONFIG.generateCarpets.get();
		boolean generatePressurePlates = AdditionalPlacementsMod.COMMON_CONFIG.generatePressurePlates.get();
		boolean generateWeightedPressurePlates = AdditionalPlacementsMod.COMMON_CONFIG.generateWeightedPressurePlates.get();
		List<String> blacklist = AdditionalPlacementsMod.COMMON_CONFIG.blacklist.get();
		registry.getEntries().forEach(entry -> {
			ResourceLocation name = entry.getKey().location();
			Block block = entry.getValue();
			if (block instanceof SlabBlock)
			{
				if (generateSlabs && !((IPlacementBlock) block).hasAdditionalStates() && !blacklist.contains(name.toString()))
					created.add(new VerticalSlabBlock((SlabBlock) block).setRegistryName(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()));
			}
			else if (block instanceof StairBlock)
			{
				if (generateStairs && !((IPlacementBlock) block).hasAdditionalStates() && !blacklist.contains(name.toString()))
					created.add(new VerticalStairBlock((StairBlock) block).setRegistryName(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()));
			}
			else if (block instanceof CarpetBlock)
			{
				if (generateCarpets && !((IPlacementBlock) block).hasAdditionalStates() && !blacklist.contains(name.toString()))
					created.add(new AdditionalCarpetBlock((CarpetBlock) block).setRegistryName(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()));
			}
			else if (block instanceof PressurePlateBlock)
			{
				if (generatePressurePlates && !((IPlacementBlock) block).hasAdditionalStates() && !blacklist.contains(name.toString()))
					created.add(new AdditionalPressurePlateBlock((PressurePlateBlock) block).setRegistryName(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()));
			}
			else if (block instanceof WeightedPressurePlateBlock)
			{
				if (generateWeightedPressurePlates && !((IPlacementBlock) block).hasAdditionalStates() && !blacklist.contains(name.toString()))
					created.add(new AdditionalWeightedPressurePlateBlock((WeightedPressurePlateBlock) block).setRegistryName(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()));
			}
		});
		created.forEach(registry::register);
		AdditionalPlacementsMod.dynamicRegistration = true;
	}
	
	@SubscribeEvent
	public static void onGatherData(GatherDataEvent event)
	{
		if (event.includeClient())
		{
			event.getGenerator().addProvider(new ModelGenerator(event.getGenerator(), AdditionalPlacementsMod.MOD_ID, event.getExistingFileHelper()));
		}
	}
}