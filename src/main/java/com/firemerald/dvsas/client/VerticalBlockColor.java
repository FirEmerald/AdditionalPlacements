package com.firemerald.dvsas.client;

import com.firemerald.dvsas.block.VerticalBlock;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class VerticalBlockColor implements BlockColor
{
	@Override
	public int getColor(BlockState state, BlockAndTintGetter tintGetter, BlockPos pos, int i)
	{
		Block block = state.getBlock();
		if (block instanceof VerticalBlock) return Minecraft.getInstance().getBlockColors().getColor(((VerticalBlock<?>) block).getModelState(state), tintGetter, pos, i);
		else return -1;
	}
}