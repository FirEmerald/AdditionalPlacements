package com.firemerald.additionalplacements.block.interfaces;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
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
	public default BlockState transform(BlockState blockState, Function<Direction, Direction> transform)
	{
		Direction placing = getPlacing(blockState);
		return placing == null ? blockState : forPlacing(transform.apply(placing), blockState);
	}

	@Override
	public default BlockState getStateForPlacementImpl(BlockPlaceContext context, BlockState currentState)
	{
		return forPlacing(getPlacingDirection(context), currentState);
	}

	public abstract BlockState forPlacing(Direction dir, BlockState blockState);

	public abstract Direction getPlacing(BlockState blockState);

	public default Direction getPlacingDirection(BlockPlaceContext context)
	{
		return context.getClickedFace().getOpposite();
	}

	@Override
	@Environment(EnvType.CLIENT)
	public default void renderPlacementHighlight(PoseStack pose, VertexConsumer vertexConsumer, Player player, BlockHitResult result, float partial) {}

    @Override
	public default void addPlacementTooltip(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(Component.translatable("tooltip.additionalplacements.vertical_placement"));
		tooltip.add(Component.translatable("tooltip.additionalplacements.ceiling_placement"));
	}

	@Environment(EnvType.CLIENT)
	public default Direction transformModelDirection(Direction from)
	{
		return from;
	}

	@Environment(EnvType.CLIENT)
	public default Function<Direction, Direction> getModelDirectionFunction(BlockState state, Random rand)
	{
		return switch(getPlacing(state)) {
		case UP -> side -> switch (side) {
		case UP -> Direction.DOWN;
		case DOWN -> Direction.UP;
		case NORTH -> Direction.SOUTH;
		case SOUTH -> Direction.NORTH;
		default -> side;
		};
		case NORTH -> side -> switch (side) {
		case UP -> Direction.NORTH;
		case DOWN -> Direction.SOUTH;
		case NORTH -> Direction.DOWN;
		case SOUTH -> Direction.UP;
		default -> side;
		};
		case SOUTH -> side -> switch (side) {
		case UP -> Direction.NORTH;
		case DOWN -> Direction.SOUTH;
		case NORTH -> Direction.UP;
		case SOUTH -> Direction.DOWN;
		case EAST -> Direction.WEST;
		case WEST -> Direction.EAST;
		default -> side;
		};
		case EAST -> side -> switch (side) {
		case UP -> Direction.NORTH;
		case DOWN -> Direction.SOUTH;
		case NORTH -> Direction.WEST;
		case SOUTH -> Direction.EAST;
		case EAST -> Direction.DOWN;
		case WEST -> Direction.UP;
		default -> side;
		};
		case WEST -> side -> switch (side) {
		case UP -> Direction.NORTH;
		case DOWN -> Direction.SOUTH;
		case NORTH -> Direction.EAST;
		case SOUTH -> Direction.WEST;
		case EAST -> Direction.UP;
		case WEST -> Direction.DOWN;
		default -> side;
		};
		default -> Function.identity();
		};
	}
}