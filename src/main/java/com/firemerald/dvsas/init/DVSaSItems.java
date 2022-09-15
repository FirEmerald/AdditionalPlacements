package com.firemerald.dvsas.init;

import com.firemerald.dvsas.DVSaSMod;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(DVSaSMod.MOD_ID)
public class DVSaSItems
{
	@ObjectHolder(RegistryNames.BEDROCK_SLAB)
	public static final BlockItem BEDROCK_SLAB = null;
	@ObjectHolder(RegistryNames.BEDROCK_STAIRS)
	public static final BlockItem BEDROCK_STAIRS = null;

	public static void registerItems(IEventBus eventBus)
	{
		DeferredRegister<Item> items = DeferredRegister.create(ForgeRegistries.ITEMS, DVSaSMod.MOD_ID);
		if (DVSaSMod.TEST_MODE)
		{
			items.register(RegistryNames.BEDROCK_SLAB, () -> new BlockItem(DVSaSBlocks.BEDROCK_SLAB, new Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
			items.register(RegistryNames.BEDROCK_STAIRS, () -> new BlockItem(DVSaSBlocks.BEDROCK_STAIRS, new Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
		}
		items.register(eventBus);
	}
}
