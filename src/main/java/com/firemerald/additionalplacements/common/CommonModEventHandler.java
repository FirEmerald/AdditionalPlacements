package com.firemerald.additionalplacements.common;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.*;
import com.firemerald.additionalplacements.block.interfaces.IPlacementBlock;
import com.firemerald.additionalplacements.datagen.ModelGenerator;

import net.minecraft.block.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEventHandler
{
	@SubscribeEvent
	public static void onBlockRegistry(RegistryEvent.Register<Block> event)
	{
		IForgeRegistry<Block> registry = event.getRegistry();
		boolean generateSlabs = AdditionalPlacementsMod.COMMON_CONFIG.generateSlabs.get();
		boolean generateStairs = AdditionalPlacementsMod.COMMON_CONFIG.generateStairs.get();
		boolean generateCarpets = AdditionalPlacementsMod.COMMON_CONFIG.generateCarpets.get();
		boolean generatePressurePlates = AdditionalPlacementsMod.COMMON_CONFIG.generatePressurePlates.get();
		boolean generateWeightedPressurePlates = AdditionalPlacementsMod.COMMON_CONFIG.generateWeightedPressurePlates.get();
		List<Block> created = new ArrayList<>();
		registry.getEntries().forEach(entry -> {
			ResourceLocation name = entry.getKey().location();
			Block block = entry.getValue();
			if (block instanceof SlabBlock)
			{
				if (generateSlabs) tryAdd((SlabBlock) block, name, VerticalSlabBlock::of, created);
			}
			else if (block instanceof StairsBlock)
			{
				if (generateStairs) tryAdd((StairsBlock) block, name, VerticalStairBlock::of, created);
			}
			else if (block instanceof CarpetBlock)
			{
				if (generateCarpets) tryAdd((CarpetBlock) block, name, AdditionalCarpetBlock::of, created);
			}
			else if (block instanceof PressurePlateBlock)
			{
				if (generatePressurePlates) tryAdd((PressurePlateBlock) block, name, AdditionalPressurePlateBlock::of, created);
			}
			else if (block instanceof WeightedPressurePlateBlock)
			{
				if (generateWeightedPressurePlates) tryAdd((WeightedPressurePlateBlock) block, name, AdditionalWeightedPressurePlateBlock::of, created);
			}
		});
		created.forEach(registry::register);
		AdditionalPlacementsMod.dynamicRegistration = true;
	}

	private static <T extends Block, U extends AdditionalPlacementBlock<T>> void tryAdd(T block, ResourceLocation name, Function<T, U> construct, List<Block> list)
	{
		if (!((IPlacementBlock<?>) block).hasAdditionalStates() && AdditionalPlacementsMod.COMMON_CONFIG.isValidForGeneration(name))
			list.add(construct.apply(block).setRegistryName(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()));
	}

	@SubscribeEvent
	public static void onGatherData(GatherDataEvent event)
	{
		if (event.includeClient())
		{
			event.getGenerator().addProvider(new ModelGenerator(event.getGenerator(), AdditionalPlacementsMod.MOD_ID, event.getExistingFileHelper()));
		}
	}

	public static boolean doubleslabsLoaded;

	@SubscribeEvent
	public static void onFMLCommonSetup(FMLCommonSetupEvent event)
	{
		doubleslabsLoaded = ModList.get().isLoaded("doubleslabs");
	}
}