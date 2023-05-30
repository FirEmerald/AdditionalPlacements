package com.firemerald.additionalplacements.block.interfaces;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public interface IBasePressurePlateBlock
{
	public abstract void playOnSoundPublic(IWorld level, BlockPos pos);
	
	public abstract void playOffSoundPublic(IWorld level, BlockPos pos);
	
	public abstract int getSignalForStatePublic(BlockState state);

	public abstract BlockState setSignalForStatePublic(BlockState state, int strength);
	
	public abstract int getPressedTimePublic();
}