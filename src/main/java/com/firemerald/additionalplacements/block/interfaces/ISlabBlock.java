package com.firemerald.additionalplacements.block.interfaces;

import java.util.List;

import javax.annotation.Nullable;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.VerticalSlabBlock;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ISlabBlock<T extends Block> extends IPlacementBlock<T>
{
	public static interface IVanillaSlabBlock extends ISlabBlock<VerticalSlabBlock>, IVanillaBlock<VerticalSlabBlock> {}

	@Override
	public default BlockState rotateImpl(BlockState blockState, Rotation rotation)
	{
		Direction placing = getPlacing(blockState);
		return placing == null ? blockState : forPlacing(rotation.rotate(placing), blockState);
	}

	@Override
	public default BlockState mirrorImpl(BlockState blockState, Mirror mirror)
	{
		Direction placing = getPlacing(blockState);
		return placing == null ? blockState : forPlacing(mirror.mirror(placing), blockState);
	}

	public default boolean canBeReplacedImpl(BlockState state, BlockItemUseContext context)
	{
		ItemStack itemstack = context.getItemInHand();
		if (itemstack.getItem() == this.asItem())
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
	public default BlockState getStateForPlacementImpl(BlockItemUseContext context, BlockState currentState)
	{
		BlockPos blockPos = context.getClickedPos();
		BlockState blockState = context.getLevel().getBlockState(blockPos);
        if (isThis(blockState)) return getDefaultVanillaState(blockState).setValue(SlabBlock.TYPE, SlabType.DOUBLE);
        else return forPlacing(getPlacingDirection(context), currentState);
	}

	public default BlockState forPlacing(Direction dir, BlockState blockState)
	{
    	if (dir == Direction.UP) return getDefaultVanillaState(blockState).setValue(SlabBlock.TYPE, SlabType.TOP);
    	else if (dir == Direction.DOWN) return getDefaultVanillaState(blockState).setValue(SlabBlock.TYPE, SlabType.BOTTOM);
    	else return getDefaultAdditionalState(blockState).setValue(VerticalSlabBlock.PLACING, dir);
	}

	@Nullable
	public default Direction getPlacing(BlockState blockState)
	{
		if (blockState.getBlock() instanceof SlabBlock)
		{
			SlabType type = blockState.getValue(SlabBlock.TYPE);
			return type == SlabType.BOTTOM ? Direction.DOWN : type == SlabType.TOP ? Direction.UP : null;
		}
		else return blockState.getValue(VerticalSlabBlock.PLACING);
	}

	public default Direction getPlacingDirection(BlockItemUseContext context)
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
	public default void renderPlacementHighlight(MatrixStack pose, IVertexBuilder vertexConsumer, PlayerEntity player, BlockRayTraceResult result, float partial)
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
	public default boolean disablePlacement()
	{
		return this instanceof SlabBlock && AdditionalPlacementsMod.COMMON_CONFIG.disableAutomaticSlabPlacement.get();
	}

    @Override
	public default void addPlacementTooltip(ItemStack stack, @Nullable IBlockReader level, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent("tooltip.additionalplacements.vertical_placement"));
	}
}