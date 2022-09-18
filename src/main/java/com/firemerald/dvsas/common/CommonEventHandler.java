package com.firemerald.dvsas.common;

import com.firemerald.dvsas.DVSaSMod;
import com.firemerald.dvsas.block.IVerticalBlock;
import com.firemerald.dvsas.block.VerticalBlock;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

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
				if (verticalBlock.hasVertical()) verticalBlock.appendHoverTextImpl(event.getItemStack(), event.getPlayer() == null ? null : event.getPlayer().getLevel(), event.getToolTip(), event.getFlags());
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
	public static void onMissingBlockMappings(RegistryEvent.MissingMappings<Block> event)
	{
		event.getMappings(DVSaSMod.MOD_ID).forEach(mapping -> {
			String oldPath = mapping.key.getPath();
			if (oldPath.indexOf('.') < 0)
			{
				String newPath = "minecraft." + oldPath;
				Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(DVSaSMod.MOD_ID, newPath));
				if (block != Blocks.AIR) mapping.remap(block);
			}
		});
	}
}