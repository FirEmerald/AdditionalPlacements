package com.firemerald.vsas.block;

import com.firemerald.vsas.client.IBlockHighlight;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ICustomBlockHighlight
{
	@OnlyIn(Dist.CLIENT)
	public IBlockHighlight getBlockHighlight(Player player, BlockHitResult trace);
}