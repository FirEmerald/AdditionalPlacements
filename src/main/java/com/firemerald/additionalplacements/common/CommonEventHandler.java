package com.firemerald.additionalplacements.common;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.interfaces.IPlacementBlock;
import com.firemerald.additionalplacements.command.CommandExportTags;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Util;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEventHandler
{
	public static boolean misMatchedTags = false;
	
	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event)
	{
		if (event.getItemStack().getItem() instanceof BlockItem)
		{
			Block block = ((BlockItem) event.getItemStack().getItem()).getBlock();
			if (block instanceof IPlacementBlock)
			{
				IPlacementBlock<?> verticalBlock = ((IPlacementBlock<?>) block);
				if (verticalBlock.hasAdditionalStates()) verticalBlock.appendHoverTextImpl(event.getItemStack(), event.getPlayer() == null ? null : event.getPlayer().level, event.getToolTip(), event.getFlags());
			}
		}
	}
	
	@SubscribeEvent
	public static void onRegisterCommands(RegisterCommandsEvent event)
	{
		CommandExportTags.register(event.getDispatcher());
	}

	@SubscribeEvent
	public static void onTagsUpdated(TagsUpdatedEvent event)
	{
		misMatchedTags = false;
		if (AdditionalPlacementsMod.COMMON_CONFIG.checkTags.get() && AdditionalPlacementsMod.SERVER_CONFIG.checkTags.get())
			TagMismatchChecker.startChecker(); //TODO halt on datapack reload
	}
	
	@SubscribeEvent
	public static void onPlayerLogin(PlayerLoggedInEvent event)
	{
		if (misMatchedTags && event.getPlayer().hasPermissions(2)) event.getPlayer().sendMessage(TagMismatchChecker.MESSAGE, Util.NIL_UUID);
	}
}