package com.firemerald.additionalplacements.block.interfaces;

import javax.annotation.Nullable;

import com.firemerald.additionalplacements.block.AdditionalCarpetBlock;
import com.firemerald.additionalplacements.block.AdditionalFloorBlock;
import com.firemerald.additionalplacements.generation.APGenerationTypes;
import com.firemerald.additionalplacements.generation.GenerationType;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.state.BlockState;

public interface ICarpetBlock<T extends Block> extends IFloorBlock<T>
{
	interface IVanillaCarpetBlock extends ICarpetBlock<AdditionalCarpetBlock>, IVanillaBlock<AdditionalCarpetBlock> {}

	@Override
	default BlockState forPlacing(Direction dir, BlockState blockState)
	{
    	if (dir == Direction.DOWN) return getDefaultVanillaState(blockState);
    	else return getDefaultAdditionalState(blockState).setValue(AdditionalFloorBlock.PLACING, dir);
	}

	@Override
	@Nullable
	default Direction getPlacing(BlockState blockState)
	{
		if (blockState.getBlock() instanceof CarpetBlock) return Direction.DOWN;
		else return blockState.getValue(AdditionalFloorBlock.PLACING);
	}

	@Override
	default GenerationType<?, ?> getGenerationType() {
		return APGenerationTypes.carpet();
	}
}