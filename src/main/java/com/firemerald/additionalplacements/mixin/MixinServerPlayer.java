package com.firemerald.additionalplacements.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.common.IAPServerPlayer;

import net.minecraft.server.level.ServerPlayer;

@Mixin(ServerPlayer.class)
public class MixinServerPlayer implements IAPServerPlayer
{
	public boolean placementEnabled = AdditionalPlacementsMod.SERVER_CONFIG.fakePlayerPlacement.get();

	@Override
	public boolean isPlacementEnabled()
	{
		return placementEnabled;
	}

	@Override
	public void setPlacementEnabled(boolean state)
	{
		this.placementEnabled = state;
	}
}