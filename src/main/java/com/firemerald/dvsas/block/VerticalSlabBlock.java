package com.firemerald.dvsas.block;

import java.util.Collection;

import com.firemerald.dvsas.common.DVSaSBlockTags;
import com.firemerald.dvsas.util.VoxelShapes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VerticalSlabBlock extends VerticalBlock<SlabBlock> implements ISlabBlock
{
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final DirectionProperty PLACING = BlockStateProperties.HORIZONTAL_FACING;

	public VerticalSlabBlock(SlabBlock slab)
	{
		this(slab, slab.defaultBlockState());
	}

	public VerticalSlabBlock(SlabBlock slab, BlockState modelState)
	{
		super(slab, modelState);
		this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false).setValue(PLACING, Direction.NORTH));
		((IVanillSlabBlock) slab).setSlab(this);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(WATERLOGGED, PLACING);
	}

	@Override
	@Deprecated
	public BlockState updateShape(BlockState state, Direction direction, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos)
	{
		if (state.getValue(WATERLOGGED)) level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		return super.updateShape(state, direction, otherState, level, pos, otherPos);
	}

	@Override
	@Deprecated
	public FluidState getFluidState(BlockState blockState)
	{
		return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		switch (state.getValue(PLACING))
		{
		case NORTH: return VoxelShapes.SLAB_NORTH;
		case EAST: return VoxelShapes.SLAB_EAST;
		case SOUTH: return VoxelShapes.SLAB_SOUTH;
		case WEST: return VoxelShapes.SLAB_WEST;
		default: return super.getShape(state, level, pos, context);
		}
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext context)
	{
		return canBeReplacedImpl(state, context);
	}

	@Override
	public Direction getPlacing(BlockState blockState)
	{
		return blockState.getValue(PLACING);
	}

	@Override
	public BlockState getDefaultHorizontalState(BlockState currentState, FluidState fluidState)
	{
		return currentState.is(parentBlock) ? currentState : parentBlock.defaultBlockState().setValue(SlabBlock.WATERLOGGED, fluidState.getType() == Fluids.WATER);
	}

	@Override
	public BlockState getDefaultVerticalState(BlockState currentState, FluidState fluidState)
	{
		return currentState.is(this) ? currentState : this.defaultBlockState().setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
	}

	@Override
	public Collection<TagKey<Block>> modifyTags(Collection<TagKey<Block>> tags)
	{
		tags.remove(BlockTags.SLABS);
		tags.add(DVSaSBlockTags.VERTICAL_SLABS);
		if (tags.remove(BlockTags.WOODEN_SLABS)) tags.add(DVSaSBlockTags.VERTICAL_WOODEN_SLABS);
		return tags;
	}
}
