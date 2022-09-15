package com.firemerald.dvsas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firemerald.dvsas.block.VerticalBlock;
import com.firemerald.dvsas.datagen.ModelGenerator;
import com.firemerald.dvsas.init.DVSaSBlocks;
import com.firemerald.dvsas.init.DVSaSItems;

import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(DVSaSMod.MOD_ID)
public class DVSaSMod
{
	public static final boolean TEST_MODE = false;
	
	public static final String MOD_ID = "dvsas";
    public static final Logger LOGGER = LoggerFactory.getLogger("DVSaS");

    public DVSaSMod()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onGatherData);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, EventPriority.LOWEST, this::onItemsRegistered);
        DVSaSBlocks.registerBlocks(FMLJavaModLoadingContext.get().getModEventBus());
        DVSaSItems.registerItems(FMLJavaModLoadingContext.get().getModEventBus());
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
