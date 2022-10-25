package com.firemerald.additionalplacements.block.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public interface IBasePressurePlateBlock
{
	public abstract void playOnSoundPublic(LevelAccessor pLevel, BlockPos pPos);
	
	public abstract void playOffSoundPublic(LevelAccessor pLevel, BlockPos pPos);
	
	public abstract int getSignalForStatePublic(BlockState pState);

	public abstract BlockState setSignalForStatePublic(BlockState pState, int pStrength);
	
	public abstract int getPressedTimePublic();
}