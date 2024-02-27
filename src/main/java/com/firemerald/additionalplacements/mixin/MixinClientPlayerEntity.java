package com.firemerald.additionalplacements.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.firemerald.additionalplacements.client.APClientData;
import com.firemerald.additionalplacements.common.IAPPlayer;

import net.minecraft.client.entity.player.ClientPlayerEntity;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity implements IAPPlayer
{
	@Override
	public boolean isPlacementEnabled()
	{
		return APClientData.placementEnabled();
	}
}