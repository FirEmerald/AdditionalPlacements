package com.firemerald.additionalplacements.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.firemerald.additionalplacements.block.AdditionalWeightedPressurePlateBlock;
import com.firemerald.additionalplacements.block.interfaces.IWeightedPressurePlateBlock.IVanillaWeightedPressurePlateBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.WeightedPressurePlateBlock;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(WeightedPressurePlateBlock.class)
public abstract class MixinWeightedPressurePlateBlock extends Block implements IVanillaWeightedPressurePlateBlock
{
	private MixinWeightedPressurePlateBlock(Properties properties)
	{
		super(properties);
	}

	private AdditionalWeightedPressurePlateBlock plate;

	private WeightedPressurePlateBlock asPlate()
	{
		return (WeightedPressurePlateBlock) (Object) this;
	}

	@Override
	public void setOtherBlock(AdditionalWeightedPressurePlateBlock plate)
	{
		this.plate = plate;
	}

	@Override
	public AdditionalWeightedPressurePlateBlock getOtherBlock()
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

	@Inject(at = @At("RETURN"), remap = false, cancellable = true, require = 0, method = {
			"getStateForPlacement(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/level/block/state/BlockState;",
			             "m_5573_(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/level/block/state/BlockState;"
	})
	private void getStateForPlacement(BlockPlaceContext context, CallbackInfoReturnable<BlockState> ci)
	{
		if (this.hasAdditionalStates() && enablePlacement(context.getClickedPos(), context.getLevel(), context.getClickedFace(), context.getPlayer())) ci.setReturnValue(getStateForPlacementImpl(context, ci.getReturnValue()));
	}

	@Override
	@Unique(silent = true)
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		BlockState superRet = super.getStateForPlacement(context);
		if (this.hasAdditionalStates() && enablePlacement(context.getClickedPos(), context.getLevel(), context.getClickedFace(), context.getPlayer())) return getStateForPlacementImpl(context, superRet);
		else return superRet;
	}

	@Inject(at = @At("HEAD"), remap = false, cancellable = true, require = 0, method = {
			"rotate(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/Rotation;)Lnet/minecraft/world/level/block/state/BlockState;",
			"m_6843_(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/Rotation;)Lnet/minecraft/world/level/block/state/BlockState;"
	})
	private void rotate(BlockState blockState, Rotation rotation, CallbackInfoReturnable<BlockState> ci)
	{
		if (this.hasAdditionalStates()) ci.setReturnValue(rotateImpl(blockState, rotation));
	}

	@Override
	@Unique(silent = true)
	public BlockState rotate(BlockState blockState, Rotation rotation)
	{
		if (this.hasAdditionalStates()) return rotateImpl(blockState, rotation);
		else return super.rotate(blockState, rotation);
	}

	@Inject(at = @At("HEAD"), remap = false, cancellable = true, require = 0, method = {
			 "mirror(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/Mirror;)Lnet/minecraft/world/level/block/state/BlockState;",
			"m_6943_(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/Mirror;)Lnet/minecraft/world/level/block/state/BlockState;"
	})
	private void mirror(BlockState blockState, Mirror mirror, CallbackInfoReturnable<BlockState> ci)
	{
		if (this.hasAdditionalStates()) ci.setReturnValue(mirrorImpl(blockState, mirror));
	}

	@Override
	@Unique(silent = true)
	public BlockState mirror(BlockState blockState, Mirror mirror)
	{
		if (this.hasAdditionalStates()) return mirrorImpl(blockState, mirror);
		else return super.mirror(blockState, mirror);
	}

	@Override
	public BlockState updateShapeImpl(BlockState state, LevelReader level, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos otherPos, BlockState otherState, RandomSource rand)
	{
		return super.updateShape(state, level, tickAccess, pos, direction, otherPos, otherState, rand);
	}
}