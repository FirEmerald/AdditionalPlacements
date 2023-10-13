package com.firemerald.additionalplacements.block.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public interface IBasePressurePlateBlockExtensions
{
	public abstract void playOnSoundPublic(LevelAccessor level, BlockPos pos);
	
	public abstract void playOffSoundPublic(LevelAccessor level, BlockPos pos);
	
	public abstract int getSignalForStatePublic(BlockState state);

	public abstract BlockState setSignalForStatePublic(BlockState state, int strength);
	
	public abstract int getPressedTimePublic();
}