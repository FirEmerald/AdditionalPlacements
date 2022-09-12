package com.firemerald.vsas.block;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public interface IVerticalBlock extends ItemLike, ICustomBlockHighlight
{	
	public BlockState rotateImpl(BlockState blockState, Rotation rotation);

	public BlockState mirrorImpl(BlockState blockState, Mirror mirror);

	public BlockState getStateForPlacementImpl(BlockPlaceContext context);

	public default void appendHoverTextImpl(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag)
	{
		if (hasVertical()) tooltip.add(new TranslatableComponent("vsas.tooltip.verticalplacement"));
	}
	
	public boolean hasVertical();
	
	public BlockState getDefaultVerticalState(BlockState currentState, FluidState fluidState);
	
	public BlockState getDefaultHorizontalState(BlockState currentState, FluidState fluidState);
	
	public FluidState getFluidState(BlockState blockState);
	
	public boolean isThis(BlockState blockState);
}