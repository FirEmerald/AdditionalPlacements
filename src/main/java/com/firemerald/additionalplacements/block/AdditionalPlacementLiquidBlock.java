package com.firemerald.additionalplacements.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public abstract class AdditionalPlacementLiquidBlock<T extends Block & IBucketPickupHandler & ILiquidContainer> extends AdditionalPlacementBlock<T> implements IBucketPickupHandler, ILiquidContainer
{
	public AdditionalPlacementLiquidBlock(T parentBlock)
	{
		super(parentBlock);
	}

	@Override
	public Fluid takeLiquid(IWorld level, BlockPos pos, BlockState blockState)
	{
		Fluid ret = this.getOtherBlock().takeLiquid(level, pos, this.getModelState(blockState));
		level.setBlock(pos, this.copyProperties(level.getBlockState(pos), blockState), 3);
		return ret;
	}

	/*
	@Override
    public Optional<SoundEvent> getPickupSound(BlockState blockState)
    {
		return this.getOtherBlock().getPickupSound(this.getModelState(blockState));
    }

	@Override
	@Deprecated
	public Optional<SoundEvent> getPickupSound()
	{
		return this.getOtherBlock().getPickupSound();
	}
	*/

	@Override
	public boolean canPlaceLiquid(IBlockReader level, BlockPos pos, BlockState blockState, Fluid fluid)
	{
		return this.getOtherBlock().canPlaceLiquid(level, pos, getModelState(blockState), fluid);
	}

	@Override
	public boolean placeLiquid(IWorld level, BlockPos pos, BlockState blockState, FluidState fluidState)
	{
		boolean flag = this.getOtherBlock().placeLiquid(level, pos, getModelState(blockState), fluidState);
		level.setBlock(pos, this.copyProperties(level.getBlockState(pos), blockState), 3);
		return flag;
	}

	@Override
	@Deprecated
	public BlockState updateShape(BlockState state, Direction direction, BlockState otherState, IWorld level, BlockPos pos, BlockPos otherPos)
	{
		FluidState fluid = level.getFluidState(pos);
		if (!fluid.isEmpty()) level.getLiquidTicks().scheduleTick(pos, fluid.getType(), fluid.getType().getTickDelay(level));
		return super.updateShape(state, direction, otherState, level, pos, otherPos);
	}
}
