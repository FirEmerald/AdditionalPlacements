package com.firemerald.additionalplacements.block.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public interface IBasePressurePlateBlockExtensions
{
	void playOnSoundPublic(LevelAccessor level, BlockPos pos);

	void playOffSoundPublic(LevelAccessor level, BlockPos pos);

	int getSignalForStatePublic(BlockState state);

	BlockState setSignalForStatePublic(BlockState state, int strength);

	int getPressedTimePublic();
}