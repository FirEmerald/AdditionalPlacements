package com.firemerald.additionalplacements.block;

import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public abstract class AdditionalPlacementLiquidBlock<T extends Block & BucketPickup & LiquidBlockContainer> extends AdditionalPlacementBlock<T> implements BucketPickup, LiquidBlockContainer
{
	public AdditionalPlacementLiquidBlock(T parentBlock)
	{
		super(parentBlock);
	}

	@Override
	public ItemStack pickupBlock(@Nullable Player player, LevelAccessor level, BlockPos pos, BlockState blockState)
	{
		ItemStack ret = this.getOtherBlock().pickupBlock(player, level, pos, this.getModelState(blockState));
		level.setBlock(pos, this.copyProperties(level.getBlockState(pos), blockState), 3);
		return ret;
	}

	@Override
	public Optional<SoundEvent> getPickupSound()
	{
		return this.getOtherBlock().getPickupSound();
	}

	@Override
	public boolean canPlaceLiquid(@Nullable Player player, BlockGetter level, BlockPos pos, BlockState blockState, Fluid fluid)
	{
		return this.getOtherBlock().canPlaceLiquid(player, level, pos, getModelState(blockState), fluid);
	}

	@Override
	public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState blockState, FluidState fluidState)
	{
		boolean flag = this.getOtherBlock().placeLiquid(level, pos, getModelState(blockState), fluidState);
		level.setBlock(pos, this.copyProperties(level.getBlockState(pos), blockState), 3);
		return flag;
	}

	@Override
	@Deprecated
	public BlockState updateShape(BlockState state, Direction direction, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos)
	{
		FluidState fluid = level.getFluidState(pos);
		if (!fluid.isEmpty()) level.scheduleTick(pos, fluid.getType(), fluid.getType().getTickDelay(level));
		return super.updateShape(state, direction, otherState, level, pos, otherPos);
	}
}
