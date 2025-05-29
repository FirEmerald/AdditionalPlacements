package com.firemerald.additionalplacements.block;

import java.util.Optional;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public abstract class AdditionalPlacementLiquidBlock<T extends Block & BucketPickup & LiquidBlockContainer> extends AdditionalPlacementBlock<T> implements BucketPickup, LiquidBlockContainer
{
	public AdditionalPlacementLiquidBlock(T parentBlock, ResourceKey<Block> id)
	{
		super(parentBlock, id);
	}

	@Override
	public ItemStack pickupBlock(@Nullable LivingEntity owner, LevelAccessor level, BlockPos pos, BlockState blockState)
	{
		ItemStack ret = this.getOtherBlock().pickupBlock(owner, level, pos, this.getModelState(blockState));
		level.setBlock(pos, this.copyProperties(level.getBlockState(pos), blockState), 3);
		return ret;
	}

	@Override
	public Optional<SoundEvent> getPickupSound()
	{
		return this.getOtherBlock().getPickupSound();
	}

	@Override
	public boolean canPlaceLiquid(@Nullable LivingEntity owner, BlockGetter level, BlockPos pos, BlockState blockState, Fluid fluid)
	{
		return this.getOtherBlock().canPlaceLiquid(owner, level, pos, getModelState(blockState), fluid);
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
	public BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos otherPos, BlockState otherState, RandomSource rand)
	{
		FluidState fluid = level.getFluidState(pos);
		if (!fluid.isEmpty()) tickAccess.scheduleTick(pos, fluid.getType(), fluid.getType().getTickDelay(level));
		return super.updateShape(state, level, tickAccess, pos, direction, otherPos, otherState, rand);
	}
}
