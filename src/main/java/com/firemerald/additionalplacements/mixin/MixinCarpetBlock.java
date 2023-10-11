package com.firemerald.additionalplacements.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.firemerald.additionalplacements.block.AdditionalCarpetBlock;
import com.firemerald.additionalplacements.block.interfaces.ICarpetBlock.IVanillaCarpetBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(CarpetBlock.class)
public abstract class MixinCarpetBlock extends Block implements IVanillaCarpetBlock
{
	private MixinCarpetBlock(Properties properties)
	{
		super(properties);
	}

	public AdditionalCarpetBlock carpet;

	public CarpetBlock asCarpet()
	{
		return (CarpetBlock) (Object) this;
	}

	@Override
	public void setOtherBlock(AdditionalCarpetBlock carpet)
	{
		this.carpet = carpet;
	}

	@Override
	public AdditionalCarpetBlock getOtherBlock()
	{
		return carpet;
	}

	@Override
	public boolean hasAdditionalStates()
	{
		return carpet != null;
	}

	@Override
	public Direction getPlacing(BlockState blockState)
	{
		return Direction.DOWN;
	}

	@Override
	public boolean isThis(BlockState blockState)
	{
		return blockState.is(asCarpet()) || blockState.is(carpet);
	}

	@Override
	public BlockState getDefaultVanillaState(BlockState currentState)
	{
		return currentState.is(asCarpet()) ? currentState : carpet.copyProperties(currentState, asCarpet().defaultBlockState());
	}

	@Override
	public BlockState getDefaultAdditionalState(BlockState currentState)
	{
		return currentState.is(carpet) ? currentState : carpet.copyProperties(currentState, carpet.defaultBlockState());
	}

	@Inject(method = "getStateForPlacement", at = @At("RETURN"), cancellable = true)
	private void getStateForPlacement(BlockPlaceContext context, CallbackInfoReturnable<BlockState> ci)
	{
		if (this.hasAdditionalStates() && !disablePlacement(context.getClickedPos(), context.getLevel(), context.getClickedFace())) ci.setReturnValue(getStateForPlacementImpl(context, ci.getReturnValue()));
	}

	@Override
	@Unique
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		if (this.hasAdditionalStates() && !disablePlacement(context.getClickedPos(), context.getLevel(), context.getClickedFace())) return getStateForPlacementImpl(context, super.getStateForPlacement(context));
		else return super.getStateForPlacement(context);
	}

	@Inject(method = "rotate", at = @At("HEAD"), cancellable = true)
	private void rotate(BlockState blockState, Rotation rotation, CallbackInfoReturnable<BlockState> ci) //this injects into an existing method if it has already been added
	{
		if (this.hasAdditionalStates()) ci.setReturnValue(rotateImpl(blockState, rotation));
	}

	@Override
	@Unique
	@SuppressWarnings("deprecation")
	public BlockState rotate(BlockState blockState, Rotation rotation) //this adds the method if it does not exist
	{
		if (this.hasAdditionalStates()) return rotateImpl(blockState, rotation);
		else return super.rotate(blockState, rotation);
	}

	@Inject(method = "mirror", at = @At("HEAD"), cancellable = true)
	private void mirror(BlockState blockState, Mirror mirror, CallbackInfoReturnable<BlockState> ci) //this injects into an existing method if it has already been added
	{
		if (this.hasAdditionalStates()) ci.setReturnValue(mirrorImpl(blockState, mirror));
	}

	@Override
	@Unique
	@SuppressWarnings("deprecation")
	public BlockState mirror(BlockState blockState, Mirror mirror) //this adds the method if it does not exist
	{
		if (this.hasAdditionalStates()) return mirrorImpl(blockState, mirror);
		else return super.mirror(blockState, mirror);
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShapeImpl(BlockState state, Direction direction, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos)
	{
		return super.updateShape(state, direction, otherState, level, pos, otherPos);
	}
}