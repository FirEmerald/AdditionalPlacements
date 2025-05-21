package com.firemerald.additionalplacements.mixin;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.firemerald.additionalplacements.block.interfaces.IStairBlock.IVanillaStairBlock;
import com.firemerald.additionalplacements.block.stairs.AdditionalStairBlock;
import com.firemerald.additionalplacements.block.stairs.StairConnectionsType;
import com.firemerald.additionalplacements.block.stairs.common.CommonStairShapeState;
import com.firemerald.additionalplacements.block.stairs.vanilla.VanillaStairShapeState;

import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

@Mixin(StairsBlock.class)
public abstract class MixinStairBlock implements IVanillaStairBlock
{
	private AdditionalStairBlock stairs;
	@Final
	@Shadow(remap = false)
	private Supplier<BlockState> stateSupplier;

	private StairsBlock asStair()
	{
		return (StairsBlock) (Object) this;
	}

	@Override
	public void setOtherBlock(AdditionalStairBlock stairs)
	{
		this.stairs = stairs;
	}

	@Override
	public AdditionalStairBlock getOtherBlock()
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
	private void getStateForPlacement(BlockItemUseContext context, CallbackInfoReturnable<BlockState> ci)
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
	private void updateShape(BlockState state, Direction direction, BlockState otherState, IWorld level, BlockPos pos, BlockPos otherPos, CallbackInfoReturnable<BlockState> ci)
	{
		if (this.hasAdditionalStates()) ci.setReturnValue(updateShapeImpl(state, direction, otherState, level, pos, otherPos));
	}

	@Override
	public BlockState getModelStateImpl()
	{
		return stateSupplier.get();
	}

	@Override
	public BlockState getBlockStateInternal(CommonStairShapeState shapeState, BlockState currentState) {
		return stairs.getBlockStateInternal(shapeState, currentState);
	}

	@Override
	public CommonStairShapeState getShapeState(BlockState blockState) {
		return VanillaStairShapeState.toCommon(
				blockState.getValue(StairsBlock.FACING),
				blockState.getValue(StairsBlock.HALF),
				blockState.getValue(StairsBlock.SHAPE));
	}

    @Override
	public StairConnectionsType connectionsType() {
		return stairs.connectionsType();
	}
}