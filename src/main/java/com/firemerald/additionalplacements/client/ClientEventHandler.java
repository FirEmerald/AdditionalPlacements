package com.firemerald.additionalplacements.client;

import com.firemerald.additionalplacements.block.interfaces.IPlacementBlock;
import com.firemerald.additionalplacements.client.gui.screen.ConnectionErrorsScreen;
import com.firemerald.additionalplacements.config.APConfigs;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.DrawHighlightEvent.HighlightBlock;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
@OnlyIn(Dist.CLIENT)
public class ClientEventHandler
{
	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event)
	{
		if (event.getItemStack().getItem() instanceof BlockItem)
		{
			Block block = ((BlockItem) event.getItemStack().getItem()).getBlock();
			if (block instanceof IPlacementBlock)
			{
				IPlacementBlock<?> verticalBlock = ((IPlacementBlock<?>) block);
				if (verticalBlock.hasAdditionalStates()) verticalBlock.appendHoverTextImpl(event.getItemStack(), event.getEntity() == null ? null : event.getEntity().level, event.getToolTip(), event.getFlags());
			}
		}
	}

	@SubscribeEvent
	public static void onHighlightBlock(HighlightBlock event)
	{
		if (!APConfigs.client().enablePlacementHighlight.get()) return;
		PlayerEntity player = Minecraft.getInstance().player;
		ItemStack stack = player.getMainHandItem();
		if (stack.isEmpty()) stack = player.getOffhandItem();
		if (stack.getItem() instanceof BlockItem)
		{
			Block block = ((BlockItem) stack.getItem()).getBlock();
			if (block instanceof IPlacementBlock)
			{
				IPlacementBlock<?> verticalBlock = ((IPlacementBlock<?>) block);
				if (verticalBlock.hasAdditionalStates()) verticalBlock.renderHighlight(event.getMatrix(), event.getBuffers().getBuffer(RenderType.LINES), player, event.getTarget(), event.getInfo(), event.getPartialTicks());
			}
		}
	}

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
			if ((System.currentTimeMillis() - APClientData.placementKeyPressTime) > APConfigs.client().toggleQuickpressTime.get()) //more than half-second press, toggle back
			{
				APClientData.togglePlacementEnabled();
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggingIn(ClientPlayerNetworkEvent.LoggedInEvent event)
	{
		APClientData.setPlacementEnabledAndSynchronize(APConfigs.client().defaultPlacementLogicState.get(), APConfigs.client().loginPlacementLogicStateMessage.get());
	}

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

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onScreenOpening(GuiOpenEvent event) {
		if (Minecraft.getInstance().screen instanceof ConnectionErrorsScreen && event.getGui() instanceof DisconnectedScreen) event.setCanceled(true);
	}
}