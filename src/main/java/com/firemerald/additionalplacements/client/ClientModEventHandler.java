package com.firemerald.additionalplacements.client;

import java.util.function.Consumer;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.client.models.PlacementBlockModelLoader;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.Pack.PackConstructor;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent.RegisterGeometryLoaders;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEventHandler
{
	public static final Pack GENERATED_RESOURCES_PACK = new Pack(
			"Additional Placements blockstate redirection pack",
			true,
			BlockstatesPackResources::new,
			MutableComponent.create(new LiteralContents("title")),
			MutableComponent.create(new LiteralContents("description")),
			PackCompatibility.COMPATIBLE,
			Pack.Position.BOTTOM,
			true,
			PackSource.BUILT_IN,
			true
			);

	@SubscribeEvent
	public static void onAddPackFinders(AddPackFindersEvent event)
	{
		if (event.getPackType() == PackType.CLIENT_RESOURCES)
		{
			event.addRepositorySource(new RepositorySource() {
				@Override
				public void loadPacks(Consumer<Pack> addPack, PackConstructor buildPack)
				{
					addPack.accept(GENERATED_RESOURCES_PACK);
				}
			});
		}
	}

	@SubscribeEvent
	public static void onModelRegistryEvent(RegisterGeometryLoaders event)
	{
		PlacementBlockModelLoader loader = new PlacementBlockModelLoader();
		event.register(PlacementBlockModelLoader.ID.getPath(), loader);
		((ReloadableResourceManager) Minecraft.getInstance().getResourceManager()).registerReloadListener(loader);
	}

    @SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onRegisterBlockColorHandlers(RegisterColorHandlersEvent.Block event)
    {
		event.register(new AdditionalBlockColor(), ForgeRegistries.BLOCKS.getValues().stream().filter(block -> block instanceof AdditionalPlacementBlock && !((AdditionalPlacementBlock<?>) block).hasCustomColors()).toArray(Block[]::new));
    }
}