package com.firemerald.additionalplacements.block.interfaces;

import javax.annotation.Nullable;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.AdditionalCarpetBlock;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.state.BlockState;

public interface ICarpetBlock extends IFloorBlock
{
	public static interface IVanillaCarpetBlock extends ICarpetBlock
	{
		public void setCarpet(AdditionalCarpetBlock carpet);
	}

	public default BlockState forPlacing(Direction dir, BlockState blockState)
	{
    	if (dir == Direction.DOWN) return getDefaultVanillaState(blockState);
    	else return getDefaultAdditionalState(blockState).setValue(AdditionalCarpetBlock.PLACING, dir);
	}

	@Nullable
	public default Direction getPlacing(BlockState blockState)
	{
		if (blockState.getBlock() instanceof CarpetBlock) return Direction.DOWN;
		else return blockState.getValue(AdditionalCarpetBlock.PLACING);
	}

    @Override
	public default boolean disablePlacement()
	{
		return this instanceof CarpetBlock && AdditionalPlacementsMod.COMMON_CONFIG.disableAutomaticSlabPlacement.get();
	}
}