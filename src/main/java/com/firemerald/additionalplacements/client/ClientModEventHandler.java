package com.firemerald.additionalplacements.client;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.client.models.PlacementBlockModelLoader;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
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
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
@OnlyIn(Dist.CLIENT)
public class ClientModEventHandler
{
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
}