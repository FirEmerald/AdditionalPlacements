package com.firemerald.additionalplacements.block;

import com.firemerald.additionalplacements.block.interfaces.IAdditionalBeaconBeamBlock;
import com.firemerald.additionalplacements.block.interfaces.ISlabBlock;
import com.firemerald.additionalplacements.util.VoxelShapes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VerticalSlabBlock extends AdditionalPlacementLiquidBlock<SlabBlock> implements ISlabBlock<SlabBlock>
{
	public static final DirectionProperty PLACING = BlockStateProperties.HORIZONTAL_FACING;

	public static VerticalSlabBlock of(SlabBlock slab)
	{
		return slab instanceof BeaconBeamBlock ? new AdditionalBeaconBeamVerticalSlabBlock(slab) : new VerticalSlabBlock(slab);
	}
	
	private static class AdditionalBeaconBeamVerticalSlabBlock extends VerticalSlabBlock implements IAdditionalBeaconBeamBlock<SlabBlock>
	{
		AdditionalBeaconBeamVerticalSlabBlock(SlabBlock slab)
		{
			super(slab);
		}
	}
	
	@SuppressWarnings("deprecation")
	private VerticalSlabBlock(SlabBlock slab)
	{
		super(slab);
		this.registerDefaultState(copyProperties(getModelState(), this.stateDefinition.any()).setValue(PLACING, Direction.NORTH));
		((IVanillaSlabBlock) slab).setOtherBlock(this);
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
	public BlockState getDefaultVanillaState(BlockState currentState)
	{
		return currentState.is(parentBlock) ? currentState : copyProperties(currentState, parentBlock.defaultBlockState());
	}

	@Override
	public BlockState getDefaultAdditionalState(BlockState currentState)
	{
		return currentState.is(this) ? currentState : copyProperties(currentState, this.defaultBlockState());
	}

	@Override
	public String getTagTypeName()
	{
		return "slab";
	}

	@Override
	public String getTagTypeNamePlural()
	{
		return "slabs";
	}

	@Override
	public BlockState updateShapeImpl(BlockState state, Direction direction, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos)
	{
		return state;
	}
}
