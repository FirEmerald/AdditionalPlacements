package com.firemerald.dvsas.mixin;

import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.firemerald.dvsas.block.ISlabBlock.IVanillSlabBlock;
import com.firemerald.dvsas.block.VerticalSlabBlock;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

@Mixin(SlabBlock.class)
public abstract class MixinSlabBlock extends Block implements IVanillSlabBlock
{
	private MixinSlabBlock(Properties properties)
	{
		super(properties);
	}

	public VerticalSlabBlock slab;

	public SlabBlock asSlab()
	{
		return (SlabBlock) (Object) this;
	}

	@Override
	public void setSlab(VerticalSlabBlock slab)
	{
		this.slab = slab;
	}

	@Override
	public boolean hasVertical()
	{
		return slab != null;
	}

	@Override
	public Direction getPlacing(BlockState blockState)
	{
		switch (blockState.getValue(SlabBlock.TYPE))
		{
		case TOP: return Direction.UP;
		case BOTTOM: return Direction.DOWN;
		default: return null;
		}
	}

	@Override
	public boolean isThis(BlockState blockState)
	{
		return blockState.is(asSlab()) || blockState.is(slab);
	}

	@Override
	public BlockState getDefaultHorizontalState(BlockState currentState, FluidState fluidState)
	{
		return currentState.is(asSlab()) ? currentState : asSlab().defaultBlockState().setValue(SlabBlock.WATERLOGGED, fluidState.getType() == Fluids.WATER);
	}

	@Override
	public BlockState getDefaultVerticalState(BlockState currentState, FluidState fluidState)
	{
		return currentState.is(slab) ? currentState : slab.defaultBlockState().setValue(VerticalSlabBlock.WATERLOGGED, fluidState.getType() == Fluids.WATER);
	}

	//@Override
	@Inject(method = "getStateForPlacement", at = @At("HEAD"), cancellable = true)
	private void getStateForPlacement(BlockPlaceContext context, CallbackInfoReturnable<BlockState> ci)
	{
		if (this.hasVertical()) ci.setReturnValue(getStateForPlacementImpl(context));
	}

	//@Override
	@Inject(method = "rotate", at = @At("HEAD"), cancellable = true)
	private void rotate(BlockState blockState, Rotation rotation, CallbackInfoReturnable<BlockState> ci) //this injects into an existing method if it has already been added
	{
		if (this.hasVertical()) ci.setReturnValue(rotateImpl(blockState, rotation));
	}

	//@Override
	@Override
	@Intrinsic
	@SuppressWarnings("deprecation")
	public BlockState rotate(BlockState blockState, Rotation rotation) //this adds the method if it does not exist
	{
		if (this.hasVertical()) return rotateImpl(blockState, rotation);
		else return super.rotate(blockState, rotation);
	}

	//@Override
	@Inject(method = "mirror", at = @At("HEAD"), cancellable = true)
	private void mirror(BlockState blockState, Mirror mirror, CallbackInfoReturnable<BlockState> ci) //this injects into an existing method if it has already been added
	{
		if (this.hasVertical()) ci.setReturnValue(mirrorImpl(blockState, mirror));
	}

	//@Override
	@Override
	@Intrinsic
	@SuppressWarnings("deprecation")
	public BlockState mirror(BlockState blockState, Mirror mirror) //this adds the method if it does not exist
	{
		if (this.hasVertical()) return mirrorImpl(blockState, mirror);
		else return super.mirror(blockState, mirror);
	}

	//@Override
	@Inject(method = "canBeReplaced", at = @At("HEAD"), cancellable = true)
	private void canBeReplaced(BlockState state, BlockPlaceContext context, CallbackInfoReturnable<Boolean> ci)
	{
		if (this.hasVertical()) ci.setReturnValue(canBeReplacedImpl(state, context));
	}

	@Override
	public FluidState getFluidStateImpl(BlockState blockState)
	{
		return asSlab().getFluidState(blockState);
	}
}