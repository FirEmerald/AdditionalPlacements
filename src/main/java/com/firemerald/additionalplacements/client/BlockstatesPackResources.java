package com.firemerald.additionalplacements.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.*;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockstatesPackResources implements IResourcePack
{
	static final ResourceLocation SLAB_BLOCKSTATES = new ResourceLocation(AdditionalPlacementsMod.MOD_ID, "blockstate_templates/slab.json");
	static final ResourceLocation STAIR_BLOCKSTATES = new ResourceLocation(AdditionalPlacementsMod.MOD_ID, "blockstate_templates/stairs.json");
	static final ResourceLocation CARPET_BLOCKSTATES = new ResourceLocation(AdditionalPlacementsMod.MOD_ID, "blockstate_templates/carpet.json");
	static final ResourceLocation PRESSURE_PLATE_BLOCKSTATES = new ResourceLocation(AdditionalPlacementsMod.MOD_ID, "blockstate_templates/pressure_plate.json");
	static final ResourceLocation WEIGHTED_PRESSURE_PLATE_BLOCKSTATES = new ResourceLocation(AdditionalPlacementsMod.MOD_ID, "blockstate_templates/weighted_pressure_plate.json");

	@Override
	public InputStream getRootResource(String p_10294_) throws IOException
	{
		return null;
	}

	@Override
	public InputStream getResource(ResourcePackType packType, ResourceLocation resource) throws IOException
	{
		if (packType != ResourcePackType.CLIENT_RESOURCES) throw new FileNotFoundException("Cannot provide " + resource + ": invalid pack type " + packType);
		else if (!resource.getNamespace().equals(AdditionalPlacementsMod.MOD_ID)) throw new FileNotFoundException("Cannot provide " + resource + ": invalid namespace " + resource.getNamespace());
		else if (!resource.getPath().endsWith(".json")) throw new FileNotFoundException("Cannot provide " + resource + ": invalid file " + resource.getPath());
		else if (resource.getPath().startsWith("blockstates/")) //blockstate json
		{
			String blockName = resource.getPath().substring(12, resource.getPath().length() - 5);
			Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, blockName));
			if (block instanceof VerticalSlabBlock) return Minecraft.getInstance().getResourceManager().getResource(SLAB_BLOCKSTATES).getInputStream();
			else if (block instanceof VerticalStairBlock) return Minecraft.getInstance().getResourceManager().getResource(STAIR_BLOCKSTATES).getInputStream();
			else if (block instanceof AdditionalCarpetBlock) return Minecraft.getInstance().getResourceManager().getResource(CARPET_BLOCKSTATES).getInputStream();
			else if (block instanceof AdditionalPressurePlateBlock) return Minecraft.getInstance().getResourceManager().getResource(PRESSURE_PLATE_BLOCKSTATES).getInputStream();
			else if (block instanceof AdditionalWeightedPressurePlateBlock) return Minecraft.getInstance().getResourceManager().getResource(WEIGHTED_PRESSURE_PLATE_BLOCKSTATES).getInputStream();
			else throw new FileNotFoundException("Cannot provide " + resource + ": invalid block additionalplacements:" + blockName);
		}
		else throw new FileNotFoundException("Cannot provide " + resource + ": invalid file " + resource.getPath());
	}

	@Override
	public Collection<ResourceLocation> getResources(ResourcePackType packType, String p_10285_, String p_10286_, int p_10287_, Predicate<String> p_10288_)
	{
		return Collections.emptyList();
	}

	@Override
	public boolean hasResource(ResourcePackType packType, ResourceLocation resource)
	{
		if (packType != ResourcePackType.CLIENT_RESOURCES) return false;
		else if (!resource.getNamespace().equals(AdditionalPlacementsMod.MOD_ID)) return false;
		else if (!resource.getPath().endsWith(".json")) return false;
		else if (resource.getPath().startsWith("blockstates/")) //blockstate json
		{
			String blockName = resource.getPath().substring(12, resource.getPath().length() - 5);
			Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, blockName));
			if (block instanceof VerticalSlabBlock) return true;
			else if (block instanceof VerticalStairBlock) return true;
			else if (block instanceof AdditionalCarpetBlock) return true;
			else if (block instanceof AdditionalPressurePlateBlock) return true;
			else if (block instanceof AdditionalWeightedPressurePlateBlock) return true;
			else return false;
		}
		else return false;
	}

	@Override
	public Set<String> getNamespaces(ResourcePackType p_10283_)
	{
		return Collections.singleton(AdditionalPlacementsMod.MOD_ID);
	}

	@Override
	public <T> T getMetadataSection(IMetadataSectionSerializer<T> p_10291_) throws IOException
	{
		return null;
	}

	@Override
	public String getName()
	{
		return "Additional Placements blockstate redirection pack";
	}

	@Override
	public void close() {}
}
