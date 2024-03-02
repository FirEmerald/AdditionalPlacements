package com.firemerald.additionalplacements.client;

import java.util.List;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.client.models.BakedParticleDeferredBlockModel;
import com.firemerald.additionalplacements.client.models.PlacementBlockModelLoader;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.Pack.Info;
import net.minecraft.server.packs.repository.Pack.ResourcesSupplier;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent.RegisterGeometryLoaders;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
@OnlyIn(Dist.CLIENT)
public class ClientModEventHandler
{
	//create(String p_252257_, Component p_248717_, boolean p_248811_, Pack.ResourcesSupplier p_248969_, Pack.Info p_251314_, Pack.Position p_252110_, boolean p_250237_, PackSource p_248524_) {
	//public Info(Component description, PackCompatibility compatibility, FeatureFlagSet requestedFeatures, List<String> overlays)
	public static final Pack GENERATED_RESOURCES_PACK = Pack.create(
			"Additional Placements blockstate redirection pack",
			Component.literal("title"),
			true,
			new ResourcesSupplier() { //TODO

				@Override
				public PackResources openPrimary(String p_298664_)
				{
					return new BlockstatesPackResources();
				}

				@Override
				public PackResources openFull(String p_251717_, Info p_298253_)
				{
					return new BlockstatesPackResources();
				}

			},
			new Pack.Info(Component.literal("description"), PackCompatibility.COMPATIBLE, FeatureFlagSet.of(), List.of(), true),
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
		event.register(PlacementBlockModelLoader.ID, loader);
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
		event.register(new AdditionalBlockColor(), BuiltInRegistries.BLOCK.stream().filter(block -> block instanceof AdditionalPlacementBlock && !((AdditionalPlacementBlock<?>) block).hasCustomColors()).toArray(Block[]::new));
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event)
    {
    	event.register(APClientData.AP_PLACEMENT_KEY);
    }
}