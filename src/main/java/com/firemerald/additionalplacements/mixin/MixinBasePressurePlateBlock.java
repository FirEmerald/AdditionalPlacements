package com.firemerald.additionalplacements.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.firemerald.additionalplacements.block.interfaces.IBasePressurePlateBlockExtensions;

import net.minecraft.block.AbstractPressurePlateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

@Mixin(AbstractPressurePlateBlock.class)
public abstract class MixinBasePressurePlateBlock implements IBasePressurePlateBlockExtensions
{
	@Shadow
	protected abstract void playOnSound(IWorld pLevel, BlockPos pPos);

	@Shadow
	protected abstract void playOffSound(IWorld pLevel, BlockPos pPos);
	
	@Shadow
	protected abstract BlockState setSignalForState(BlockState pState, int pStrength);

	@Shadow
	protected abstract int getSignalForState(BlockState pState);

	@Shadow
	protected abstract int getPressedTime();

	@Override
	public void playOnSoundPublic(IWorld pLevel, BlockPos pPos)
	{
		playOnSound(pLevel, pPos);
	}

	@Override
	public void playOffSoundPublic(IWorld pLevel, BlockPos pPos)
	{
		playOffSound(pLevel, pPos);
	}
	
	@Override
	public BlockState setSignalForStatePublic(BlockState pState, int pStrength)
	{
		return setSignalForState(pState, pStrength);
	}

	@Override
	public int getSignalForStatePublic(BlockState pState)
	{
		return getSignalForState(pState);
	}

	@Override
	public int getPressedTimePublic()
	{
		return getPressedTime();
	}
}