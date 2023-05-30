package com.firemerald.additionalplacements.mixin;

import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.firemerald.additionalplacements.block.VerticalSlabBlock;
import com.firemerald.additionalplacements.block.interfaces.ISlabBlock.IVanillaSlabBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

@Mixin(SlabBlock.class)
public abstract class MixinSlabBlock extends Block implements IVanillaSlabBlock
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
	public void setOtherBlock(VerticalSlabBlock slab)
	{
		this.slab = slab;
	}

	@Override
	public VerticalSlabBlock getOtherBlock()
	{
		return slab;
	}

	@Override
	public boolean hasAdditionalStates()
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
	public BlockState getDefaultVanillaState(BlockState currentState)
	{
		return currentState.is(asSlab()) ? currentState : slab.copyProperties(currentState, asSlab().defaultBlockState());
	}

	@Override
	public BlockState getDefaultAdditionalState(BlockState currentState)
	{
		return currentState.is(slab) ? currentState : slab.copyProperties(currentState, slab.defaultBlockState());
	}

	//@Override
	@Inject(method = "getStateForPlacement", at = @At("RETURN"), cancellable = true)
	private void getStateForPlacement(BlockItemUseContext context, CallbackInfoReturnable<BlockState> ci)
	{
		if (this.hasAdditionalStates() && !disablePlacement()) ci.setReturnValue(getStateForPlacementImpl(context, ci.getReturnValue()));
	}

	//@Override
	@Inject(method = "rotate", at = @At("HEAD"), cancellable = true)
	private void rotate(BlockState blockState, Rotation rotation, CallbackInfoReturnable<BlockState> ci) //this injects into an existing method if it has already been added
	{
		if (this.hasAdditionalStates()) ci.setReturnValue(rotateImpl(blockState, rotation));
	}

	//@Override
	@Override
	@Intrinsic
	@SuppressWarnings("deprecation")
	public BlockState rotate(BlockState blockState, Rotation rotation) //this adds the method if it does not exist
	{
		if (this.hasAdditionalStates()) return rotateImpl(blockState, rotation);
		else return super.rotate(blockState, rotation);
	}

	//@Override
	@Inject(method = "mirror", at = @At("HEAD"), cancellable = true)
	private void mirror(BlockState blockState, Mirror mirror, CallbackInfoReturnable<BlockState> ci) //this injects into an existing method if it has already been added
	{
		if (this.hasAdditionalStates()) ci.setReturnValue(mirrorImpl(blockState, mirror));
	}

	//@Override
	@Override
	@Intrinsic
	@SuppressWarnings("deprecation")
	public BlockState mirror(BlockState blockState, Mirror mirror) //this adds the method if it does not exist
	{
		if (this.hasAdditionalStates()) return mirrorImpl(blockState, mirror);
		else return super.mirror(blockState, mirror);
	}

	//@Override
	@Inject(method = "canBeReplaced", at = @At("HEAD"), cancellable = true)
	private void canBeReplaced(BlockState state, BlockItemUseContext context, CallbackInfoReturnable<Boolean> ci)
	{
		if (this.hasAdditionalStates()) ci.setReturnValue(canBeReplacedImpl(state, context));
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShapeImpl(BlockState state, Direction direction, BlockState otherState, IWorld level, BlockPos pos, BlockPos otherPos)
	{
		return super.updateShape(state, direction, otherState, level, pos, otherPos);
	}
}