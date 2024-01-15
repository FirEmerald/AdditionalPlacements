package com.firemerald.additionalplacements.client;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.client.models.BakedParticleDeferredBlockModel;
import com.firemerald.additionalplacements.client.models.PlacementBlockModelLoader;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent.RegisterGeometryLoaders;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEventHandler
{
	static KeyMapping AP_KEY;

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void keyEVent(RegisterKeyMappingsEvent event)
	{
		AP_KEY = new KeyMapping(
				"key.toggle_additional_placements",
				InputConstants.Type.KEYSYM,
				InputConstants.UNKNOWN.getValue(),
				"key.categories.misc");

		event.register(AP_KEY);
	}

	public static final Pack GENERATED_RESOURCES_PACK = Pack.create(
			"Additional Placements blockstate redirection pack",
			Component.literal("title"),
			true,
			unknown -> new BlockstatesPackResources(),
			new Pack.Info(Component.literal("description"), 9, 8, FeatureFlagSet.of(), true),
			PackType.CLIENT_RESOURCES,
			Pack.Position.BOTTOM,
			true,
			PackSource.BUILT_IN
			);
	private static PlacementBlockModelLoader loader;

	@SubscribeEvent
	public static void onAddPackFinders(AddPackFindersEvent event)
	{
		if (event.getPackType() == PackType.CLIENT_RESOURCES) event.addRepositorySource(addPack -> addPack.accept(GENERATED_RESOURCES_PACK));
	}

	@SubscribeEvent
	public static void onModelRegistryEvent(RegisterGeometryLoaders event)
	{
		loader = new PlacementBlockModelLoader();
		event.register(PlacementBlockModelLoader.ID.getPath(), loader);
	}

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event)
	{
		((ReloadableResourceManager) Minecraft.getInstance().getResourceManager()).registerReloadListener(loader);
    	((ReloadableResourceManager) Minecraft.getInstance().getResourceManager()).registerReloadListener((ResourceManagerReloadListener) (resourceManager -> BakedParticleDeferredBlockModel.clearCache()));
	}

    @SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onRegisterBlockColorHandlers(RegisterColorHandlersEvent.Block event)
    {
		event.register(new AdditionalBlockColor(), ForgeRegistries.BLOCKS.getValues().stream().filter(block -> block instanceof AdditionalPlacementBlock && !((AdditionalPlacementBlock<?>) block).hasCustomColors()).toArray(Block[]::new));
    }
}