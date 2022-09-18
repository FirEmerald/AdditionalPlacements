package com.firemerald.dvsas.common;

import java.util.LinkedList;
import java.util.List;

import com.firemerald.dvsas.DVSaSMod;
import com.firemerald.dvsas.block.IVerticalBlock;
import com.firemerald.dvsas.block.VerticalSlabBlock;
import com.firemerald.dvsas.block.VerticalStairBlock;
import com.firemerald.dvsas.datagen.ModelGenerator;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEventHandler
{
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onBlockRegistry(RegistryEvent.Register<Block> event)
	{
		final List<Block> created = new LinkedList<>();
		event.getRegistry().forEach(block -> {
			if (block instanceof IVerticalBlock)
			{
				if (!((IVerticalBlock) block).hasVertical())
				{
					if (block instanceof SlabBlock)
					{
						if (block.defaultBlockState().getProperties().size() == 2) //don't register if there are properties that won't get carried over
							created.add(new VerticalSlabBlock((SlabBlock) block).setRegistryName(DVSaSMod.MOD_ID, block.getRegistryName().getNamespace() + "." + block.getRegistryName().getPath()));
					}
					else if (block instanceof StairBlock)
					{
						if (block.defaultBlockState().getProperties().size() == 4) //don't register if there are properties that won't get carried over
							created.add(new VerticalStairBlock((StairBlock) block).setRegistryName(DVSaSMod.MOD_ID, block.getRegistryName().getNamespace() + "." + block.getRegistryName().getPath()));
					}
				}
			}
		});
		created.forEach(event.getRegistry()::register);
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