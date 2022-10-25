package com.firemerald.additionalplacements.block;

import java.util.Collection;
import java.util.Random;

import javax.annotation.Nullable;

import com.firemerald.additionalplacements.block.interfaces.IBasePressurePlateBlock;
import com.firemerald.additionalplacements.block.interfaces.IPressurePlateBlock;
import com.firemerald.additionalplacements.common.AdditionalPlacementsBlockTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class AdditionalBasePressurePlateBlock<T extends BasePressurePlateBlock> extends AdditionalPlacementBlock<T> implements IPressurePlateBlock<T>
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
	public static final AABB[] TOUCH_AABBS = {
			new AABB(0.125, 0.75, 0.125, 0.875, 1, 0.875),
			new AABB(0.125, 0.125, 0, 0.875, 0.875, 0.25),
			new AABB(0.125, 0.125, 0.75, 0.875, 0.875, 1),
			new AABB(0, 0.125, 0.125, 0.25, 0.875, 0.875),
			new AABB(0.75, 0.125, 0.125, 1, 0.875, 0.875)
	};
	
	public final IBasePressurePlateBlock plateMethods;

	@SuppressWarnings({ "deprecation", "unchecked" })
	public AdditionalBasePressurePlateBlock(T plate)
	{
		super(plate);
		this.registerDefaultState(copyProperties(getModelState(), this.stateDefinition.any()).setValue(PLACING, Direction.NORTH));
		((IVanillaPressurePlateBlock<T>) plate).setPressurePlate(this);
		plateMethods = (IBasePressurePlateBlock) plate;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(PLACING);
		super.createBlockStateDefinition(builder);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter p_49342_, BlockPos p_49343_, CollisionContext p_49344_)
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
	public Collection<TagKey<Block>> modifyTags(Collection<TagKey<Block>> tags)
	{
		tags.remove(BlockTags.PRESSURE_PLATES);
		tags.add(AdditionalPlacementsBlockTags.ADDITIONAL_PRESSURE_PLATES);
		if (tags.remove(BlockTags.WOODEN_PRESSURE_PLATES)) tags.add(AdditionalPlacementsBlockTags.ADDITIONAL_WOODEN_PRESSURE_PLATES);
		if (tags.remove(BlockTags.STONE_PRESSURE_PLATES)) tags.add(AdditionalPlacementsBlockTags.ADDITIONAL_STONE_PRESSURE_PLATES);
		return tags;
	}

	@Override
	public BlockState updateShapeImpl(BlockState thisState, Direction updatedDirection, BlockState otherState, LevelAccessor level, BlockPos thisPos, BlockPos otherPos)
	{
		return !thisState.canSurvive(level, thisPos) ? Blocks.AIR.defaultBlockState() : thisState;
	}

	@Override
	@Deprecated
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
	{
		Direction dir = state.getValue(PLACING);
		BlockPos blockpos = pos.relative(dir);
		return canSupportRigidBlock(level, blockpos, dir.getOpposite()) || canSupportCenter(level, blockpos, dir.getOpposite());
	}
	
	public static boolean canSupportRigidBlock(BlockGetter pLevel, BlockPos pPos, Direction dir)
	{
		return pLevel.getBlockState(pPos).isFaceSturdy(pLevel, pPos, dir, SupportType.RIGID);
	}

	protected abstract int getSignalStrength(Level level, BlockPos pos);
	
	@Override
	public boolean isPossibleToRespawnInThis()
	{
		return parentBlock.isPossibleToRespawnInThis();
	}

	@Override
	public void tick(BlockState p_49304_, ServerLevel p_49305_, BlockPos p_49306_, Random p_49307_)
	{
		int i = plateMethods.getSignalForStatePublic(p_49304_);
		if (i > 0) this.checkPressed((Entity)null, p_49305_, p_49306_, p_49304_, i);
	}

	@Override
	public void entityInside(BlockState p_49314_, Level p_49315_, BlockPos p_49316_, Entity p_49317_)
	{
		if (!p_49315_.isClientSide)
		{
			int i = plateMethods.getSignalForStatePublic(p_49314_);
			if (i == 0) this.checkPressed(p_49317_, p_49315_, p_49316_, p_49314_, i);
		}
	}

	protected void checkPressed(@Nullable Entity p_152144_, Level p_152145_, BlockPos p_152146_, BlockState p_152147_, int p_152148_)
	{
		int i = this.getSignalStrength(p_152145_, p_152146_);
		boolean flag = p_152148_ > 0;
		boolean flag1 = i > 0;
		if (p_152148_ != i)
		{
			BlockState blockstate = plateMethods.setSignalForStatePublic(p_152147_, i);
			p_152145_.setBlock(p_152146_, blockstate, 2);
			this.updateNeighbours(p_152145_, p_152146_);
			p_152145_.setBlocksDirty(p_152146_, p_152147_, blockstate);
		}
		if (!flag1 && flag)
		{
			plateMethods.playOffSoundPublic(p_152145_, p_152146_);
			p_152145_.gameEvent(p_152144_, GameEvent.BLOCK_UNPRESS, p_152146_);
		}
		else if (flag1 && !flag)
		{
			plateMethods.playOnSoundPublic(p_152145_, p_152146_);
			p_152145_.gameEvent(p_152144_, GameEvent.BLOCK_PRESS, p_152146_);
		}
		if (flag1) p_152145_.scheduleTick(new BlockPos(p_152146_), this, plateMethods.getPressedTimePublic());
	}

	@Override
	public void onRemove(BlockState p_49319_, Level p_49320_, BlockPos p_49321_, BlockState p_49322_, boolean p_49323_)
	{
		if (!p_49323_ && !p_49319_.is(p_49322_.getBlock()))
		{
			if (plateMethods.getSignalForStatePublic(p_49319_) > 0) this.updateNeighbours(p_49320_, p_49321_);
			super.onRemove(p_49319_, p_49320_, p_49321_, p_49322_, p_49323_);
		}
	}
	
	protected void updateNeighbours(Level p_49292_, BlockPos p_49293_)
	{
		p_49292_.updateNeighborsAt(p_49293_, this);
		p_49292_.updateNeighborsAt(p_49293_.relative(p_49292_.getBlockState(p_49293_).getValue(PLACING)), this);
	}
	
	@Override
	public int getSignal(BlockState p_49309_, BlockGetter p_49310_, BlockPos p_49311_, Direction p_49312_)
	{
		return this.plateMethods.getSignalForStatePublic(p_49309_);
	}

	@Override
	public int getDirectSignal(BlockState p_49346_, BlockGetter p_49347_, BlockPos p_49348_, Direction p_49349_)
	{
		return p_49349_ == p_49346_.getValue(PLACING).getOpposite() ? plateMethods.getSignalForStatePublic(p_49346_) : 0;
	}

	@Override
	@Deprecated
	public boolean isSignalSource(BlockState p_49351_)
	{
		return parentBlock.isSignalSource(p_49351_);
	}

	@Override
	@Deprecated
	public PushReaction getPistonPushReaction(BlockState p_49353_)
	{
		return parentBlock.getPistonPushReaction(p_49353_);
	}
}
