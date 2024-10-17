package com.firemerald.additionalplacements.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.firemerald.additionalplacements.block.AdditionalCarpetBlock;
import com.firemerald.additionalplacements.block.interfaces.ICarpetBlock.IVanillaCarpetBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
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

	@Inject(at = @At("RETURN"), remap = false, cancellable = true, target = {
			@Desc(value = "getStateForPlacement", ret = BlockState.class, args = {BlockPlaceContext.class}),
			@Desc(value = "m_5573_", ret = BlockState.class, args = {BlockPlaceContext.class})
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

	@Inject(at = @At("HEAD"), remap = false, cancellable = true, target = {
			@Desc(value = "rotate", ret = BlockState.class, args = {BlockState.class, Rotation.class}),
			@Desc(value = "m_6843_", ret = BlockState.class, args = {BlockState.class, Rotation.class})
	})
	private void rotate(BlockState blockState, Rotation rotation, CallbackInfoReturnable<BlockState> ci)
	{
		if (this.hasAdditionalStates()) ci.setReturnValue(rotateImpl(blockState, rotation));
	}

	@Override
	@Unique(silent = true)
	@SuppressWarnings("deprecation")
	public BlockState rotate(BlockState blockState, Rotation rotation)
	{
		if (this.hasAdditionalStates()) return rotateImpl(blockState, rotation);
		else return super.rotate(blockState, rotation);
	}

	@Inject(at = @At("HEAD"), remap = false, cancellable = true, target = {
			@Desc(value = "mirror", ret = BlockState.class, args = {BlockState.class, Mirror.class}),
			@Desc(value = "m_6943_", ret = BlockState.class, args = {BlockState.class, Mirror.class})
	})
	private void mirror(BlockState blockState, Mirror mirror, CallbackInfoReturnable<BlockState> ci)
	{
		if (this.hasAdditionalStates()) ci.setReturnValue(mirrorImpl(blockState, mirror));
	}

	@Override
	@Unique(silent = true)
	@SuppressWarnings("deprecation")
	public BlockState mirror(BlockState blockState, Mirror mirror)
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