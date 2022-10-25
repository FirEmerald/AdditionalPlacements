package com.firemerald.additionalplacements.common;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.*;
import com.firemerald.additionalplacements.block.interfaces.IPlacementBlock;
import com.firemerald.additionalplacements.datagen.ModelGenerator;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
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
						created.put(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()), new VerticalSlabBlock((SlabBlock) block));
				}
				else if (block instanceof StairBlock)
				{
					if (generateStairs && !((IPlacementBlock) block).hasAdditionalStates() && !blacklist.contains(name.toString()))
						created.put(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()), new VerticalStairBlock((StairBlock) block));
				}
				else if (block instanceof CarpetBlock)
				{
					if (generateCarpets && !((IPlacementBlock) block).hasAdditionalStates() && !blacklist.contains(name.toString()))
						created.put(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()), new AdditionalCarpetBlock((CarpetBlock) block));
				}
				else if (block instanceof PressurePlateBlock)
				{
					if (generatePressurePlates && !((IPlacementBlock) block).hasAdditionalStates() && !blacklist.contains(name.toString()))
						created.put(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()), new AdditionalPressurePlateBlock((PressurePlateBlock) block));
				}
				else if (block instanceof WeightedPressurePlateBlock)
				{
					if (generateWeightedPressurePlates && !((IPlacementBlock) block).hasAdditionalStates() && !blacklist.contains(name.toString()))
						created.put(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()), new AdditionalWeightedPressurePlateBlock((WeightedPressurePlateBlock) block));
				}
			});
			created.forEach(registry::register);
			AdditionalPlacementsMod.dynamicRegistration = true;
		}
	}
	
	@SubscribeEvent
	public static void onGatherData(GatherDataEvent event)
	{
		if (event.includeClient())
		{
			event.getGenerator().addProvider(true, new ModelGenerator(event.getGenerator(), AdditionalPlacementsMod.MOD_ID, event.getExistingFileHelper()));
		}
	}
}