package com.firemerald.additionalplacements.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.firemerald.additionalplacements.client.APClientData;
import com.firemerald.additionalplacements.common.IAPPlayer;

import net.minecraft.client.player.LocalPlayer;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer implements IAPPlayer
{
	@Override
	public boolean isPlacementEnabled()
	{
		return APClientData.placementEnabled();
	}
}