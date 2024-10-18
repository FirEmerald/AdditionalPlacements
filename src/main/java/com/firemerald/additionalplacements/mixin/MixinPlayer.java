package com.firemerald.additionalplacements.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.firemerald.additionalplacements.common.IAPPlayer;
import com.firemerald.additionalplacements.config.APConfigs;

import net.minecraft.world.entity.player.Player;

@Mixin(Player.class)
public class MixinPlayer implements IAPPlayer
{
	@Override
	public boolean isPlacementEnabled()
	{
		return APConfigs.SERVER.fakePlayerPlacement.get();
	}
}