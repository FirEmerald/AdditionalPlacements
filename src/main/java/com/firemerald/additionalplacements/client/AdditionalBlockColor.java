package com.firemerald.additionalplacements.client;

import javax.annotation.Nullable;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AdditionalBlockColor implements IBlockColor
{
	@Override
	public int getColor(BlockState state, @Nullable IBlockDisplayReader tintGetter, BlockPos pos, int i)
	{
		Block block = state.getBlock();
		if (block instanceof AdditionalPlacementBlock) return Minecraft.getInstance().getBlockColors().getColor(((AdditionalPlacementBlock<?>) block).getModelState(state), tintGetter, pos, i);
		else return -1;
	}
}