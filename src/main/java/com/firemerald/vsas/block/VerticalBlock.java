package com.firemerald.vsas.block;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.collect.Streams;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public abstract class VerticalBlock<T extends Block> extends Block implements IVerticalBlock
{
	protected final T parentBlock;
	protected final BlockState model;
	protected final Block modelBlock;

	public VerticalBlock(T parentBlock, BlockState model)
	{
		super(BlockBehaviour.Properties.copy(model.getBlock()));
		this.parentBlock = parentBlock;
		this.model = model;
		this.modelBlock = model.getBlock();
	}
	
	public void onFinishedRegistering()
	{
		this.item = parentBlock.asItem();
		this.descriptionId = parentBlock.getDescriptionId();
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
		modelBlock.animateTick(model, level, pos, random);
	}

	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float damage)
	{
		modelBlock.fallOn(level, model, pos, entity, damage);
	}

	@Override
	public void updateEntityAfterFallOn(BlockGetter level, Entity entity)
	{
		modelBlock.updateEntityAfterFallOn(level, entity);
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity)
	{
		modelBlock.stepOn(level, pos, state, entity);
	}

	@Override
	public void handlePrecipitation(BlockState state, Level level, BlockPos pos, Biome.Precipitation precipitation)
	{
		modelBlock.handlePrecipitation(model, level, pos, precipitation);
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState p_56967_)
	{
		return true;
	}

	@Override
	public void attack(BlockState p_56896_, Level p_56897_, BlockPos p_56898_, Player p_56899_)
	{
		this.model.attack(p_56897_, p_56898_, p_56899_);
	}

	@Override
	public void destroy(LevelAccessor p_56882_, BlockPos p_56883_, BlockState p_56884_)
	{
		this.modelBlock.destroy(p_56882_, p_56883_, p_56884_);
	}

	@Override
	@Deprecated
	public float getExplosionResistance()
	{
		return this.modelBlock.getExplosionResistance();
	}

	@Override
	@Deprecated
	public void onPlace(BlockState p_56961_, Level p_56962_, BlockPos p_56963_, BlockState p_56964_, boolean p_56965_)
	{
		if (!p_56961_.is(p_56961_.getBlock()))
		{
			this.model.neighborChanged(p_56962_, p_56963_, Blocks.AIR, p_56963_, false);
			this.modelBlock.onPlace(this.model, p_56962_, p_56963_, p_56964_, false);
		}
	}

	@Override
	public void onRemove(BlockState p_56908_, Level p_56909_, BlockPos p_56910_, BlockState p_56911_, boolean p_56912_)
	{
		if (!p_56908_.is(p_56911_.getBlock()))
		{
			this.model.onRemove(p_56909_, p_56910_, p_56911_, p_56912_);
		}
	}

	@Override
	public boolean isRandomlyTicking(BlockState p_56947_)
	{
		return this.modelBlock.isRandomlyTicking(p_56947_);
	}

	@Override
	@Deprecated
	public void randomTick(BlockState p_56951_, ServerLevel p_56952_, BlockPos p_56953_, Random p_56954_)
	{
		this.modelBlock.randomTick(p_56951_, p_56952_, p_56953_, p_56954_);
	}

	@Override
	@Deprecated
	public void tick(BlockState p_56886_, ServerLevel p_56887_, BlockPos p_56888_, Random p_56889_)
	{
		this.modelBlock.tick(p_56886_, p_56887_, p_56888_, p_56889_);
	}

	@Override
	public InteractionResult use(BlockState p_56901_, Level p_56902_, BlockPos p_56903_, Player p_56904_, InteractionHand p_56905_, BlockHitResult p_56906_)
	{
		return this.model.use(p_56902_, p_56904_, p_56905_, p_56906_);
	}

	@Override
	public void wasExploded(Level p_56878_, BlockPos p_56879_, Explosion p_56880_)
	{
		this.modelBlock.wasExploded(p_56878_, p_56879_, p_56880_);
	}

	@Override
	public boolean isPathfindable(BlockState p_56891_, BlockGetter p_56892_, BlockPos p_56893_, PathComputationType p_56894_)
	{
		return false;
	}

	@SuppressWarnings("deprecation")
	public void bindTags()
	{
		this.builtInRegistryHolder().bindTags(Streams.concat(parentBlock.builtInRegistryHolder().tags(), this.builtInRegistryHolder().tags()).collect(Collectors.toSet())); //add the model block's tags
	}

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
}
