package com.firemerald.vsas.block;

import javax.annotation.Nullable;

import com.firemerald.vsas.client.IBlockHighlight;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;

public interface ISlabBlock extends IVerticalBlock, SimpleWaterloggedBlock
{
	public static interface IVanillSlabBlock extends ISlabBlock
	{
		public void setSlab(VerticalSlabBlock slab);
	}
	
	@Override
	public default BlockState rotateImpl(BlockState blockState, Rotation rotation)
	{
		Direction placing = getPlacing(blockState);
		return placing == null ? blockState : forPlacing(rotation.rotate(placing), blockState, getFluidState(blockState));
	}

	@Override
	public default BlockState mirrorImpl(BlockState blockState, Mirror mirror)
	{
		Direction placing = getPlacing(blockState);
		return placing == null ? blockState : forPlacing(mirror.mirror(placing), blockState, getFluidState(blockState));
	}

	public default boolean canBeReplacedImpl(BlockState state, BlockPlaceContext context)
	{
		ItemStack itemstack = context.getItemInHand();
		if (itemstack.is(this.asItem()))
		{
			if (context.replacingClickedOnBlock())
			{
				Direction placing = getPlacing(state);
				if (placing != null)
				{
					Direction direction = getPlacingDirection(context);
					return direction.getAxis() == placing.getAxis() && context.getClickedFace().getOpposite() == placing;
				}
				else return false;
			}
			else return true;
		}
		else return false;
	}

	@Override
	public default BlockState getStateForPlacementImpl(BlockPlaceContext context)
	{
		BlockPos blockPos = context.getClickedPos();
		BlockState blockState = context.getLevel().getBlockState(blockPos);
        FluidState fluidState = context.getLevel().getFluidState(blockPos);
        if (isThis(blockState)) return getDefaultHorizontalState(blockState, fluidState).setValue(SlabBlock.TYPE, SlabType.DOUBLE);
        else return forPlacing(getPlacingDirection(context), blockState, fluidState);
	}
	
	public default BlockState forPlacing(Direction dir, BlockState blockState, FluidState fluidState)
	{
    	if (dir == Direction.UP) return getDefaultHorizontalState(blockState, fluidState).setValue(SlabBlock.TYPE, SlabType.TOP);
    	else if (dir == Direction.DOWN) return getDefaultHorizontalState(blockState, fluidState).setValue(SlabBlock.TYPE, SlabType.BOTTOM);
    	else return getDefaultVerticalState(blockState, fluidState).setValue(VerticalSlabBlock.PLACING, dir);
	}
	
	@Nullable
	public default Direction getPlacing(BlockState blockState)
	{
		if (blockState.getBlock() instanceof SlabBlock)
		{
			SlabType type = blockState.getValue(SlabBlock.TYPE);
			return type == SlabType.BOTTOM ? Direction.DOWN : type == SlabType.TOP ? Direction.UP : null;
		}
		else return blockState.getValue(VerticalSlabBlock.PLACING);
	}

	@Override
	public default IBlockHighlight getBlockHighlight(Player player, BlockHitResult trace)
	{
		return hasVertical() ? IBlockHighlight.SLAB : null;
	}

	public default Direction getPlacingDirection(BlockPlaceContext context)
	{
		double hitX = context.getClickLocation().x - context.getClickedPos().getX() - .5;
		double hitY = context.getClickLocation().y - context.getClickedPos().getY() - .5;
		double hitZ = context.getClickLocation().z - context.getClickedPos().getZ() - .5;
        switch (context.getClickedFace())
        {
		case DOWN:
			if (Math.abs(hitX) <= .25f && Math.abs(hitZ) <= .25f) return Direction.UP;
			else if (hitX > hitZ)
			{
				if (hitX > -hitZ) return Direction.EAST;
				else return Direction.NORTH;
			}
			else
			{
				if (hitZ > -hitX) return Direction.SOUTH;
				else return Direction.WEST;
			}
		case UP:
			if (Math.abs(hitX) <= .25f && Math.abs(hitZ) <= .25f) return Direction.DOWN;
			else if (hitX > hitZ)
			{
				if (hitX > -hitZ) return Direction.EAST;
				else return Direction.NORTH;
			}
			else
			{
				if (hitZ > -hitX) return Direction.SOUTH;
				else return Direction.WEST;
			}
		case NORTH:
			if (Math.abs(hitX) <= .25f && Math.abs(hitY) <= .25f) return Direction.SOUTH;
			else if (hitX > hitY)
			{
				if (hitX > -hitY) return Direction.EAST;
				else return Direction.DOWN;
			}
			else
			{
				if (hitY > -hitX) return Direction.UP;
				else return Direction.WEST;
			}
		case SOUTH:
			if (Math.abs(hitX) <= .25f && Math.abs(hitY) <= .25f) return Direction.NORTH;
			else if (hitX > hitY)
			{
				if (hitX > -hitY) return Direction.EAST;
				else return Direction.DOWN;
			}
			else
			{
				if (hitY > -hitX) return Direction.UP;
				else return Direction.WEST;
			}
		case WEST:
			if (Math.abs(hitY) <= .25f && Math.abs(hitZ) <= .25f) return Direction.EAST;
			else if (hitZ > hitY)
			{
				if (hitZ > -hitY) return Direction.SOUTH;
				else return Direction.DOWN;
			}
			else
			{
				if (hitY > -hitZ) return Direction.UP;
				else return Direction.NORTH;
			}
		case EAST:
			if (Math.abs(hitY) <= .25f && Math.abs(hitZ) <= .25f) return Direction.WEST;
			else if (hitZ > hitY)
			{
				if (hitZ > -hitY) return Direction.SOUTH;
				else return Direction.DOWN;
			}
			else
			{
				if (hitY > -hitZ) return Direction.UP;
				else return Direction.NORTH;
			}
		default:
			return null;
        }
	}
}