package com.firemerald.additionalplacements.block;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Triple;

import com.firemerald.additionalplacements.block.interfaces.IPlacementBlock;
import com.firemerald.additionalplacements.common.AdditionalPlacementsBlockTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public abstract class AdditionalPlacementBlock<T extends Block> extends Block implements IPlacementBlock<T>
{
	private static Set<Property<?>> copyPropsStatic;
	protected final T parentBlock;
	private final Property<?>[] copyProps;

	public AdditionalPlacementBlock(T parentBlock)
	{
		super(theHack(parentBlock));
		this.copyProps = copyPropsStatic.toArray(Property[]::new);
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
		else if (parentBlock instanceof StairBlock)
		{
			props.remove(StairBlock.FACING);
			props.remove(StairBlock.SHAPE);
			props.remove(StairBlock.HALF);
		}
		copyPropsStatic = props;
		return BlockBehaviour.Properties.copy(parentBlock);
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
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
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
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player)
	{
		return parentBlock.getCloneItemStack(state, target, level, pos, player);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
	{
		BlockState modelState = getModelState(state);
		modelState.getBlock().animateTick(modelState, level, pos, random);
	}

	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float damage)
	{
		BlockState modelState = getModelState(state);
		modelState.getBlock().fallOn(level, modelState, pos, entity, damage);
	}

	@Override
	public void updateEntityAfterFallOn(BlockGetter level, Entity entity)
	{
		getOtherBlock().updateEntityAfterFallOn(level, entity);
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity)
	{
		BlockState modelState = getModelState(state);
		modelState.getBlock().stepOn(level, pos, modelState, entity);
	}

	@Override
	public void handlePrecipitation(BlockState state, Level level, BlockPos pos, Biome.Precipitation precipitation)
	{
		BlockState modelState = getModelState(state);
		modelState.getBlock().handlePrecipitation(modelState, level, pos, precipitation);
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState state)
	{
		return true;
	}

	@Override
	public void attack(BlockState state, Level level, BlockPos pos, Player player)
	{
		getModelState(state).attack(level, pos, player);
	}

	@Override
	public void destroy(LevelAccessor level, BlockPos pos, BlockState state)
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
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		if (!state.is(oldState.getBlock()))
		{
			BlockState modelState = getModelState(state);
			modelState.neighborChanged(level, pos, Blocks.AIR, pos, isMoving);
			modelState.getBlock().onPlace(modelState, level, pos, oldState, isMoving);
		}
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		if (!state.is(oldState.getBlock()))
		{
			getModelState(state).onRemove(level, pos, oldState, isMoving);
		}
	}

	@Override
	public boolean isRandomlyTicking(BlockState state)
	{
		return getOtherBlock().isRandomlyTicking(getModelState(state));
	}

	@Override
	@Deprecated
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand)
	{
		BlockState modelState = getModelState(state);
		modelState.getBlock().randomTick(modelState, level, pos, rand);
		applyChanges(state, modelState, level, pos);
	}

	@Override
	@Deprecated
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand)
	{
		BlockState modelState = getModelState(state);
		modelState.getBlock().tick(modelState, level, pos, rand);
		applyChanges(state, modelState, level, pos);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
	{
		BlockState modelState = getModelState(state);
		InteractionResult res = getModelState(state).use(level, player, hand, hitResult);
		applyChanges(state, modelState, level, pos);
		return res;
	}

	public void applyChanges(BlockState oldState, BlockState modelState, Level level, BlockPos pos)
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
	public void wasExploded(Level level, BlockPos pos, Explosion explosion)
	{
		getOtherBlock().wasExploded(level, pos, explosion);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType pathType)
	{
		return false;
	}

	@SuppressWarnings("deprecation")
	@Nullable
	public Triple<Block, Collection<TagKey<Block>>, Collection<TagKey<Block>>> checkTagMismatch()
	{
		Set<TagKey<Block>> desiredTags = getDesiredTags();
		Set<TagKey<Block>> hasTags = this.builtInRegistryHolder().tags().collect(Collectors.toSet());
		List<TagKey<Block>> hasTagsList = new ArrayList<>(hasTags);
		List<TagKey<Block>> desiredTagsList = new ArrayList<>(desiredTags);
		hasTagsList.removeAll(desiredTags);
		desiredTagsList.removeAll(hasTags);
		if (!hasTagsList.isEmpty() || !desiredTagsList.isEmpty()) return Triple.of(this, desiredTagsList, hasTagsList);
		else return null;
	}

	@SuppressWarnings("deprecation")
	public Set<TagKey<Block>> getDesiredTags()
	{
		return modifyTags(parentBlock.builtInRegistryHolder().getTagKeys());
	}

	public abstract String getTagTypeName();

	public abstract String getTagTypeNamePlural();

	public Set<TagKey<Block>> modifyTags(Stream<TagKey<Block>> tags)
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
	public BlockState getStateForPlacement(BlockPlaceContext context)
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
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag)
	{
		appendHoverTextImpl(stack, level, tooltip, flag);
	}

	@Override
	@Deprecated
	public BlockState updateShape(BlockState state, Direction direction, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos)
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
	@Deprecated
	public boolean skipRendering(BlockState thisState, BlockState adjacentState, Direction dir)
	{
		return this.getModelState(thisState).skipRendering(adjacentState, dir);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos)
	{
		return this.getModelState(state).propagatesSkylightDown(level, pos);
	}

	@Override
	public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos)
	{
		return this.getModelState(state).getShadeBrightness(level, pos);
	}

	@Override
	public float[] getBeaconColorMultiplier(BlockState state, LevelReader level, BlockPos pos1, BlockPos pos2)
	{
		return this.getModelState(state).getBeaconColorMultiplier(level, pos1, pos2);
	}
}
