package com.firemerald.additionalplacements.block;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Triple;

import com.firemerald.additionalplacements.block.interfaces.IPlacementBlock;
import com.firemerald.additionalplacements.client.models.definitions.StateModelDefinition;
import com.firemerald.additionalplacements.common.AdditionalPlacementsBlockTags;
import com.firemerald.additionalplacements.util.BlockRotation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public abstract class AdditionalPlacementBlock<T extends Block> extends Block implements IPlacementBlock<T>
{
	private static final List<Property<?>> copyPropsStatic = new ArrayList<>();
	public final T parentBlock;
	private final Property<?>[] copyProps;

	public AdditionalPlacementBlock(T parentBlock, ResourceKey<Block> id)
	{
		super(theHack(parentBlock, id));
		this.copyProps = copyPropsStatic.toArray(Property[]::new);
		copyPropsStatic.clear();
		this.parentBlock = parentBlock;
	}

	@Override
	public T getOtherBlock()
	{
		return parentBlock;
	}

	public static Properties theHack(Block parentBlock, ResourceKey<Block> id)
	{
		copyPropsStatic.addAll(parentBlock.defaultBlockState().getProperties());
		return BlockBehaviour.Properties.ofFullCopy(parentBlock).setId(id);
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

	public boolean isValidProperty(Property<?> prop) {
		return true;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		Set<Property<?>> invalid = new HashSet<>();
		copyPropsStatic.forEach(prop -> {
			if (isValidProperty(prop)) builder.add(prop);
			else invalid.add(prop);
		});
		copyPropsStatic.removeAll(invalid);
	}

	@Override
	public Item asItem()
	{
		return parentBlock.asItem();
	}

	public BlockState getOtherBlockState() {
		return getOtherBlock().defaultBlockState();
	}

	public BlockState getModelState(BlockState worldState)
	{
		return withUnrotatedPlacement(worldState, copyProperties(worldState, getOtherBlockState()));
	}

	public abstract BlockState withUnrotatedPlacement(BlockState worldState, BlockState modelState);

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder)
	{
		return getModelState(state).getDrops(builder);
	}

	@Override
	public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player)
	{
		return getModelState(state).getCloneItemStack(pos, level, includeData, player);
	}

	@Override
	public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData)
	{
		return getModelState(state).getCloneItemStack(level, pos, includeData);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
	{
		BlockState modelState = getModelState(state);
		modelState.getBlock().animateTick(modelState, level, pos, random);
	}

	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double damage)
	{
		BlockState modelState = getModelState(state);
		modelState.getBlock().fallOn(level, modelState, pos, entity, damage);
	}

	@Override
	public void updateEntityMovementAfterFallOn(BlockGetter level, Entity entity)
	{
		getOtherBlock().updateEntityMovementAfterFallOn(level, entity);
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
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		if (!state.is(oldState.getBlock()))
		{
			BlockState modelState = getModelState(state);
			modelState.handleNeighborChanged(level, pos, Blocks.AIR, null, isMoving);
			modelState.onPlace(level, pos, oldState, isMoving);
		}
	}

	@Override
	public void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean isMoving)
	{
		getModelState(state).affectNeighborsAfterRemoval(level, pos, isMoving);
	}

	@Override
	public boolean isRandomlyTicking(BlockState state)
	{
		return getModelState(state).isRandomlyTicking();
	}

	@Override
	@Deprecated
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand)
	{
		BlockState modelState = getModelState(state);
		modelState.randomTick(level, pos, rand);
		applyChanges(state, modelState, level, pos);
	}

	@Override
	@Deprecated
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand)
	{
		BlockState modelState = getModelState(state);
		modelState.tick(level, pos, rand);
		applyChanges(state, modelState, level, pos);
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
	{
		BlockState modelState = getModelState(state);
		InteractionResult res = modelState.useWithoutItem(level, player, hitResult);
		applyChanges(state, modelState, level, pos);
		return res;
	}

	public void applyChanges(BlockState oldState, BlockState modelState, Level level, BlockPos pos)
	{
		BlockState newState = level.getBlockState(pos);
		if (newState.getBlock() != this) //block has changed
		{
			if (newState.getBlock() instanceof IPlacementBlock<?> placement)
			{
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
	public void wasExploded(ServerLevel level, BlockPos pos, Explosion explosion)
	{
		getOtherBlock().wasExploded(level, pos, explosion);
	}

	@Override
	public boolean isPathfindable(BlockState state, PathComputationType pathComputationType)
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
		return modifyTags(parentBlock.builtInRegistryHolder().tags());
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
	public BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos otherPos, BlockState otherState, RandomSource rand)
	{
		return updateShapeImpl(state, level, tickAccess, pos, direction, otherPos, otherState, rand);
	}

	@Override
	public FluidState getFluidState(BlockState state)
	{
		return this.getModelState(state).getFluidState();
	}

	@Override
	public boolean skipRendering(BlockState thisState, BlockState adjacentState, Direction dir)
	{
		return this.getModelState(thisState).skipRendering(adjacentState, dir);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state)
	{
		return this.getModelState(state).propagatesSkylightDown();
	}

	@Override
	public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos)
	{
		return this.getModelState(state).getShadeBrightness(level, pos);
	}

	@Override
	public Integer getBeaconColorMultiplier(BlockState state, LevelReader level, BlockPos pos1, BlockPos pos2)
	{
		return this.getModelState(state).getBeaconColorMultiplier(level, pos1, pos2);
	}

	@Override
	public boolean isSignalSource(BlockState state)
	{
		return getModelState(state).isSignalSource();
	}

	@Override
	@Deprecated
	public PushReaction getPistonPushReaction(BlockState state)
	{
		return parentBlock.getPistonPushReaction(this.getModelState(state));
	}

	public abstract boolean rotatesLogic(BlockState state);

	public abstract boolean rotatesTexture(BlockState state);

	public abstract boolean rotatesModel(BlockState state);

	public abstract BlockRotation getRotation(BlockState state);


	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		if (rotatesModel(state))
			return getRotation(state).applyBlockSpace(getModelState(state).getShape(level, pos, context));
		else
			return getShapeInternal(state, level, pos, context);
	}

	public abstract VoxelShape getShapeInternal(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context);

	@Override
	public boolean canGenerateAdditionalStates() {
		return false;
	}

	@OnlyIn(Dist.CLIENT)
	public abstract ResourceLocation getBaseModelPrefix();

	@OnlyIn(Dist.CLIENT)
	public abstract StateModelDefinition getModelDefinition(BlockState state);
}
