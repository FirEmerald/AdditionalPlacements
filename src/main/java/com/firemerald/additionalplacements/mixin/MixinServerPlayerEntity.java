package com.firemerald.additionalplacements.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.firemerald.additionalplacements.common.IAPServerPlayer;
import com.firemerald.additionalplacements.config.APConfigs;

import net.minecraft.entity.player.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity implements IAPServerPlayer
{
	private boolean placementEnabled = APConfigs.server().fakePlayerPlacement.get();

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