package com.firemerald.dvsas.block;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.collect.Streams;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public abstract class VerticalBlock<T extends Block> extends Block implements IVerticalBlock
{
	protected final T parentBlock;
	public final Supplier<BlockState> model;

	public VerticalBlock(T parentBlock, Supplier<BlockState> model)
	{
		super(BlockBehaviour.Properties.copy(parentBlock));
		this.parentBlock = parentBlock;
		this.model = model;
	}

	@Override
	public Item asItem()
	{
		return getModelBlock().asItem();
	}

	@Override
	public String getDescriptionId()
	{
		return getModelBlock().getDescriptionId();
	}

	@Deprecated
	public BlockState getModelState()
	{
		return model.get();
	}

	public BlockState getModelState(BlockState worldState)
	{
		return getModelState();
	}

	@Deprecated
	public Block getModelBlock()
	{
		return getModelState().getBlock();
	}

	public Block getModelBlock(BlockState worldState)
	{
		return getModelState(worldState).getBlock();
	}

	@Deprecated
	public BlockState getVisualState()
	{
		return getVisualBlock().defaultBlockState();
	}

	public BlockState getVisualState(BlockState worldState)
	{
		return getVisualState();
	}

	@Deprecated
	public Block getVisualBlock()
	{
		return parentBlock;
	}

	public Block getVisualBlock(BlockState worldState)
	{
		return getVisualBlock();
	}

	@Override
	@Deprecated
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)
	{
		return parentBlock.getDrops(this.getDefaultHorizontalState(state, getFluidState(state)), builder);
	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player)
	{
		return parentBlock.getCloneItemStack(state, target, level, pos, player);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, Random random)
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
		getModelBlock().updateEntityAfterFallOn(level, entity);
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
	public boolean useShapeForLightOcclusion(BlockState p_56967_)
	{
		return true;
	}

	@Override
	public void attack(BlockState state, Level p_56897_, BlockPos p_56898_, Player p_56899_)
	{
		getModelState(state).attack(p_56897_, p_56898_, p_56899_);
	}

	@Override
	public void destroy(LevelAccessor p_56882_, BlockPos p_56883_, BlockState state)
	{
		BlockState modelState = getModelState(state);
		modelState.getBlock().destroy(p_56882_, p_56883_, modelState);
	}

	@Override
	@Deprecated
	public float getExplosionResistance()
	{
		return getModelBlock().getExplosionResistance();
	}

	@Override
	@Deprecated
	public void onPlace(BlockState state, Level p_56962_, BlockPos p_56963_, BlockState p_56964_, boolean p_56965_)
	{
		if (!state.is(p_56964_.getBlock()))
		{
			BlockState modelState = getModelState(state);
			modelState.neighborChanged(p_56962_, p_56963_, Blocks.AIR, p_56963_, false);
			modelState.getBlock().onPlace(modelState, p_56962_, p_56963_, p_56964_, false);
		}
	}

	@Override
	public void onRemove(BlockState state, Level p_56909_, BlockPos p_56910_, BlockState p_56911_, boolean p_56912_)
	{
		if (!state.is(p_56911_.getBlock()))
		{
			getModelState(state).onRemove(p_56909_, p_56910_, p_56911_, p_56912_);
		}
	}

	@Override
	public boolean isRandomlyTicking(BlockState state)
	{
		return getModelBlock(state).isRandomlyTicking(state);
	}

	@Override
	@Deprecated
	public void randomTick(BlockState state, ServerLevel p_56952_, BlockPos p_56953_, Random p_56954_)
	{
		BlockState modelState = getModelState(state);
		modelState.getBlock().randomTick(modelState, p_56952_, p_56953_, p_56954_);
	}

	@Override
	@Deprecated
	public void tick(BlockState state, ServerLevel p_56887_, BlockPos p_56888_, Random p_56889_)
	{
		BlockState modelState = getModelState(state);
		modelState.getBlock().tick(modelState, p_56887_, p_56888_, p_56889_);
	}

	@Override
	public InteractionResult use(BlockState state, Level p_56902_, BlockPos p_56903_, Player p_56904_, InteractionHand p_56905_, BlockHitResult p_56906_)
	{
		return getModelState(state).use(p_56902_, p_56904_, p_56905_, p_56906_);
	}

	@Override
	public void wasExploded(Level p_56878_, BlockPos p_56879_, Explosion p_56880_)
	{
		getModelBlock().wasExploded(p_56878_, p_56879_, p_56880_);
	}

	@Override
	public boolean isPathfindable(BlockState p_56891_, BlockGetter p_56892_, BlockPos p_56893_, PathComputationType p_56894_)
	{
		return false;
	}

	@SuppressWarnings("deprecation")
	public void bindTags()
	{
		this.builtInRegistryHolder().bindTags(Streams.concat(modifyTags(parentBlock.builtInRegistryHolder().tags().collect(Collectors.toSet())).stream(), this.builtInRegistryHolder().tags()).collect(Collectors.toSet())); //add the model block's tags
	}

	public abstract Collection<TagKey<Block>> modifyTags(Collection<TagKey<Block>> tags);


	@Override
	public boolean hasVertical()
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
		return getStateForPlacementImpl(context);
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

	@SuppressWarnings("deprecation")
	@Override
	public FluidState getFluidStateImpl(BlockState blockState)
	{
		return this.getFluidState(blockState);
	}
}
