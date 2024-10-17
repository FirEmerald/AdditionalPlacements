package com.firemerald.additionalplacements.block.interfaces;

import java.util.List;
import java.util.function.Function;

import javax.annotation.Nullable;

import org.joml.Quaternionf;

import com.firemerald.additionalplacements.common.IAPPlayer;
import com.firemerald.additionalplacements.config.APConfigs;
import com.firemerald.additionalplacements.generation.GenerationType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;

public interface IPlacementBlock<T extends Block> extends ItemLike
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

	public BlockState getStateForPlacementImpl(BlockPlaceContext context, BlockState currentState);

	public BlockState updateShapeImpl(BlockState state, Direction direction, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos);

	public default void appendHoverTextImpl(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag)
	{
		if (APConfigs.COMMON.showTooltip.get() && getGenerationType().placementEnabled()) addPlacementTooltip(stack, level, tooltip, flag);
	}

	public void addPlacementTooltip(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag);

	public boolean hasAdditionalStates();

	public BlockState getDefaultAdditionalState(BlockState currentState);

	public BlockState getDefaultVanillaState(BlockState currentState);

	public boolean isThis(BlockState blockState);

	public static final float SQRT_2_INV = 0.70710678118654752440084436210485f;

	public static Quaternionf[] DIRECTION_TRANSFORMS = new Quaternionf[] {
		new Quaternionf(SQRT_2_INV, 0, 0, SQRT_2_INV), //DOWN
		new Quaternionf(-SQRT_2_INV, 0, 0, SQRT_2_INV), //UP
		new Quaternionf(0, 1, 0, 0), //NORTH
		new Quaternionf(0, 0, 0, 1), //SOUTH
		new Quaternionf(0, -SQRT_2_INV, 0, SQRT_2_INV), //WEST
		new Quaternionf(0, SQRT_2_INV, 0, SQRT_2_INV), //EAST
	};

	@OnlyIn(Dist.CLIENT)
	public default void renderHighlight(PoseStack pose, VertexConsumer vertexConsumer, Player player, BlockHitResult result, Camera camera, float partial)
	{
		BlockPos hit = result.getBlockPos();
		if (enablePlacement(hit, player.level(), result.getDirection(), player)) {
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
			Vec3 pos = camera.getPosition();
			pose.translate(hitX - pos.x + .5, hitY - pos.y + .5, hitZ - pos.z + .5);
			renderPlacementPreview(pose, vertexConsumer, player, result, partial);
			pose.mulPose(DIRECTION_TRANSFORMS[result.getDirection().ordinal()]);
			renderPlacementHighlight(pose, vertexConsumer, player, result, partial);
			pose.popPose();
		}
	}

	@OnlyIn(Dist.CLIENT)
	public default void renderPlacementPreview(PoseStack pose, VertexConsumer vertexConsumer, Player player, BlockHitResult result, float partial) {}

	@OnlyIn(Dist.CLIENT)
	public void renderPlacementHighlight(PoseStack pose, VertexConsumer vertexConsumer, Player player, BlockHitResult result, float partial);

	public default boolean enablePlacement(@Nullable Player player) {
		return getGenerationType().placementEnabled() && (!(player instanceof IAPPlayer) || ((IAPPlayer) player).isPlacementEnabled());
	}

	public default boolean enablePlacement(BlockPos hit, Level level, Direction direction, Player player) {
		return enablePlacement(player);
	}

	@OnlyIn(Dist.CLIENT)
	public default Function<Direction, Direction> getModelDirectionFunction(BlockState state, RandomSource rand, ModelData extraData)
	{
		return Function.identity();
	}
	
	public GenerationType<?, ?> getGenerationType();
}