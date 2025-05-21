package com.firemerald.additionalplacements.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.firemerald.additionalplacements.block.AdditionalCarpetBlock;
import com.firemerald.additionalplacements.block.interfaces.ICarpetBlock.IVanillaCarpetBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CarpetBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

@Mixin(CarpetBlock.class)
public abstract class MixinCarpetBlock extends Block implements IVanillaCarpetBlock
{
	private MixinCarpetBlock(Properties properties)
	{
		super(properties);
	}

	private AdditionalCarpetBlock carpet;

	private CarpetBlock asCarpet()
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

	@Inject(at = @At("RETURN"), remap = false, cancellable = true, require = 0, method = {
			"getStateForPlacement(Lnet/minecraft/item/BlockItemUseContext;)Lnet/minecraft/block/BlockState;",
			       "func_196258_a(Lnet/minecraft/item/BlockItemUseContext;)Lnet/minecraft/block/BlockState;"
	})
	private void getStateForPlacement(BlockItemUseContext context, CallbackInfoReturnable<BlockState> ci)
	{
		if (this.hasAdditionalStates() && enablePlacement(context.getClickedPos(), context.getLevel(), context.getClickedFace(), context.getPlayer())) ci.setReturnValue(getStateForPlacementImpl(context, ci.getReturnValue()));
	}

	@Override
	@Unique(silent = true)
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		BlockState superRet = super.getStateForPlacement(context);
		if (this.hasAdditionalStates() && enablePlacement(context.getClickedPos(), context.getLevel(), context.getClickedFace(), context.getPlayer())) return getStateForPlacementImpl(context, superRet);
		else return superRet;
	}

	@Inject(at = @At("HEAD"), remap = false, cancellable = true, require = 0, method = {
			       "rotate(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/Rotation;)Lnet/minecraft/block/BlockState;",
			"func_185499_a(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/Rotation;)Lnet/minecraft/block/BlockState;"
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

	@Inject(at = @At("HEAD"), remap = false, cancellable = true, require = 0, method = {
			       "mirror(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/Mirror;)Lnet/minecraft/block/BlockState;",
			"func_185471_a(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/Mirror;)Lnet/minecraft/block/BlockState;"
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
	public BlockState updateShapeImpl(BlockState state, Direction direction, BlockState otherState, IWorld level, BlockPos pos, BlockPos otherPos)
	{
		return super.updateShape(state, direction, otherState, level, pos, otherPos);
	}
}