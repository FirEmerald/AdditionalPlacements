package com.firemerald.additionalplacements.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Set;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.*;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

@OnlyIn(Dist.CLIENT)
public class BlockstatesPackResources implements PackResources
{
	@Override
	public IoSupplier<InputStream> getRootResource(String... p_10294_)
	{
		return null;
	}

	@Override
	public IoSupplier<InputStream> getResource(PackType packType, ResourceLocation resource)
	{
		if (packType != PackType.CLIENT_RESOURCES) return null;
		else if (!resource.getNamespace().equals(AdditionalPlacementsMod.MOD_ID)) return null;
		else if (!resource.getPath().endsWith(".json")) return null;
		else if (resource.getPath().startsWith("blockstates/")) //blockstate json
		{
			String blockName = resource.getPath().substring(12, resource.getPath().length() - 5);
			Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, blockName));
			IoSupplier<InputStream> ioResource = getResourceFor(block);
			if (ioResource != null) return ioResource;
			else return null;
		}
		else return null;
	}

	@Override
	public void listResources(PackType packType, String domain, String path, ResourceOutput filter)
	{
		if (packType == PackType.CLIENT_RESOURCES && AdditionalPlacementsMod.MOD_ID.equals(domain) && "blockstates".equals(path))
		{
			ForgeRegistries.BLOCKS.getEntries().forEach(entry -> {
				ResourceLocation id = entry.getKey().location();
				if (id.getNamespace().equals(AdditionalPlacementsMod.MOD_ID))
				{
					IoSupplier<InputStream> ioResource = getResourceFor(entry.getValue());
					if (ioResource != null) filter.accept(new ResourceLocation(id.getNamespace(), "blockstates/" + id.getPath() + ".json"), ioResource);
				}
			});
		}
	}

	public static IoSupplier<InputStream> getResourceFor(Block block)
	{
		ResourceLocation loc = getBlockstateJson(block);
		return loc == null ? null : () -> Minecraft.getInstance().getResourceManager().getResource(loc).get().open();
	}

	public static ResourceLocation getBlockstateJson(Block block)
	{
		if (block instanceof AdditionalPlacementBlock) return ((AdditionalPlacementBlock<?>) block).getDynamicBlockstateJson();
		else return null;
	}

	@Override
	public Set<String> getNamespaces(PackType p_10283_)
	{
		return Collections.singleton(AdditionalPlacementsMod.MOD_ID);
	}

	@Override
	public <T> T getMetadataSection(MetadataSectionSerializer<T> p_10291_) throws IOException
	{
		return null;
	}

	@Override
	public String packId()
	{
		return "Additional Placements blockstate redirection pack";
	}

	@Override
	public void close() {}

	@Override
	public boolean isBuiltin()
	{
		return true;
	}

	@Override
	public boolean isHidden()
	{
		return true;
	}
}
