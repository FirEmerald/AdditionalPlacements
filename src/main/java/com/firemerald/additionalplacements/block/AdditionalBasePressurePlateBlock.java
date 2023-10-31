package com.firemerald.additionalplacements.block;

import java.util.Random;

import javax.annotation.Nullable;

import com.firemerald.additionalplacements.block.interfaces.IBasePressurePlateBlock;
import com.firemerald.additionalplacements.block.interfaces.IBasePressurePlateBlockExtensions;

import net.minecraft.block.AbstractPressurePlateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockVoxelShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public abstract class AdditionalBasePressurePlateBlock<T extends AbstractPressurePlateBlock> extends AdditionalPlacementBlock<T> implements IBasePressurePlateBlock<T>
{
	public static final DirectionProperty PLACING = AdditionalBlockStateProperties.HORIZONTAL_OR_UP_FACING;
	public static final VoxelShape[] AABBS = {
			Block.box(1, 15, 1, 15, 16, 15),
			Block.box(1, 1, 0, 15, 15, 1),
			Block.box(1, 1, 15, 15, 15, 16),
			Block.box(0, 1, 1, 1, 15, 15),
			Block.box(15, 1, 1, 16, 15, 15)
	};
	public static final VoxelShape[] PRESSED_AABBS = {
			Block.box(1, 15.5, 1, 15, 16, 15),
			Block.box(1, 1, 0, 15, 15, .5),
			Block.box(1, 1, 15.5, 15, 15, 16),
			Block.box(0, 1, 1, .5, 15, 15),
			Block.box(15.5, 1, 1, 16, 15, 15)
	};
	public static final AxisAlignedBB[] TOUCH_AABBS = {
			new AxisAlignedBB(0.125, 0.75, 0.125, 0.875, 1, 0.875),
			new AxisAlignedBB(0.125, 0.125, 0, 0.875, 0.875, 0.25),
			new AxisAlignedBB(0.125, 0.125, 0.75, 0.875, 0.875, 1),
			new AxisAlignedBB(0, 0.125, 0.125, 0.25, 0.875, 0.875),
			new AxisAlignedBB(0.75, 0.125, 0.125, 1, 0.875, 0.875)
	};

	public final IBasePressurePlateBlockExtensions plateMethods;

	@SuppressWarnings({ "deprecation", "unchecked" })
	public AdditionalBasePressurePlateBlock(T plate)
	{
		super(plate);
		this.registerDefaultState(copyProperties(getModelState(), this.stateDefinition.any()).setValue(PLACING, Direction.NORTH));
		((IVanillaBasePressurePlateBlock<AdditionalBasePressurePlateBlock<T>>) plate).setOtherBlock(this);
		plateMethods = (IBasePressurePlateBlockExtensions) plate;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(PLACING);
		super.createBlockStateDefinition(builder);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext collisionContext)
	{
		return plateMethods.getSignalForStatePublic(state) > 0 ? PRESSED_AABBS[state.getValue(PLACING).ordinal() - 1] : AABBS[state.getValue(PLACING).ordinal() - 1];
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
		return "pressure_plate";
	}

	@Override
	public String getTagTypeNamePlural()
	{
		return "pressure_plates";
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
		Direction dir = state.getValue(PLACING);
		BlockPos blockpos = pos.relative(dir);
		return canSupportRigidBlock(level, blockpos, dir.getOpposite()) || canSupportCenter(level, blockpos, dir.getOpposite());
	}

	public static boolean canSupportRigidBlock(IBlockReader level, BlockPos pos, Direction dir)
	{
		return level.getBlockState(pos).isFaceSturdy(level, pos, dir, BlockVoxelShape.RIGID);
	}

	protected abstract int getSignalStrength(World level, BlockPos pos);

	@Override
	public boolean isPossibleToRespawnInThis()
	{
		return parentBlock.isPossibleToRespawnInThis();
	}

	@Override
	public void tick(BlockState state, ServerWorld level, BlockPos pos, Random rand)
	{
		int strength = plateMethods.getSignalForStatePublic(state);
		if (strength > 0) this.checkPressed(null, level, pos, state, strength);
	}

	@Override
	public void entityInside(BlockState state, World level, BlockPos pos, Entity entity)
	{
		if (!level.isClientSide)
		{
			int strength = plateMethods.getSignalForStatePublic(state);
			if (strength == 0) this.checkPressed(entity, level, pos, state, strength);
		}
	}

	protected void checkPressed(@Nullable Entity entity, World level, BlockPos pos, BlockState state, int oldStrength)
	{
		int strength = this.getSignalStrength(level, pos);
		boolean prevPowered = oldStrength > 0;
		boolean powered = strength > 0;
		if (oldStrength != strength)
		{
			BlockState blockstate = plateMethods.setSignalForStatePublic(state, strength);
			level.setBlock(pos, blockstate, 2);
			this.updateNeighbours(level, pos, state);
			level.setBlocksDirty(pos, state, blockstate);
		}
		if (!powered && prevPowered)
		{
			plateMethods.playOffSoundPublic(level, pos);
			//level.gameEvent(entity, GameEvent.BLOCK_UNPRESS, pos);
		}
		else if (powered && !prevPowered)
		{
			plateMethods.playOnSoundPublic(level, pos);
			//level.gameEvent(entity, GameEvent.BLOCK_PRESS, pos);
		}
		if (powered) level.getBlockTicks().scheduleTick(new BlockPos(pos), this, plateMethods.getPressedTimePublic());
	}

	@Override
	public void onRemove(BlockState state, World level, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		if (!isMoving && !state.is(oldState.getBlock()))
		{
			if (plateMethods.getSignalForStatePublic(state) > 0) this.updateNeighbours(level, pos, state);
			super.onRemove(state, level, pos, oldState, isMoving);
		}
	}

	protected void updateNeighbours(World level, BlockPos pos, BlockState state)
	{
		level.updateNeighborsAt(pos, this);
		level.updateNeighborsAt(pos.relative(state.getValue(PLACING)), this);
	}

	@Override
	public int getSignal(BlockState state, IBlockReader level, BlockPos pos, Direction dir)
	{
		return this.plateMethods.getSignalForStatePublic(state);
	}

	@Override
	public int getDirectSignal(BlockState state, IBlockReader level, BlockPos pos, Direction dir)
	{
		return dir == state.getValue(PLACING).getOpposite() ? plateMethods.getSignalForStatePublic(state) : 0;
	}

	@Override
	@Deprecated
	public boolean isSignalSource(BlockState state)
	{
		return parentBlock.isSignalSource(state);
	}

	@Override
	@Deprecated
	public PushReaction getPistonPushReaction(BlockState state)
	{
		return parentBlock.getPistonPushReaction(state);
	}
}
