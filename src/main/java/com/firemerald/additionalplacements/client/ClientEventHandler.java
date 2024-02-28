package com.firemerald.additionalplacements.client;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.interfaces.IPlacementBlock;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
@OnlyIn(Dist.CLIENT)
public class ClientEventHandler
{
	@SubscribeEvent
	public static void onHighlightBlock(RenderHighlightEvent.Block event)
	{
		@SuppressWarnings("resource")
		Player player = Minecraft.getInstance().player;
		ItemStack stack = player.getMainHandItem();
		if (stack.isEmpty()) stack = player.getOffhandItem();
		if (stack.getItem() instanceof BlockItem)
		{
			Block block = ((BlockItem) stack.getItem()).getBlock();
			if (block instanceof IPlacementBlock)
			{
				IPlacementBlock<?> verticalBlock = ((IPlacementBlock<?>) block);
				if (verticalBlock.hasAdditionalStates()) verticalBlock.renderHighlight(event.getPoseStack(), event.getMultiBufferSource().getBuffer(RenderType.LINES), player, event.getTarget(), event.getCamera(), event.getPartialTick());
			}
		}
	}

	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onInput(InputEvent event)
	{
		if (Minecraft.getInstance().player == null) return;
		if (APClientData.AP_PLACEMENT_KEY.consumeClick() && !APClientData.placementKeyDown)
		{
			APClientData.togglePlacementEnabled();
			APClientData.placementKeyPressTime = System.currentTimeMillis();
			APClientData.placementKeyDown = true;
		}
		else if (APClientData.placementKeyDown && !APClientData.AP_PLACEMENT_KEY.isDown()) //released
		{
			APClientData.placementKeyDown = false;
			if ((System.currentTimeMillis() - APClientData.placementKeyPressTime) > 500) //more than half-second press, toggle back
			{
				APClientData.togglePlacementEnabled();
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggingIn(ClientPlayerNetworkEvent.LoggingIn event)
	{
		APClientData.setPlacementEnabledAndSynchronize(AdditionalPlacementsMod.CLIENT_CONFIG.defaultPlacementLogicState.get());
	}

	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event)
	{
		if (Minecraft.getInstance().player == null) return;
		if (event.phase == TickEvent.Phase.END)
		{
			if ((System.currentTimeMillis() - APClientData.lastSynchronizedTime) > 10000) //synchronize every 10 seconds in case of desync
			{
				APClientData.synchronizePlacementEnabled();
			}
		}
	}
}