package com.firemerald.dvsas.common;

import com.firemerald.dvsas.DVSaSMod;
import com.firemerald.dvsas.block.IVerticalBlock;
import com.firemerald.dvsas.block.VerticalBlock;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.MissingMappingsEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEventHandler
{
	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event)
	{
		if (event.getItemStack().getItem() instanceof BlockItem)
		{
			Block block = ((BlockItem) event.getItemStack().getItem()).getBlock();
			if (block instanceof IVerticalBlock)
			{
				IVerticalBlock verticalBlock = ((IVerticalBlock) block);
				if (verticalBlock.hasVertical()) verticalBlock.appendHoverTextImpl(event.getItemStack(), event.getEntity() == null ? null : event.getEntity().getLevel(), event.getToolTip(), event.getFlags());
			}
		}
	}

	@SubscribeEvent
	public static void onTagsUpdated(TagsUpdatedEvent event)
	{
		ForgeRegistries.BLOCKS.forEach(block -> {
			if (block instanceof VerticalBlock) ((VerticalBlock<?>) block).bindTags();
		});
	}

	@SubscribeEvent
	public static void onMissingBlockMappings(MissingMappingsEvent event)
	{
		event.getMappings(ForgeRegistries.BLOCKS.getRegistryKey(), DVSaSMod.MOD_ID).forEach(mapping -> {
			String oldPath = mapping.getKey().getPath();
			if (oldPath.indexOf('.') < 0)
			{
				String newPath = "minecraft." + oldPath;
				Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(DVSaSMod.MOD_ID, newPath));
				if (block != Blocks.AIR) mapping.remap(block);
			}
		});
	}
}