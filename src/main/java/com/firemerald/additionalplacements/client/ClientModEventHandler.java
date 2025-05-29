package com.firemerald.additionalplacements.client;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;

import com.firemerald.additionalplacements.client.models.BakedRetexturedPlacementModel;
import com.firemerald.additionalplacements.client.models.BakedRotatedPlacementModel;
import com.firemerald.additionalplacements.client.models.BakingCache;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.resource.VanillaResourceType;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
@OnlyIn(Dist.CLIENT)
public class ClientModEventHandler
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void init(FMLClientSetupEvent event) {
    	ForgeRegistries.BLOCKS.forEach(block -> {
    		if (block instanceof AdditionalPlacementBlock)
    		{
				BlockState otherState = ((AdditionalPlacementBlock<?>) block).getOtherBlockState();
    			RenderTypeLookup.setRenderLayer(block, (layer) -> RenderTypeLookup.canRenderInLayer(otherState, layer));
    		}
    	});
    	Minecraft.getInstance().getBlockColors().register(new AdditionalBlockColor(), ForgeRegistries.BLOCKS.getValues().stream().filter(block -> block instanceof AdditionalPlacementBlock && !((AdditionalPlacementBlock<?>) block).hasCustomColors()).toArray(Block[]::new));
    	ClientRegistry.registerKeyBinding(APClientData.AP_PLACEMENT_KEY);
    	((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).registerReloadListener((ISelectiveResourceReloadListener) (resourceManager, resourcePredicate) -> {
    		if (resourcePredicate.test(VanillaResourceType.MODELS)) {
				BakingCache.clearCache();
				BakedRotatedPlacementModel.clearCache();
				BakedRetexturedPlacementModel.clearCache();
			}
    	});
    }
}