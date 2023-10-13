package com.firemerald.additionalplacements.block.interfaces;

import javax.annotation.Nullable;

import com.firemerald.additionalplacements.block.AdditionalBasePressurePlateBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.util.Direction;

public interface IBasePressurePlateBlock<T extends Block> extends IFloorBlock<T>
{
	public static interface IVanillaBasePressurePlateBlock<T extends AdditionalBasePressurePlateBlock<?>> extends IBasePressurePlateBlock<T>, IVanillaBlock<T> {}

	public default BlockState forPlacing(Direction dir, BlockState blockState)
	{
    	if (dir == Direction.DOWN) return getDefaultVanillaState(blockState);
    	else return getDefaultAdditionalState(blockState).setValue(AdditionalBasePressurePlateBlock.PLACING, dir);
	}

	@Nullable
	public default Direction getPlacing(BlockState blockState)
	{
		if (blockState.getBlock() instanceof PressurePlateBlock) return Direction.DOWN;
		else return blockState.getValue(AdditionalBasePressurePlateBlock.PLACING);
	}
}