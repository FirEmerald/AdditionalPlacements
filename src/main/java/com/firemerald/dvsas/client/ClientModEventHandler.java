package com.firemerald.dvsas.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.firemerald.dvsas.DVSaSMod;
import com.firemerald.dvsas.block.VerticalBlock;
import com.firemerald.dvsas.block.VerticalSlabBlock;
import com.firemerald.dvsas.block.VerticalStairBlock;
import com.firemerald.dvsas.client.models.VerticalBlockModelLoader;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.Pack.PackConstructor;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.ModelEvent.RegisterGeometryLoaders;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEventHandler
{
	public static final Pack GENERATED_RESOURCES_PACK = new Pack(
			"Dynamic Vertical Slabs and Stairs blockstate redirection pack",
			true,
			() -> new PackResources() {
				static final ResourceLocation SLAB_BLOCKSTATES = new ResourceLocation(DVSaSMod.MOD_ID, "blockstate_templates/slab.json");
				static final ResourceLocation STAIR_BLOCKSTATES = new ResourceLocation(DVSaSMod.MOD_ID, "blockstate_templates/stairs.json");

				@Override
				public InputStream getRootResource(String p_10294_) throws IOException
				{
					return null;
				}

				@Override
				public InputStream getResource(PackType packType, ResourceLocation resource) throws IOException
				{
					if (packType != PackType.CLIENT_RESOURCES) throw new FileNotFoundException("Cannot provide " + resource + ": invalid pack type " + packType);
					else if (!resource.getNamespace().equals(DVSaSMod.MOD_ID)) throw new FileNotFoundException("Cannot provide " + resource + ": invalid namespace " + resource.getNamespace());
					else if (!resource.getPath().endsWith(".json")) throw new FileNotFoundException("Cannot provide " + resource + ": invalid file " + resource.getPath());
					else if (resource.getPath().startsWith("blockstates/")) //blockstate json
					{
						String blockName = resource.getPath().substring(12, resource.getPath().length() - 5);
						Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(DVSaSMod.MOD_ID, blockName));
						if (block instanceof VerticalSlabBlock) return Minecraft.getInstance().getResourceManager().getResource(SLAB_BLOCKSTATES).get().open();
						else if (block instanceof VerticalStairBlock) return Minecraft.getInstance().getResourceManager().getResource(STAIR_BLOCKSTATES).get().open();
						else throw new FileNotFoundException("Cannot provide " + resource + ": invalid block dvsas:" + blockName);
					}
					else throw new FileNotFoundException("Cannot provide " + resource + ": invalid file " + resource.getPath());
				}

				@Override
				public Collection<ResourceLocation> getResources(PackType packType, String p_215340_, String p_215341_, Predicate<ResourceLocation> filter)
				{
					return Collections.emptySet();
				}

				@Override
				public boolean hasResource(PackType packType, ResourceLocation resource)
				{
					if (packType != PackType.CLIENT_RESOURCES) return false;
					else if (!resource.getNamespace().equals(DVSaSMod.MOD_ID)) return false;
					else if (!resource.getPath().endsWith(".json")) return false;
					else if (resource.getPath().startsWith("blockstates/")) //blockstate json
					{
						String blockName = resource.getPath().substring(12, resource.getPath().length() - 5);
						Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(DVSaSMod.MOD_ID, blockName));
						if (block instanceof VerticalSlabBlock) return true;
						else if (block instanceof VerticalStairBlock) return true;
						else return false;
					}
					else return false;
				}

				@Override
				public Set<String> getNamespaces(PackType p_10283_)
				{
					return Collections.singleton(DVSaSMod.MOD_ID);
				}

				@Override
				public <T> T getMetadataSection(MetadataSectionSerializer<T> p_10291_) throws IOException
				{
					return null;
				}

				@Override
				public String getName()
				{
					return "Dynamic Vertical Slabs and Stairs blockstate redirection pack";
				}

				@Override
				public void close() {}
			},
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
		event.register(VerticalBlockModelLoader.ID.getPath(), new VerticalBlockModelLoader());
	}
	
	@SubscribeEvent
	public static void onRegisterBlockColorHandlers(RegisterColorHandlersEvent.Block event)
	{
		event.register(new VerticalBlockColor(), ForgeRegistries.BLOCKS.getValues().stream().filter(block -> block instanceof VerticalBlock && !((VerticalBlock<?>) block).hasCustomColors()).toArray(Block[]::new));
	}
}