package com.firemerald.vsas.common;

import com.firemerald.vsas.block.IVerticalBlock;
import com.firemerald.vsas.block.VerticalBlock;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
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
				((IVerticalBlock) block).appendHoverTextImpl(event.getItemStack(), event.getPlayer() == null ? null : event.getPlayer().getLevel(), event.getToolTip(), event.getFlags());
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
}