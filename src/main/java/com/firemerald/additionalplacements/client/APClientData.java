package com.firemerald.additionalplacements.client;

import com.firemerald.additionalplacements.network.PacketSetPlacementToggle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.KeyConflictContext;

@OnlyIn(Dist.CLIENT)
public class APClientData
{
	public static final KeyBinding AP_PLACEMENT_KEY = new KeyBinding("key.additionalplacements.placement_toggle", KeyConflictContext.IN_GAME, InputMappings.UNKNOWN, "key.categories.additionalplacements");

	private static boolean placementEnabled = true;
	public static long placementKeyPressTime, lastSynchronizedTime;
	public static boolean placementKeyDown = false;
	
	public static boolean placementEnabled()
	{
		return placementEnabled;
	}
	
	public static void setPlacementEnabled(boolean state)
	{
		if (state != placementEnabled) togglePlacementEnabled();
	}

	public static void togglePlacementEnabled()
	{
		setPlacementEnabledAndSynchronize(!placementEnabled);
	}

	@SuppressWarnings("resource")
	public static void setPlacementEnabledAndSynchronize(boolean state)
	{
		placementEnabled = state;
		ClientPlayerEntity player = Minecraft.getInstance().player;
		if (player != null)
		{
			synchronizePlacementEnabled();
			player.displayClientMessage(new TranslationTextComponent(placementEnabled ? "msg.additionalplacements.placement_enable" : "msg.additionalplacements.placement_disable"), true);
		}
	}
	
	public static void synchronizePlacementEnabled()
	{
		new PacketSetPlacementToggle(placementEnabled).sendToServer();
	}
}