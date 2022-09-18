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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VerticalSlabBlock extends VerticalBlock<SlabBlock> implements ISlabBlock
{
	public static final DirectionProperty PLACING = BlockStateProperties.HORIZONTAL_FACING;

	@SuppressWarnings("deprecation")
	public VerticalSlabBlock(SlabBlock slab)
	{
		super(slab);
		this.registerDefaultState(copyProperties(getModelState(), this.stateDefinition.any()).setValue(PLACING, Direction.NORTH));
		((IVanillSlabBlock) slab).setSlab(this);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(PLACING);
		super.createBlockStateDefinition(builder);
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
	public BlockState getDefaultHorizontalState(BlockState currentState)
	{
		return currentState.is(parentBlock) ? currentState : copyProperties(currentState, parentBlock.defaultBlockState());
	}

	@Override
	public BlockState getDefaultVerticalState(BlockState currentState)
	{
		return currentState.is(this) ? currentState : copyProperties(currentState, this.defaultBlockState());
	}

	@Override
	public Collection<TagKey<Block>> modifyTags(Collection<TagKey<Block>> tags)
	{
		tags.remove(BlockTags.SLABS);
		tags.add(DVSaSBlockTags.VERTICAL_SLABS);
		if (tags.remove(BlockTags.WOODEN_SLABS)) tags.add(DVSaSBlockTags.VERTICAL_WOODEN_SLABS);
		return tags;
	}

	@Override
	public BlockState updateShapeImpl(BlockState state, Direction direction, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos)
	{
		return state;
	}
}
