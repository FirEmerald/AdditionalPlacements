package com.firemerald.additionalplacements.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.common.IAPPlayer;

import net.minecraft.entity.player.PlayerEntity;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity implements IAPPlayer
{
	@Override
	public boolean isPlacementEnabled()
	{
		return AdditionalPlacementsMod.SERVER_CONFIG.fakePlayerPlacement.get();
	}
}