package com.firemerald.dvsas.mixin;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.firemerald.dvsas.block.IStairBlock.IVanillStairBlock;
import com.firemerald.dvsas.block.VerticalStairBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

@Mixin(StairBlock.class)
public abstract class MixinStairBlock implements IVanillStairBlock
{
	public VerticalStairBlock stairs;
	@Shadow(remap = false)
	private Supplier<BlockState> stateSupplier;

	public StairBlock asStair()
	{
		return (StairBlock) (Object) this;
	}

	@Override
	public void setStairs(VerticalStairBlock stairs)
	{
		this.stairs = stairs;
	}

	@Override
	public boolean hasVertical()
	{
		return stairs != null;
	}

	@Override
	public boolean isThis(BlockState blockState)
	{
		return blockState.is(asStair()) || blockState.is(stairs);
	}

	@Override
	public BlockState getDefaultHorizontalState(BlockState currentState, FluidState fluidState)
	{
		return currentState.is(asStair()) ? currentState : asStair().defaultBlockState().setValue(StairBlock.WATERLOGGED, fluidState.getType() == Fluids.WATER);
	}

	@Override
	public BlockState getDefaultVerticalState(BlockState currentState, FluidState fluidState)
	{
		return currentState.is(stairs) ? currentState : stairs.defaultBlockState().setValue(VerticalStairBlock.WATERLOGGED, fluidState.getType() == Fluids.WATER);
	}

	@Inject(method = "getStateForPlacement", at = @At("HEAD"), cancellable = true)
	private void getStateForPlacement(BlockPlaceContext context, CallbackInfoReturnable<BlockState> ci)
	{
		if (this.hasVertical()) ci.setReturnValue(getStateForPlacementImpl(context));
	}

	@Inject(method = "rotate", at = @At("HEAD"), cancellable = true)
	private void rotate(BlockState blockState, Rotation rotation, CallbackInfoReturnable<BlockState> ci)
	{
		if (this.hasVertical()) ci.setReturnValue(rotateImpl(blockState, rotation));
	}

	@Inject(method = "mirror", at = @At("HEAD"), cancellable = true)
	private void mirror(BlockState blockState, Mirror mirror, CallbackInfoReturnable<BlockState> ci)
	{
		if (this.hasVertical()) ci.setReturnValue(mirrorImpl(blockState, mirror));
	}

	@Inject(method = "updateShape", at = @At("HEAD"), cancellable = true)
	private void updateShape(BlockState state, Direction direction, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos, CallbackInfoReturnable<BlockState> ci)
	{
		if (this.hasVertical()) ci.setReturnValue(updateShapeImpl(state, direction, otherState, level, pos, otherPos));
	}

	@Override
	public FluidState getFluidStateImpl(BlockState blockState)
	{
		return asStair().getFluidState(blockState);
	}

	@Override
	public BlockState getModelStateImpl()
	{
		return stateSupplier.get();
	}
}