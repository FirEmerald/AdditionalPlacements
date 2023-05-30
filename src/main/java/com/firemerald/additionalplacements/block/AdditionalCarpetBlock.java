package com.firemerald.additionalplacements.block;

import com.firemerald.additionalplacements.block.interfaces.ICarpetBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CarpetBlock;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

public class AdditionalCarpetBlock extends AdditionalPlacementBlock<CarpetBlock> implements ICarpetBlock<CarpetBlock>
{
	public static final DirectionProperty PLACING = AdditionalBlockStateProperties.HORIZONTAL_OR_UP_FACING;
	public static final VoxelShape[] SHAPES = {
			Block.box(0, 15, 0, 16, 16, 16),
			Block.box(0, 0, 0, 16, 16, 1),
			Block.box(0, 0, 15, 16, 16, 16),
			Block.box(0, 0, 0, 1, 16, 16),
			Block.box(15, 0, 0, 16, 16, 16)
	};

	@SuppressWarnings("deprecation")
	public AdditionalCarpetBlock(CarpetBlock carpet)
	{
		super(carpet);
		this.registerDefaultState(copyProperties(getModelState(), this.stateDefinition.any()).setValue(PLACING, Direction.NORTH));
		((IVanillaCarpetBlock) carpet).setOtherBlock(this);
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
		return SHAPES[state.getValue(PLACING).ordinal() - 1];
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
		return "carpet";
	}

	@Override
	public String getTagTypeNamePlural()
	{
		return "carpets";
	}

	@Override
	public BlockState updateShapeImpl(BlockState thisState, Direction updatedDirection, BlockState otherState, IWorld level, BlockPos thisPos, BlockPos otherPos)
	{
		return !thisState.canSurvive(level, thisPos) ? Blocks.AIR.defaultBlockState() : thisState;
	}

	@Override
	@Deprecated
	public boolean canSurvive(BlockState state, IWorldReader level, BlockPos pos)
	{
		return !level.isEmptyBlock(pos.relative(state.getValue(PLACING)));
	}
}
