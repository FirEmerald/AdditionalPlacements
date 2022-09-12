package com.firemerald.vsas.init;

import com.firemerald.vsas.VSASMod;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(VSASMod.MOD_ID)
public class VSASItems
{
	@ObjectHolder(RegistryNames.BEDROCK_SLAB)
	public static final BlockItem BEDROCK_SLAB = null;
	@ObjectHolder(RegistryNames.BEDROCK_STAIRS)
	public static final BlockItem BEDROCK_STAIRS = null;

	public static void registerItems(IEventBus eventBus)
	{
		DeferredRegister<Item> items = DeferredRegister.create(ForgeRegistries.ITEMS, VSASMod.MOD_ID);
		if (VSASMod.TEST_MODE)
		{
			items.register(RegistryNames.BEDROCK_SLAB, () -> new BlockItem(VSASBlocks.BEDROCK_SLAB, new Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
			items.register(RegistryNames.BEDROCK_STAIRS, () -> new BlockItem(VSASBlocks.BEDROCK_STAIRS, new Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
		}
		items.register(eventBus);
	}
}
