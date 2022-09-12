package com.firemerald.vsas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firemerald.vsas.block.VerticalBlock;
import com.firemerald.vsas.datagen.ModelGenerator;
import com.firemerald.vsas.init.VSASBlocks;
import com.firemerald.vsas.init.VSASItems;

import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(VSASMod.MOD_ID)
public class VSASMod
{
	public static final boolean TEST_MODE = false;
	
	public static final String MOD_ID = "vsas";
    public static final Logger LOGGER = LoggerFactory.getLogger("VerticalSlabsAndStairs");

    public VSASMod()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onGatherData);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, EventPriority.LOWEST, this::onItemsRegistered);
        VSASBlocks.registerBlocks(FMLJavaModLoadingContext.get().getModEventBus());
        VSASItems.registerItems(FMLJavaModLoadingContext.get().getModEventBus());
    }

	public void onGatherData(GatherDataEvent event)
	{
		if (event.includeClient())
		{
			event.getGenerator().addProvider(new ModelGenerator(event.getGenerator(), MOD_ID, event.getExistingFileHelper()));
		}
	}
	
	public void onItemsRegistered(RegistryEvent.Register<Item> event)
	{
		ForgeRegistries.BLOCKS.forEach(block -> {
			if (block instanceof VerticalBlock) ((VerticalBlock<?>) block).onFinishedRegistering();
		});
	}
}
