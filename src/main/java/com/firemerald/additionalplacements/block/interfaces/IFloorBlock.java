package com.firemerald.additionalplacements.block.interfaces;

import java.util.List;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public interface IFloorBlock<T extends Block> extends IPlacementBlock<T>
{
	@Override
    default BlockState transform(BlockState blockState, Function<Direction, Direction> transform)
	{
		Direction placing = getPlacing(blockState);
		return placing == null ? blockState : forPlacing(transform.apply(placing), blockState);
	}

	@Override
    default BlockState getStateForPlacementImpl(BlockPlaceContext context, BlockState currentState)
	{
		return forPlacing(getPlacingDirection(context), currentState);
	}

	BlockState forPlacing(Direction dir, BlockState blockState);

	Direction getPlacing(BlockState blockState);

	default Direction getPlacingDirection(BlockPlaceContext context)
	{
		return context.getClickedFace().getOpposite();
	}

	@Override
	@Environment(EnvType.CLIENT)
    default void renderPlacementHighlight(PoseStack pose, VertexConsumer vertexConsumer, Player player, BlockHitResult result, float partial, float r, float g, float b, float a) {}

    @Override
    default void addPlacementTooltip(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(new TranslatableComponent("tooltip.additionalplacements.vertical_placement"));
		tooltip.add(new TranslatableComponent("tooltip.additionalplacements.ceiling_placement"));
	}

	@Environment(EnvType.CLIENT)
    default Direction transformModelDirection(Direction from)
	{
		return from;
	}
}