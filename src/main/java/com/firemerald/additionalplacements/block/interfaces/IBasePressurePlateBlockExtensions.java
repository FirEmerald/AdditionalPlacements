package com.firemerald.additionalplacements.block.interfaces;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public interface IBasePressurePlateBlockExtensions
{
	void playOnSoundPublic(IWorld level, BlockPos pos);

	void playOffSoundPublic(IWorld level, BlockPos pos);

	int getSignalForStatePublic(BlockState state);

	BlockState setSignalForStatePublic(BlockState state, int strength);

	int getPressedTimePublic();
}