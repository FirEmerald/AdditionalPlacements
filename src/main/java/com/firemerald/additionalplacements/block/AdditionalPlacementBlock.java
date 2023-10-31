package com.firemerald.additionalplacements.block;

import java.util.*;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Triple;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.interfaces.IPlacementBlock;
import com.firemerald.additionalplacements.common.AdditionalPlacementsBlockTags;

import net.minecraft.block.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;

public abstract class AdditionalPlacementBlock<T extends Block> extends Block implements IPlacementBlock<T>
{
	private static Set<Property<?>> copyPropsStatic;
	protected final T parentBlock;
	private final Property<?>[] copyProps;

	public AdditionalPlacementBlock(T parentBlock)
	{
		super(theHack(parentBlock));
		this.copyProps = copyPropsStatic.toArray(new Property[copyPropsStatic.size()]);
		copyPropsStatic = null;
		this.parentBlock = parentBlock;
	}

	@Override
	public T getOtherBlock()
	{
		return parentBlock;
	}

	public static Properties theHack(Block parentBlock)
	{
		Set<Property<?>> props = new HashSet<>(parentBlock.defaultBlockState().getProperties());
		if (parentBlock instanceof SlabBlock) props.remove(SlabBlock.TYPE);
		else if (parentBlock instanceof StairsBlock)
		{
			props.remove(StairsBlock.FACING);
			props.remove(StairsBlock.SHAPE);
			props.remove(StairsBlock.HALF);
		}
		copyPropsStatic = props;
		return Properties.copy(parentBlock);
	}

	public boolean hasCustomColors()
	{
		return false;
	}

	public Property<?>[] getCopyProps()
	{
		return copyProps;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BlockState copyProperties(BlockState from, BlockState to)
	{
		for (Property prop : copyProps) to = to.setValue(prop, from.getValue(prop));
		return to;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		Set<Property<?>> invalid = new HashSet<>();
		copyPropsStatic.forEach(prop -> {
			try
			{
				builder.add(prop);
			}
			catch (IllegalArgumentException e)
			{
				invalid.add(prop);
			}
		});
		copyPropsStatic.removeAll(invalid);
	}

	@Override
	public Item asItem()
	{
		return parentBlock.asItem();
	}

	@Override
	public String getDescriptionId()
	{
		return parentBlock.getDescriptionId();
	}

	@Deprecated
	public BlockState getModelState()
	{
		return getOtherBlock().defaultBlockState();
	}

	public BlockState getModelState(BlockState worldState)
	{
		return copyProperties(worldState, getModelState());
	}

	@Override
	@Deprecated
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)
	{
		return parentBlock.getDrops(this.getDefaultVanillaState(state), builder);
	}

	@Override
	@Deprecated
	public ItemStack getCloneItemStack(IBlockReader level, BlockPos pos, BlockState state)
	{
		return parentBlock.getCloneItemStack(level, pos, state);
	}

	@Override
	public void animateTick(BlockState state, World level, BlockPos pos, Random random)
	{
		BlockState modelState = getModelState(state);
		modelState.getBlock().animateTick(modelState, level, pos, random);
	}

	@Override
	public void fallOn(World level, BlockPos pos, Entity entity, float damage)
	{
		try
		{
			getOtherBlock().fallOn(level, pos, entity, damage);
		}
		catch (Exception e)
		{
			AdditionalPlacementsMod.LOGGER.error("Block errored during \"fallOn\", cannot provide intended  behavior. defaulting.", e);
			super.fallOn(level, pos, entity, damage);
		}
	}

	@Override
	public void updateEntityAfterFallOn(IBlockReader level, Entity entity)
	{
		getOtherBlock().updateEntityAfterFallOn(level, entity);
	}

	@Override
	public void stepOn(World level, BlockPos pos, Entity entity)
	{
		try
		{
			getOtherBlock().stepOn(level, pos, entity);
		}
		catch (Exception e)
		{
			AdditionalPlacementsMod.LOGGER.error("Block errored during \"stepOn\", cannot provide intended  behavior. defaulting.", e);
			super.stepOn(level, pos, entity);
		}
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState state)
	{
		return true;
	}

	@Override
	public void attack(BlockState state, World level, BlockPos pos, PlayerEntity player)
	{
		getModelState(state).attack(level, pos, player);
	}

	@Override
	public void destroy(IWorld level, BlockPos pos, BlockState state)
	{
		BlockState modelState = getModelState(state);
		modelState.getBlock().destroy(level, pos, modelState);
	}

	@Override
	@Deprecated
	public float getExplosionResistance()
	{
		return getOtherBlock().getExplosionResistance();
	}

	@Override
	@Deprecated
	public void onPlace(BlockState state, World level, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		if (!state.is(oldState.getBlock()))
		{
			BlockState modelState = getModelState(state);
			modelState.neighborChanged(level, pos, Blocks.AIR, pos, isMoving);
			modelState.getBlock().onPlace(modelState, level, pos, oldState, isMoving);
		}
	}

