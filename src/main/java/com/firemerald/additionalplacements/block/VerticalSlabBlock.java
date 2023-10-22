package com.firemerald.additionalplacements.block;

import com.firemerald.additionalplacements.block.interfaces.ISlabBlock;
import com.firemerald.additionalplacements.util.VoxelShapes;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public class VerticalSlabBlock extends AdditionalPlacementLiquidBlock<SlabBlock> implements ISlabBlock<SlabBlock>
{
	public static final DirectionProperty PLACING = BlockStateProperties.HORIZONTAL_FACING;

	public static VerticalSlabBlock of(SlabBlock slab)
	{
		return new VerticalSlabBlock(slab);
	}
	
	@SuppressWarnings("deprecation")
	private VerticalSlabBlock(SlabBlock slab)
	{
		super(slab);
		this.registerDefaultState(copyProperties(getModelState(), this.stateDefinition.any()).setValue(PLACING, Direction.NORTH));
		((IVanillaSlabBlock) slab).setOtherBlock(this);
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(PLACING);
		super.createBlockStateDefinition(builder);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext context)
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
	public boolean canBeReplaced(BlockState state, BlockItemUseContext context)
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
	public BlockState updateShapeImpl(BlockState state, Direction direction, BlockState otherState, IWorld level, BlockPos pos, BlockPos otherPos)
	{
		return state;
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
}
