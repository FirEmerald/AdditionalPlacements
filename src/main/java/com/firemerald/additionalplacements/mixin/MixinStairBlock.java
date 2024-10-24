package com.firemerald.additionalplacements.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.firemerald.additionalplacements.block.VerticalStairBlock;
import com.firemerald.additionalplacements.block.interfaces.IStairBlock.IVanillaStairBlock;
import com.firemerald.additionalplacements.util.stairs.CompressedStairShape;
import com.firemerald.additionalplacements.util.stairs.StairConnections;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;

@Mixin(StairBlock.class)
public abstract class MixinStairBlock implements IVanillaStairBlock
{
	public VerticalStairBlock stairs;
	@Shadow
	private BlockState baseState;

	public StairBlock asStair()
	{
		return (StairBlock) (Object) this;
	}

	@Override
	public void setOtherBlock(VerticalStairBlock stairs)
	{
		this.stairs = stairs;
	}

	@Override
	public VerticalStairBlock getOtherBlock()
	{
		return stairs;
	}

	@Override
	public boolean hasAdditionalStates()
	{
		return stairs != null;
	}

	@Override
	public boolean isThis(BlockState blockState)
	{
		return blockState.is(asStair()) || blockState.is(stairs);
	}

	@Override
	public BlockState getDefaultVanillaState(BlockState currentState)
	{
		return currentState.is(asStair()) ? currentState : stairs.copyProperties(currentState, asStair().defaultBlockState());
	}

	@Override
	public BlockState getDefaultAdditionalState(BlockState currentState)
	{
		return currentState.is(stairs) ? currentState : stairs.copyProperties(currentState, stairs.defaultBlockState());
	}

	@Inject(method = "getStateForPlacement", at = @At("RETURN"), cancellable = true)
	private void getStateForPlacement(BlockPlaceContext context, CallbackInfoReturnable<BlockState> ci)
	{
		if (this.hasAdditionalStates() && enablePlacement(context.getClickedPos(), context.getLevel(), context.getClickedFace(), context.getPlayer())) ci.setReturnValue(getStateForPlacementImpl(context, ci.getReturnValue()));
	}

	@Inject(method = "rotate", at = @At("HEAD"), cancellable = true)
	private void rotate(BlockState blockState, Rotation rotation, CallbackInfoReturnable<BlockState> ci)
	{
		if (this.hasAdditionalStates()) ci.setReturnValue(rotateImpl(blockState, rotation));
	}

	@Inject(method = "mirror", at = @At("HEAD"), cancellable = true)
	private void mirror(BlockState blockState, Mirror mirror, CallbackInfoReturnable<BlockState> ci)
	{
		if (this.hasAdditionalStates()) ci.setReturnValue(mirrorImpl(blockState, mirror));
	}

	@Inject(method = "updateShape", at = @At("HEAD"), cancellable = true)
	private void updateShape(BlockState state, Direction direction, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos, CallbackInfoReturnable<BlockState> ci)
	{
		if (this.hasAdditionalStates()) ci.setReturnValue(updateShapeImpl(state, direction, otherState, level, pos, otherPos));
	}

	@Override
	public BlockState getModelStateImpl()
	{
		return baseState;
	}

	@Override
	public StairConnections allowedConnections() {
		return stairs.allowedConnections();
	}

	@Override
	public EnumProperty<CompressedStairShape> shapeProperty() {
		return stairs.shapeProperty();
	}
}