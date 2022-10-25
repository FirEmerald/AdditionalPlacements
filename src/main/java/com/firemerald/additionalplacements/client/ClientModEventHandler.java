package com.firemerald.additionalplacements.client;

import java.util.function.Consumer;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.client.models.PlacementBlockModelLoader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.Pack.PackConstructor;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEventHandler
{
	public static final Pack GENERATED_RESOURCES_PACK = new Pack(
			"Additional Placements blockstate redirection pack",
			true,
			BlockstatesPackResources::new,
			new TextComponent("title"),
			new TextComponent("description"),
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
	public static void onModelRegistryEvent(ModelRegistryEvent event)
	{
		ModelLoaderRegistry.registerLoader(PlacementBlockModelLoader.ID, new PlacementBlockModelLoader());
	}

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void init(FMLClientSetupEvent event)
    {
    	ForgeRegistries.BLOCKS.forEach(block -> {
    		if (block instanceof AdditionalPlacementBlock)
    		{
    			@SuppressWarnings("deprecation")
				BlockState modelState = ((AdditionalPlacementBlock<?>) block).getModelState();
    			ItemBlockRenderTypes.setRenderLayer(block, (layer) -> ItemBlockRenderTypes.canRenderInLayer(modelState, layer));
    		}
    	});
    	Minecraft.getInstance().getBlockColors().register(new AdditionalBlockColor(), ForgeRegistries.BLOCKS.getValues().stream().filter(block -> block instanceof AdditionalPlacementBlock && !((AdditionalPlacementBlock<?>) block).hasCustomColors()).toArray(Block[]::new));
    }
}