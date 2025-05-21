package com.firemerald.additionalplacements.block.interfaces;

import java.util.List;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.firemerald.additionalplacements.common.IAPPlayer;
import com.firemerald.additionalplacements.config.APConfigs;
import com.firemerald.additionalplacements.generation.GenerationType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
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

public interface IPlacementBlock<T extends Block> extends ItemLike, IGenerationControl
{
	T getOtherBlock();

	default BlockState rotateImpl(BlockState blockState, Rotation rotation)
	{
		return transform(blockState, rotation::rotate);
	}

	default BlockState mirrorImpl(BlockState blockState, Mirror mirror)
	{
		return transform(blockState, mirror::mirror);
	}

	BlockState transform(BlockState blockState, Function<Direction, Direction> transform);

	BlockState getStateForPlacementImpl(BlockPlaceContext context, BlockState currentState);

	BlockState updateShapeImpl(BlockState state, Direction direction, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos);

	default void appendHoverTextImpl(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag)
	{
		if (APConfigs.common().showTooltip.get() && getGenerationType().placementEnabled()) addPlacementTooltip(stack, level, tooltip, flag);
	}

	void addPlacementTooltip(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag);

	boolean hasAdditionalStates();

	BlockState getDefaultAdditionalState(BlockState currentState);

	BlockState getDefaultVanillaState(BlockState currentState);

	boolean isThis(BlockState blockState);

	Quaternion[] DIRECTION_TRANSFORMS = new Quaternion[] {
		Quaternion.fromXYZDegrees(new Vector3f(90, 0, 0)), //DOWN
		Quaternion.fromXYZDegrees(new Vector3f(-90, 0, 0)), //UP
		Quaternion.fromXYZDegrees(new Vector3f(0, 180, 0)), //NORTH
		Quaternion.ONE, //SOUTH
		Quaternion.fromXYZDegrees(new Vector3f(0, -90, 0)), //WEST
		Quaternion.fromXYZDegrees(new Vector3f(0, 90, 0)), //EAST
	};

	@OnlyIn(Dist.CLIENT)
    default void renderHighlight(PoseStack pose, VertexConsumer vertexConsumer, Player player, BlockHitResult result, Camera camera, float partial)
	{
		BlockPos hit = result.getBlockPos();
		if (enablePlacement(hit, player.getLevel(), result.getDirection(), player)) {
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
			float[] previewColor = APConfigs.client().previewColor();
			if (previewColor[3] > 0) renderPlacementPreview(pose, vertexConsumer, player, result, partial, previewColor[0], previewColor[1], previewColor[2], previewColor[3]);
			pose.mulPose(DIRECTION_TRANSFORMS[result.getDirection().ordinal()]);
			float[] gridColor = APConfigs.client().gridColor();
			if (gridColor[3] > 0) renderPlacementHighlight(pose, vertexConsumer, player, result, partial, gridColor[0], gridColor[1], gridColor[2], gridColor[3]);
			pose.popPose();
		}
	}

	@OnlyIn(Dist.CLIENT)
    default void renderPlacementPreview(PoseStack pose, VertexConsumer vertexConsumer, Player player, BlockHitResult result, float partial, float r, float g, float b, float a) {}

	@OnlyIn(Dist.CLIENT)
    void renderPlacementHighlight(PoseStack pose, VertexConsumer vertexConsumer, Player player, BlockHitResult result, float partial, float r, float g, float b, float a);

	default boolean enablePlacement(@Nullable Player player) {
		return getGenerationType().placementEnabled() && (!(player instanceof IAPPlayer) || ((IAPPlayer) player).isPlacementEnabled());
	}

	default boolean enablePlacement(BlockPos hit, Level level, Direction direction, Player player) {
		return enablePlacement(player);
	}

	GenerationType<?, ?> getGenerationType();

	@Override
    default boolean generateAdditionalStates() {
		return true;
	}

	default boolean canGenerateAdditionalStates() {
		return generateAdditionalStates() && !hasAdditionalStates();
	}
}