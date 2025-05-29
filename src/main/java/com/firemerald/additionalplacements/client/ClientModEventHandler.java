package com.firemerald.additionalplacements.client;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;

import com.firemerald.additionalplacements.client.models.BakedRetexturedPlacementModel;
import com.firemerald.additionalplacements.client.models.BakedRotatedPlacementModel;
import com.firemerald.additionalplacements.client.models.BakingCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
@OnlyIn(Dist.CLIENT)
public class ClientModEventHandler
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void init(FMLClientSetupEvent event) {
    	ForgeRegistries.BLOCKS.forEach(block -> {
    		if (block instanceof AdditionalPlacementBlock)
    		{
				BlockState otherState  = ((AdditionalPlacementBlock<?>) block).getOtherBlockState();
    			ItemBlockRenderTypes.setRenderLayer(block, (layer) -> ItemBlockRenderTypes.canRenderInLayer(otherState , layer));
    		}
    	});
    	Minecraft.getInstance().getBlockColors().register(new AdditionalBlockColor(), ForgeRegistries.BLOCKS.getValues().stream().filter(block -> block instanceof AdditionalPlacementBlock && !((AdditionalPlacementBlock<?>) block).hasCustomColors()).toArray(Block[]::new));
    	ClientRegistry.registerKeyBinding(APClientData.AP_PLACEMENT_KEY);
    }

    @SubscribeEvent
    public static void onRegisterClientReloadListeners(RegisterClientReloadListenersEvent event) {
		event.registerReloadListener((ResourceManagerReloadListener) resourceManager -> {
			BakingCache.clearCache();
			BakedRotatedPlacementModel.clearCache();
			BakedRetexturedPlacementModel.clearCache();
		});
    }
}