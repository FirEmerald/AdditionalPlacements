package com.firemerald.additionalplacements.client;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.client.models.BakedRetexturedPlacementModel;
import com.firemerald.additionalplacements.client.models.BakedRotatedPlacementModel;
import com.firemerald.additionalplacements.client.models.BakingCache;
import com.firemerald.additionalplacements.client.models.Unwrapper;

import me.pepperbell.continuity.client.model.CtmBakedModel;
import me.pepperbell.continuity.client.model.EmissiveBakedModel;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
@OnlyIn(Dist.CLIENT)
public class ClientModEventHandler
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onRegisterBlockColorHandlers(RegisterColorHandlersEvent.Block event)
    {
		event.register(new AdditionalBlockColor(), ForgeRegistries.BLOCKS.getValues().stream().filter(block -> block instanceof AdditionalPlacementBlock && !((AdditionalPlacementBlock<?>) block).hasCustomColors()).toArray(Block[]::new));
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event)
    {
    	event.register(APClientData.AP_PLACEMENT_KEY);
    }

    @SubscribeEvent
    public static void onRegisterClientReloadListeners(RegisterClientReloadListenersEvent event) {
    	event.registerReloadListener((ResourceManagerReloadListener) resourceManager -> {
            BakingCache.clearCache();
            BakedRotatedPlacementModel.clearCache();
            BakedRetexturedPlacementModel.clearCache();
        });
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
    	if (ModList.get().isLoaded("continuity")) {
    		AdditionalPlacementsMod.LOGGER.info("Continuity detected, registering continuity BakedModel unwrappers");
    		Unwrapper.registerUnwrapper(model -> {
    			if (model instanceof CtmBakedModel ctm) return ctm.getWrappedModel();
    			else if (model instanceof EmissiveBakedModel emm) return emm.getWrappedModel();
    			else return null;
    		});
    	}
    }
}