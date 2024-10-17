package com.firemerald.additionalplacements.block.interfaces;

import java.util.List;
import java.util.function.Function;

import javax.annotation.Nullable;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import com.firemerald.additionalplacements.block.VerticalSlabBlock;
import com.firemerald.additionalplacements.common.CommonModEventHandler;
import com.firemerald.additionalplacements.generation.APGenerationTypes;
import com.firemerald.additionalplacements.generation.GenerationType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ISlabBlock<T extends Block> extends IPlacementBlock<T>
{
	public static interface IVanillaSlabBlock extends ISlabBlock<VerticalSlabBlock>, IVanillaBlock<VerticalSlabBlock>
	{
		@Override
		public default boolean enablePlacement(BlockPos pos, Level level, Direction direction, @Nullable Player player)
		{
			if (ISlabBlock.super.enablePlacement(pos, level, direction, player)) {
				if (CommonModEventHandler.doubleslabsLoaded)
				{
					//if (DSConfig.COMMON.disableVerticalSlabPlacement.get()) return true;
					BlockState blockState = level.getBlockState(pos);
					if (blockState.getBlock() instanceof SlabBlock) {
						if (
								(blockState.getValue(SlabBlock.TYPE) == SlabType.BOTTOM && direction == Direction.UP) ||
								(blockState.getValue(SlabBlock.TYPE) == SlabType.TOP && direction == Direction.DOWN)
								) return false;
					}
				}
				return true;
			} else return false;
		}

		@Override
		public default Direction getPlacing(BlockState blockState)
		{
			SlabType type = blockState.getValue(SlabBlock.TYPE);
			return type == SlabType.DOUBLE ? null : type == SlabType.TOP ? Direction.UP : Direction.DOWN;
		}
	}

	@Override
	public default BlockState transform(BlockState blockState, Function<Direction, Direction> transform)
	{
		Direction placing = getPlacing(blockState);
		return placing == null ? blockState : forPlacing(transform.apply(placing), blockState);
	}

	public default boolean canBeReplacedImpl(BlockState state, BlockPlaceContext context)
	{
		ItemStack itemstack = context.getItemInHand();
		if (itemstack.is(this.asItem()))
		{
			if (context.replacingClickedOnBlock())
			{
				Direction placing = getPlacing(state);
				if (placing != null)
				{
					Direction direction = getPlacingDirection(context);
					return direction.getAxis() == placing.getAxis() && context.getClickedFace().getOpposite() == placing;
				}
				else return false;
			}
			else return true;
		}
		else return false;
	}

	@Override
	public default BlockState getStateForPlacementImpl(BlockPlaceContext context, BlockState currentState)
	{
		BlockPos blockPos = context.getClickedPos();
		BlockState blockState = context.getLevel().getBlockState(blockPos);
        if (isThis(blockState)) return blockState.setValue(SlabBlock.TYPE, SlabType.DOUBLE);
        else return forPlacing(getPlacingDirection(context), currentState);
	}

	public default BlockState forPlacing(Direction dir, BlockState blockState)
	{
		return (dir.getAxis() == Axis.Y ? 
				getDefaultVanillaState(blockState) : 
				(getDefaultAdditionalState(blockState).setValue(VerticalSlabBlock.AXIS, dir.getAxis())))
				.setValue(SlabBlock.TYPE, dir.getAxisDirection() == AxisDirection.POSITIVE ? SlabType.TOP : SlabType.BOTTOM);
	}

	@Nullable
	public Direction getPlacing(BlockState blockState);

	public default Direction getPlacingDirection(BlockPlaceContext context)
	{
		double hitX = context.getClickLocation().x - context.getClickedPos().getX() - .5;
		double hitY = context.getClickLocation().y - context.getClickedPos().getY() - .5;
		double hitZ = context.getClickLocation().z - context.getClickedPos().getZ() - .5;
        switch (context.getClickedFace())
        {
		case DOWN:
			if (Math.abs(hitX) <= .25f && Math.abs(hitZ) <= .25f) return Direction.UP;
			else if (hitX > hitZ)
			{
				if (hitX > -hitZ) return Direction.EAST;
				else return Direction.NORTH;
			}
			else
			{
				if (hitZ > -hitX) return Direction.SOUTH;
				else return Direction.WEST;
			}
		case UP:
			if (Math.abs(hitX) <= .25f && Math.abs(hitZ) <= .25f) return Direction.DOWN;
			else if (hitX > hitZ)
			{
				if (hitX > -hitZ) return Direction.EAST;
				else return Direction.NORTH;
			}
			else
			{
				if (hitZ > -hitX) return Direction.SOUTH;
				else return Direction.WEST;
			}
		case NORTH:
			if (Math.abs(hitX) <= .25f && Math.abs(hitY) <= .25f) return Direction.SOUTH;
			else if (hitX > hitY)
			{
				if (hitX > -hitY) return Direction.EAST;
				else return Direction.DOWN;
			}
			else
			{
				if (hitY > -hitX) return Direction.UP;
				else return Direction.WEST;
			}
		case SOUTH:
			if (Math.abs(hitX) <= .25f && Math.abs(hitY) <= .25f) return Direction.NORTH;
			else if (hitX > hitY)
			{
				if (hitX > -hitY) return Direction.EAST;
				else return Direction.DOWN;
			}
			else
			{
				if (hitY > -hitX) return Direction.UP;
				else return Direction.WEST;
			}
		case WEST:
			if (Math.abs(hitY) <= .25f && Math.abs(hitZ) <= .25f) return Direction.EAST;
			else if (hitZ > hitY)
			{
				if (hitZ > -hitY) return Direction.SOUTH;
				else return Direction.DOWN;
			}
			else
			{
				if (hitY > -hitZ) return Direction.UP;
				else return Direction.NORTH;
			}
		case EAST:
			if (Math.abs(hitY) <= .25f && Math.abs(hitZ) <= .25f) return Direction.WEST;
			else if (hitZ > hitY)
			{
				if (hitZ > -hitY) return Direction.SOUTH;
				else return Direction.DOWN;
			}
			else
			{
				if (hitY > -hitZ) return Direction.UP;
				else return Direction.NORTH;
			}
		default:
			return null;
        }
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public default void renderPlacementHighlight(PoseStack pose, VertexConsumer vertexConsumer, Player player, BlockHitResult result, float partial)
	{
		Matrix4f poseMat = pose.last().pose();
		Matrix3f normMat = pose.last().normal();
		vertexConsumer.vertex(poseMat, -.5f, -.5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .5f, -.5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .5f, -.5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .5f, .5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .5f, .5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.5f, .5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.5f, .5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.5f, -.5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.25f, -.25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .25f, -.25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .25f, -.25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .25f, .25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .25f, .25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.25f, .25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.25f, .25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.25f, -.25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.5f, -.5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.25f, -.25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .5f, -.5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .25f, -.25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .5f, .5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .25f, .25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.5f, .5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.25f, .25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
	}
    
	@Override
	public default GenerationType<?, ?> getGenerationType() {
		return APGenerationTypes.SLAB;
	}

    @Override
	public default void addPlacementTooltip(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(Component.translatable("tooltip.additionalplacements.vertical_placement"));
	}
}