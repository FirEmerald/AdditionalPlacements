package com.firemerald.vsas.client;

import com.firemerald.vsas.block.ICustomBlockHighlight;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawSelectionEvent.HighlightBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEventHandler
{
	@SubscribeEvent
	public static void onHighlightBlock(HighlightBlock event)
	{
		@SuppressWarnings("resource")
		Player player = Minecraft.getInstance().player;
		ItemStack stack = player.getMainHandItem();
		if (stack.isEmpty()) stack = player.getOffhandItem();
		if (stack.getItem() instanceof ICustomBlockHighlight)
		{
			IBlockHighlight highlight = ((ICustomBlockHighlight) stack.getItem()).getBlockHighlight(player, event.getTarget());
			if (highlight != null) highlight.render(event.getPoseStack(), event.getMultiBufferSource().getBuffer(RenderType.LINES), player, event.getTarget(), event.getCamera(), event.getPartialTicks());
		}
		else if (stack.getItem() instanceof BlockItem)
		{
			Block block = ((BlockItem) stack.getItem()).getBlock();
			if (block instanceof ICustomBlockHighlight)
			{
				IBlockHighlight highlight = ((ICustomBlockHighlight) block).getBlockHighlight(player, event.getTarget());
				if (highlight != null) highlight.render(event.getPoseStack(), event.getMultiBufferSource().getBuffer(RenderType.LINES), player, event.getTarget(), event.getCamera(), event.getPartialTicks());
			}
		}
	}
}