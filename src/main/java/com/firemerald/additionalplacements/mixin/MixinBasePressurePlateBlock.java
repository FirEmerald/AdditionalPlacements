package com.firemerald.additionalplacements.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.firemerald.additionalplacements.block.interfaces.IBasePressurePlateBlockExtensions;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;

@Mixin(BasePressurePlateBlock.class)
public abstract class MixinBasePressurePlateBlock implements IBasePressurePlateBlockExtensions
{
	@Shadow
	private BlockSetType type;

	@Shadow
	protected abstract BlockState setSignalForState(BlockState pState, int pStrength);

	@Shadow
	protected abstract int getSignalForState(BlockState pState);

	@Shadow
	protected abstract int getPressedTime();

	@Override
	public void playOnSoundPublic(LevelAccessor pLevel, BlockPos pPos)
	{
		pLevel.playSound(null, pPos, type.pressurePlateClickOn(), SoundSource.BLOCKS);
	}

	@Override
	public void playOffSoundPublic(LevelAccessor pLevel, BlockPos pPos)
	{
		pLevel.playSound(null, pPos, type.pressurePlateClickOff(), SoundSource.BLOCKS);
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