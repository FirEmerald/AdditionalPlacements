package com.firemerald.additionalplacements.mixin;

import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.firemerald.additionalplacements.block.AdditionalBasePressurePlateBlock;
import com.firemerald.additionalplacements.block.interfaces.IPressurePlateBlock.IVanillaPressurePlateBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.WeightedPressurePlateBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

@Mixin(WeightedPressurePlateBlock.class)
public abstract class MixinWeightedPressurePlateBlock extends Block implements IVanillaPressurePlateBlock
{
	private MixinWeightedPressurePlateBlock(Properties properties)
	{
		super(properties);
	}

	public AdditionalBasePressurePlateBlock<?> plate;

	public WeightedPressurePlateBlock asPlate()
	{
		return (WeightedPressurePlateBlock) (Object) this;
	}

	@Override
	public void setOtherBlock(AdditionalBasePressurePlateBlock<?> plate)
	{
		this.plate = plate;
	}

	@Override
	public AdditionalBasePressurePlateBlock<?> getOtherBlock()
	{
		return plate;
	}

	@Override
	public boolean hasAdditionalStates()
	{
		return plate != null;
	}

	@Override
	public Direction getPlacing(BlockState blockState)
	{
		return Direction.DOWN;
	}

	@Override
	public boolean isThis(BlockState blockState)
	{
		return blockState.is(asPlate()) || blockState.is(plate);
	}

	@Override
	public BlockState getDefaultVanillaState(BlockState currentState)
	{
		return currentState.is(asPlate()) ? currentState : plate.copyProperties(currentState, asPlate().defaultBlockState());
	}

	@Override
	public BlockState getDefaultAdditionalState(BlockState currentState)
	{
		return currentState.is(plate) ? currentState : plate.copyProperties(currentState, plate.defaultBlockState());
	}

	//@Override
	@Inject(method = "getStateForPlacement", at = @At("RETURN"), cancellable = true)
	private void getStateForPlacement(BlockItemUseContext context, CallbackInfoReturnable<BlockState> ci)
	{
		if (this.hasAdditionalStates() && !disablePlacement()) ci.setReturnValue(getStateForPlacementImpl(context, ci.getReturnValue()));
	}

	//@Override
	@Override
	@Intrinsic
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		if (this.hasAdditionalStates() && !disablePlacement()) return getStateForPlacementImpl(context, super.getStateForPlacement(context));
		else return super.getStateForPlacement(context);
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

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShapeImpl(BlockState state, Direction direction, BlockState otherState, IWorld level, BlockPos pos, BlockPos otherPos)
	{
		return super.updateShape(state, direction, otherState, level, pos, otherPos);
	}
}