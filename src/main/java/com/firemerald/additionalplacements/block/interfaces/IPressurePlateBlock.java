package com.firemerald.additionalplacements.block.interfaces;

import javax.annotation.Nullable;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.AdditionalBasePressurePlateBlock;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.BlockState;

public interface IPressurePlateBlock<T extends BasePressurePlateBlock> extends IFloorBlock
{
	public static interface IVanillaPressurePlateBlock<T extends BasePressurePlateBlock> extends IPressurePlateBlock<T>
	{
		public void setPressurePlate(AdditionalBasePressurePlateBlock<T> plate);
	}

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

    @Override
	public default boolean disablePlacement()
	{
		return this instanceof PressurePlateBlock && AdditionalPlacementsMod.COMMON_CONFIG.disableAutomaticSlabPlacement.get();
	}
}