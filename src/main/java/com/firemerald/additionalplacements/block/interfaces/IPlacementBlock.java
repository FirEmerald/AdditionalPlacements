package com.firemerald.additionalplacements.block.interfaces;

import java.util.List;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.firemerald.additionalplacements.common.IAPPlayer;
import com.firemerald.additionalplacements.config.APConfigs;
import com.firemerald.additionalplacements.generation.GenerationType;
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

public interface IPlacementBlock<T extends Block> extends IItemProvider, IGenerationControl
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

	BlockState getStateForPlacementImpl(BlockItemUseContext context, BlockState currentState);

	BlockState updateShapeImpl(BlockState state, Direction direction, BlockState otherState, IWorld level, BlockPos pos, BlockPos otherPos);

	@OnlyIn(Dist.CLIENT)
    default void appendHoverTextImpl(ItemStack stack, @Nullable IBlockReader level, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		if (APConfigs.common().showTooltip.get() && getGenerationType().placementEnabled()) addPlacementTooltip(stack, level, tooltip, flag);
	}
	
	@OnlyIn(Dist.CLIENT)
    void addPlacementTooltip(ItemStack stack, @Nullable IBlockReader level, List<ITextComponent> tooltip, ITooltipFlag flag);

	boolean hasAdditionalStates();

	BlockState getDefaultAdditionalState(BlockState currentState);

	BlockState getDefaultVanillaState(BlockState currentState);

	boolean isThis(BlockState blockState);
	
	float SQRT_2_INV = 0.70710678118654752440084436210485f;

	Quaternion[] DIRECTION_TRANSFORMS = new Quaternion[] {
			new Quaternion(SQRT_2_INV, 0, 0, SQRT_2_INV), //DOWN
			new Quaternion(-SQRT_2_INV, 0, 0, SQRT_2_INV), //UP
			new Quaternion(0, 1, 0, 0), //NORTH
			new Quaternion(0, 0, 0, 1), //SOUTH
			new Quaternion(0, -SQRT_2_INV, 0, SQRT_2_INV), //WEST
			new Quaternion(0, SQRT_2_INV, 0, SQRT_2_INV), //EAST
	};

	@OnlyIn(Dist.CLIENT)
    default void renderHighlight(MatrixStack pose, IVertexBuilder vertexConsumer, PlayerEntity player, BlockRayTraceResult result, ActiveRenderInfo camera, float partial)
	{
		BlockPos hit = result.getBlockPos();
		if (enablePlacement(hit, player.level, result.getDirection(), player)) {
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
			float[] previewColor = APConfigs.client().previewColor();
			if (previewColor[3] > 0) renderPlacementPreview(pose, vertexConsumer, player, result, partial, previewColor[0], previewColor[1], previewColor[2], previewColor[3]);
			pose.mulPose(DIRECTION_TRANSFORMS[result.getDirection().ordinal()]);
			float[] gridColor = APConfigs.client().gridColor();
			if (gridColor[3] > 0) renderPlacementHighlight(pose, vertexConsumer, player, result, partial, gridColor[0], gridColor[1], gridColor[2], gridColor[3]);
			pose.popPose();
		}
	}

	@OnlyIn(Dist.CLIENT)
    default void renderPlacementPreview(MatrixStack pose, IVertexBuilder vertexConsumer, PlayerEntity player, BlockRayTraceResult result, float partial, float r, float g, float b, float a) {}

	@OnlyIn(Dist.CLIENT)
    void renderPlacementHighlight(MatrixStack pose, IVertexBuilder vertexConsumer, PlayerEntity player, BlockRayTraceResult result, float partial, float r, float g, float b, float a);

	default boolean enablePlacement(@Nullable PlayerEntity player) {
		return getGenerationType().placementEnabled() && (!(player instanceof IAPPlayer) || ((IAPPlayer) player).isPlacementEnabled());
	}

	default boolean enablePlacement(BlockPos hit, World level, Direction direction, @Nullable PlayerEntity player) {
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