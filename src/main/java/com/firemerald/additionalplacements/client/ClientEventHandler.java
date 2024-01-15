package com.firemerald.additionalplacements.client;

import com.firemerald.additionalplacements.block.interfaces.IPlacementBlock;

import com.firemerald.additionalplacements.networking.Network;
import com.firemerald.additionalplacements.networking.PacketToggleAP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.firemerald.additionalplacements.client.ClientModEventHandler.AP_KEY;
import static com.firemerald.additionalplacements.AdditionalPlacementsMod.AP_TOGGLE;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
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

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onKeyInput(InputEvent.Key event)
	{
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		if (player == null) return;
		if (AP_KEY.isDown()) {
			boolean b = !player.getPersistentData().getBoolean(AP_TOGGLE);
			toggleClientPlayerAP(b);
			Network.sendToServer(new PacketToggleAP(b));
            player.displayClientMessage(b ? Component.translatable("msg.disable.ap") : Component.translatable("msg.enable.ap"), true);
		}
	}

	public static void toggleClientPlayerAP(boolean ap)
	{
		Player player = Minecraft.getInstance().player;
		player.getPersistentData().putBoolean(AP_TOGGLE, ap);
	}
}