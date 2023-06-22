package com.firemerald.additionalplacements.block.interfaces;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.IModelData;

public interface IFloorBlock<T extends Block> extends IPlacementBlock<T>
{
	@Override
	public default BlockState transform(BlockState blockState, Function<Direction, Direction> transform)
	{
		Direction placing = getPlacing(blockState);
		return placing == null ? blockState : forPlacing(transform.apply(placing), blockState);
	}

	@Override
	public default BlockState getStateForPlacementImpl(BlockItemUseContext context, BlockState currentState)
	{
		return forPlacing(getPlacingDirection(context), currentState);
	}

	public abstract BlockState forPlacing(Direction dir, BlockState blockState);

	public abstract Direction getPlacing(BlockState blockState);

	public default Direction getPlacingDirection(BlockItemUseContext context)
	{
		return context.getClickedFace().getOpposite();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public default void renderPlacementHighlight(MatrixStack pose, IVertexBuilder vertexConsumer, PlayerEntity player, BlockRayTraceResult result, float partial) {}

    @Override
	public default void addPlacementTooltip(ItemStack stack, @Nullable IBlockReader level, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent("tooltip.additionalplacements.vertical_placement"));
		tooltip.add(new TranslationTextComponent("tooltip.additionalplacements.ceiling_placement"));
	}

	@OnlyIn(Dist.CLIENT)
	public default Direction transformModelDirection(Direction from)
	{
		return from;
	}

	@OnlyIn(Dist.CLIENT)
	public default Function<Direction, Direction> getModelDirectionFunction(BlockState state, Random rand, IModelData extraData)
	{
		switch (getPlacing(state)) {
		case UP: return side -> {
			switch (side) {
			case UP: return Direction.DOWN;
			case DOWN: return Direction.UP;
			case NORTH: return Direction.SOUTH;
			case SOUTH: return Direction.NORTH;
			default: return side;
			}
		};
		case NORTH: return side -> {
			switch (side) {
			case UP: return Direction.NORTH;
			case DOWN: return Direction.SOUTH;
			case NORTH: return Direction.DOWN;
			case SOUTH: return Direction.UP;
			default: return side;
			}
		};
		case SOUTH: return side -> { 
			switch (side) {
			case UP: return Direction.NORTH;
			case DOWN: return Direction.SOUTH;
			case NORTH: return Direction.UP;
			case SOUTH: return Direction.DOWN;
			case EAST: return Direction.WEST;
			case WEST: return Direction.EAST;
			default: return side;
			}
		};
		case EAST: return side -> {
			switch (side) {
			case UP: return Direction.NORTH;
			case DOWN: return Direction.SOUTH;
			case NORTH: return Direction.WEST;
			case SOUTH: return Direction.EAST;
			case EAST: return Direction.DOWN;
			case WEST: return Direction.UP;
			default: return side;
			}
		};
		case WEST: return side -> {
			switch (side) {
			case UP: return Direction.NORTH;
			case DOWN: return Direction.SOUTH;
			case NORTH: return Direction.EAST;
			case SOUTH: return Direction.WEST;
			case EAST: return Direction.UP;
			case WEST: return Direction.DOWN;
			default: return side;
			}
		};
		default: return Function.identity();
		}
	}
}