	@Override
	public void onRemove(BlockState state, World level, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (!state.is(newState.getBlock()))
		{
			getModelState(state).onRemove(level, pos, newState, isMoving);
		}
	}

	@Override
	public boolean isRandomlyTicking(BlockState state)
	{
		return getOtherBlock().isRandomlyTicking(getModelState(state));
	}

	@Override
	@Deprecated
	public void randomTick(BlockState state, ServerWorld level, BlockPos pos, Random rand)
	{
		BlockState modelState = getModelState(state);
		modelState.getBlock().randomTick(modelState, level, pos, rand);
		applyChanges(state, modelState, level, pos);
	}

	@Override
	@Deprecated
	public void tick(BlockState state, ServerWorld level, BlockPos pos, Random rand)
	{
		BlockState modelState = getModelState(state);
		modelState.getBlock().tick(modelState, level, pos, rand);
		applyChanges(state, modelState, level, pos);
	}

	@Override
	public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hitResult)
	{
		BlockState modelState = getModelState(state);
		ActionResultType res = getModelState(state).use(level, player, hand, hitResult);
		applyChanges(state, modelState, level, pos);
		return res;
	}

	public void applyChanges(BlockState oldState, BlockState modelState, World level, BlockPos pos)
	{
		BlockState newState = level.getBlockState(pos);
		if (newState.getBlock() != this) //block has changed
		{
			if (newState.getBlock() instanceof IPlacementBlock)
			{
				IPlacementBlock<?> placement = (IPlacementBlock<?>) newState.getBlock();
				if (placement.hasAdditionalStates())
				{
					BlockState changedState = placement.getOtherBlock().defaultBlockState();
					for (Property<?> property : changedState.getProperties())
					{
						if (newState.hasProperty(property)) changedState = copy(property, newState, changedState);
						else if (oldState.hasProperty(property)) changedState = copy(property, oldState, changedState);
					}
					level.setBlock(pos, changedState, 3);
				}
			}
		}
	}

	public static <V extends Comparable<V>> BlockState copy(Property<V> property, BlockState from, BlockState to)
	{
		return to.setValue(property, from.getValue(property));
	}

	@Override
	public void wasExploded(World level, BlockPos pos, Explosion explosion)
	{
		getOtherBlock().wasExploded(level, pos, explosion);
	}

	@Override
	public boolean isPathfindable(BlockState state, IBlockReader level, BlockPos pos, PathType pathType)
	{
		return false;
	}

	@Nullable
	public Triple<Block, Collection<ResourceLocation>, Collection<ResourceLocation>> checkTagMismatch()
	{
		Set<ResourceLocation> desiredTags = getDesiredTags();
		Set<ResourceLocation> hasTags = getTags();
		List<ResourceLocation> hasTagsList = new ArrayList<>(hasTags);
		List<ResourceLocation> desiredTagsList = new ArrayList<>(desiredTags);
		hasTagsList.removeAll(desiredTags);
		desiredTagsList.removeAll(hasTags);
		if (!hasTagsList.isEmpty() || !desiredTagsList.isEmpty()) return Triple.of(this, desiredTagsList, hasTagsList);
		else return null;
	}

	public Set<ResourceLocation> getDesiredTags()
	{
		return modifyTags(parentBlock.getTags());
	}

	public abstract String getTagTypeName();

	public abstract String getTagTypeNamePlural();

	public Set<ResourceLocation> modifyTags(Set<ResourceLocation> tags)
	{
		return AdditionalPlacementsBlockTags.remap(tags, getTagTypeName(), getTagTypeNamePlural());
	}

	@Override
	public boolean hasAdditionalStates()
	{
		return true;
	}

	@Override
	public boolean isThis(BlockState blockState)
	{
		return blockState.is(this) || blockState.is(parentBlock);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return getStateForPlacementImpl(context, this.defaultBlockState());
	}

	@Override
	public BlockState rotate(BlockState blockState, Rotation rotation)
	{
		return rotateImpl(blockState, rotation);
	}

	@Override
	public BlockState mirror(BlockState blockState, Mirror mirror)
	{
		return mirrorImpl(blockState, mirror);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable IBlockReader level, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		appendHoverTextImpl(stack, level, tooltip, flag);
	}

	@Override
	@Deprecated
	public BlockState updateShape(BlockState state, Direction direction, BlockState otherState, IWorld level, BlockPos pos, BlockPos otherPos)
	{
		return updateShapeImpl(state, direction, otherState, level, pos, otherPos);
	}

	@Override
	@Deprecated
	public FluidState getFluidState(BlockState state)
	{
		return this.getModelState(state).getFluidState();
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader level, BlockPos pos)
	{
		return this.getModelState(state).propagatesSkylightDown(level, pos);
	}

	@Override
	public float getShadeBrightness(BlockState state, IBlockReader level, BlockPos pos)
	{
		return this.getModelState(state).getShadeBrightness(level, pos);
	}

	@Override
	public float[] getBeaconColorMultiplier(BlockState state, IWorldReader level, BlockPos pos1, BlockPos pos2)
	{
		return this.getModelState(state).getBeaconColorMultiplier(level, pos1, pos2);
	}
}
