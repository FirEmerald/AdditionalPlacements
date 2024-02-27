package com.firemerald.additionalplacements.block.interfaces;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.common.IAPPlayer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.IModelData;

public interface IPlacementBlock<T extends Block> extends IItemProvider
{
	public T getOtherBlock();

	public default BlockState rotateImpl(BlockState blockState, Rotation rotation)
	{
		return transform(blockState, rotation::rotate);
	}

	public default BlockState mirrorImpl(BlockState blockState, Mirror mirror)
	{
		return transform(blockState, mirror::mirror);
	}

	public BlockState transform(BlockState blockState, Function<Direction, Direction> transform);

	public BlockState getStateForPlacementImpl(BlockItemUseContext context, BlockState currentState);

	public BlockState updateShapeImpl(BlockState state, Direction direction, BlockState otherState, IWorld level, BlockPos pos, BlockPos otherPos);

	@OnlyIn(Dist.CLIENT)
	public default void appendHoverTextImpl(ItemStack stack, @Nullable IBlockReader level, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		if (AdditionalPlacementsMod.COMMON_CONFIG.showTooltip.get() && !disablePlacementInternal()) addPlacementTooltip(stack, level, tooltip, flag);
	}

	@OnlyIn(Dist.CLIENT)
	public void addPlacementTooltip(ItemStack stack, @Nullable IBlockReader level, List<ITextComponent> tooltip, ITooltipFlag flag);

	public boolean hasAdditionalStates();

	public BlockState getDefaultAdditionalState(BlockState currentState);

	public BlockState getDefaultVanillaState(BlockState currentState);

	public boolean isThis(BlockState blockState);

	public static final float SQRT_2_INV = 0.70710678118654752440084436210485f;

	public static Quaternion[] DIRECTION_TRANSFORMS = new Quaternion[] {
			new Quaternion(SQRT_2_INV, 0, 0, SQRT_2_INV), //DOWN
			new Quaternion(-SQRT_2_INV, 0, 0, SQRT_2_INV), //UP
			new Quaternion(0, 1, 0, 0), //NORTH
			new Quaternion(0, 0, 0, 1), //SOUTH
			new Quaternion(0, -SQRT_2_INV, 0, SQRT_2_INV), //WEST
			new Quaternion(0, SQRT_2_INV, 0, SQRT_2_INV), //EAST
	};

	@OnlyIn(Dist.CLIENT)
	public default void renderHighlight(MatrixStack pose, IVertexBuilder vertexConsumer, PlayerEntity player, BlockRayTraceResult result, ActiveRenderInfo camera, float partial)
	{
		BlockPos hit = result.getBlockPos();
		if (disablePlacement(hit, player.level, result.getDirection(), player)) return;
		pose.pushPose();
		double hitX = hit.getX();
		double hitY = hit.getY();
		double hitZ = hit.getZ();
		switch (result.getDirection())
		{
		case WEST:
			hitX = result.getLocation().x - 1.005;
			break;
		case EAST:
			hitX = result.getLocation().x + .005;
			break;
		case DOWN:
			hitY = result.getLocation().y - 1.005;
			break;
		case UP:
			hitY = result.getLocation().y + .005;
			break;
		case NORTH:
			hitZ = result.getLocation().z - 1.005;
			break;
		case SOUTH:
			hitZ = result.getLocation().z + .005;
			break;
		default:
		}
		Vector3d pos = camera.getPosition();
		pose.translate(hitX - pos.x + .5, hitY - pos.y + .5, hitZ - pos.z + .5);
		pose.mulPose(DIRECTION_TRANSFORMS[result.getDirection().ordinal()]);
		renderPlacementHighlight(pose, vertexConsumer, player, result, partial);
		pose.popPose();
	}

	@OnlyIn(Dist.CLIENT)
	public void renderPlacementHighlight(MatrixStack pose, IVertexBuilder vertexConsumer, PlayerEntity player, BlockRayTraceResult result, float partial);

	public abstract boolean disablePlacementInternal();

	public default boolean disablePlacement(@Nullable PlayerEntity player)
	{
		return (player instanceof IAPPlayer && !((IAPPlayer) player).isPlacementEnabled()) || disablePlacementInternal();
	}

	public default boolean disablePlacement(BlockPos pos, World level, Direction direction, @Nullable PlayerEntity player)
	{
		return disablePlacement(player);
	}

	@OnlyIn(Dist.CLIENT)
	public default Function<Direction, Direction> getModelDirectionFunction(BlockState state, Random rand, IModelData extraData)
	{
		return Function.identity();
	}
}