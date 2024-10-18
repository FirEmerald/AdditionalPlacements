package com.firemerald.additionalplacements.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.firemerald.additionalplacements.block.VerticalSlabBlock;
import com.firemerald.additionalplacements.block.interfaces.ISlabBlock.IVanillaSlabBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;

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

	@Inject(method = "getStateForPlacement", at = @At("RETURN"), cancellable = true)
	private void getStateForPlacement(BlockPlaceContext context, CallbackInfoReturnable<BlockState> ci)
	{
		if (this.hasAdditionalStates() && enablePlacement(context.getClickedPos(), context.getLevel(), context.getClickedFace(), context.getPlayer())) ci.setReturnValue(getStateForPlacementImpl(context, ci.getReturnValue()));
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

	@Inject(method = "canBeReplaced", at = @At("HEAD"), cancellable = true)
	private void canBeReplaced(BlockState state, BlockPlaceContext context, CallbackInfoReturnable<Boolean> ci)
	{
		if (this.hasAdditionalStates() && enablePlacement(context.getClickedPos(), context.getLevel(), context.getClickedFace(), context.getPlayer())) ci.setReturnValue(canBeReplacedImpl(state, context));
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShapeImpl(BlockState state, Direction direction, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos)
	{
		return super.updateShape(state, direction, otherState, level, pos, otherPos);
	}
}