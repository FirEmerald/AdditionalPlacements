package com.firemerald.vsas.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

@FunctionalInterface
public interface IBlockHighlight
{
	public static Quaternion[] DIRECTION_TRANSFORMS = new Quaternion[] {
		Quaternion.fromXYZDegrees(new Vector3f(90, 0, 0)), //DOWN
		Quaternion.fromXYZDegrees(new Vector3f(-90, 0, 0)), //UP
		Quaternion.fromXYZDegrees(new Vector3f(0, 180, 0)), //NORTH
		Quaternion.ONE, //SOUTH
		Quaternion.fromXYZDegrees(new Vector3f(0, -90, 0)), //WEST
		Quaternion.fromXYZDegrees(new Vector3f(0, 90, 0)), //EAST
	};

	public static final IBlockHighlight
	SLAB = (pose, vertexConsumer, player, result, partial) -> {
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
	},
	STAIRS = (pose, vertexConsumer, player, result, partial) -> {
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
		vertexConsumer.vertex(poseMat, .25f, .25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.25f, .25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .25f, -.25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.5f, -.25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.25f, -.25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.25f, -.25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.25f, -.5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .5f, -.25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .25f, -.25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .25f, -.25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .25f, -.5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.5f, .25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.25f, .25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.25f, .25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, -.25f, .5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .5f, .25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .25f, .25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .25f, .25f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
		vertexConsumer.vertex(poseMat, .25f, .5f, -.5f).color(0, 0, 0, 0.4f).normal(normMat, 0, 0, 1).endVertex();
	};

	public static void transform(PoseStack pose, Direction sideHit)
	{
		pose.mulPose(DIRECTION_TRANSFORMS[sideHit.ordinal()]);
	}

	public default void render(PoseStack pose, VertexConsumer vertexConsumer, Player player, BlockHitResult result, Camera camera, float partial)
	{
		pose.pushPose();
		BlockPos hit = result.getBlockPos();
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
		transform(pose, result.getDirection());
		draw(pose, vertexConsumer, player, result, partial);
		pose.popPose();
	}

	public void draw(PoseStack pose, VertexConsumer vertexConsumer, Player player, BlockHitResult result, float partial);
}