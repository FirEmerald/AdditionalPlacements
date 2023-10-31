package com.firemerald.additionalplacements.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.firemerald.additionalplacements.block.interfaces.IBasePressurePlateBlockExtensions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(BasePressurePlateBlock.class)
public abstract class MixinBasePressurePlateBlock implements IBasePressurePlateBlockExtensions
{
	@Shadow
	protected abstract void playOnSound(LevelAccessor pLevel, BlockPos pPos);

	@Shadow
	protected abstract void playOffSound(LevelAccessor pLevel, BlockPos pPos);

	@Shadow
	protected abstract BlockState setSignalForState(BlockState pState, int pStrength);

	@Shadow
	protected abstract int getSignalForState(BlockState pState);

	@Shadow
	protected abstract int getPressedTime();

	@Override
	public void playOnSoundPublic(LevelAccessor pLevel, BlockPos pPos)
	{
		playOnSound(pLevel, pPos);
	}

	@Override
	public void playOffSoundPublic(LevelAccessor pLevel, BlockPos pPos)
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