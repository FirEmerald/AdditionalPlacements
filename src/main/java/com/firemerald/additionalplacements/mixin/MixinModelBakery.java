package com.firemerald.additionalplacements.mixin;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.client.models.UnbakedRetexturedPlacementModel;
import com.firemerald.additionalplacements.client.models.UnbakedRotatedPlacementModel;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.client.models.PlacementModelState;
import com.firemerald.additionalplacements.client.models.definitions.StateModelDefinition;
import com.firemerald.additionalplacements.util.BlockRotation;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

@Mixin(ModelBakery.class)
public class MixinModelBakery {
	@Shadow
	@Final
	private BlockColors blockColors;
	@Shadow
	public UnbakedModel getModel(ResourceLocation modelLocation) {
		return null;
	}

	@Inject(method = "<init>(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/client/color/block/BlockColors;Lnet/minecraft/util/profiling/ProfilerFiller;I)V", at = @At("RETURN"))
	public void init(ResourceManager resourceManager, BlockColors blockColors, ProfilerFiller profiler, int maxMipmapLevel, CallbackInfo cli) {
		UnbakedRotatedPlacementModel.clearCache();
		UnbakedRetexturedPlacementModel.clearCache();
	}

	@Redirect(
			method = "loadModel(Lnet/minecraft/resources/ResourceLocation;)V",
			at = @At(value = "INVOKE", target = "org/slf4j/Logger.warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"),
			slice = @Slice(
					from = @At(value = "CONSTANT", args = {"stringValue=Exception loading blockstate definition: {}: {}"}),
					to = @At(value = "INVOKE", target = "org/slf4j/Logger.warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", shift = Shift.AFTER)
					)
			)
	private void loadModel(Logger logger, String message, Object blockStateJsonObj, Object eObj, ResourceLocation blockstateLocation) {
		if (eObj instanceof FileNotFoundException) {
			ResourceLocation blockId = new ResourceLocation(blockstateLocation.getNamespace(), blockstateLocation.getPath());
			Block block = Registry.BLOCK.get(blockId);
			if (block instanceof AdditionalPlacementBlock) return;
		}
		logger.warn(message, blockStateJsonObj, eObj);
	}

	@ModifyVariable(
			method = "method_21604(Ljava/util/Map;Lnet/minecraft/resources/ResourceLocation;Lcom/mojang/datafixers/util/Pair;Ljava/util/Map;Lnet/minecraft/client/resources/model/ModelResourceLocation;Lnet/minecraft/world/level/block/state/BlockState;)V",
			at = @At("STORE"),
			index = 7
			)
	private Pair<UnbakedModel, Supplier<ModelBakery.ModelGroupKey>> loadModelLambda(
			Pair<UnbakedModel, Supplier<ModelBakery.ModelGroupKey>> modelPair,
			Map<ModelResourceLocation, BlockState> modelToState,
			ResourceLocation blockId,
			Pair<UnbakedModel, Supplier<ModelBakery.ModelGroupKey>> missingModelPair,
			Map<ModelBakery.ModelGroupKey, Set<BlockState>> modelGroups,
			ModelResourceLocation currentModelLocation,
			BlockState ourState) {
		if (modelPair == null) { //replace only states which do not already have a model
			if (ourState != null && ourState.getBlock() instanceof AdditionalPlacementBlock<?> block) {
				BlockState theirState = block.getModelState(ourState);
				ModelResourceLocation theirModelLocation = BlockModelShaper.stateToModelLocation(theirState);
				UnbakedModel theirModel = getModel(theirModelLocation);
				if (theirModel != null) {
					UnbakedModel unbakedModel;
					if (block.rotatesModel(ourState)) {
						BlockRotation theirModelRotation = block.getRotation(ourState);
						unbakedModel = UnbakedRotatedPlacementModel.of(theirModelLocation, theirModel, theirModelRotation, block.rotatesTexture(ourState));
					} else {
						StateModelDefinition modelDefinition = block.getModelDefinition(ourState);
						ResourceLocation ourModel = modelDefinition.location(block.getBaseModelPrefix());
						ModelState ourModelRotation = PlacementModelState.by(modelDefinition.xRotation(), modelDefinition.yRotation());
						unbakedModel = UnbakedRetexturedPlacementModel.of(ourModel, ourModelRotation, theirModelLocation, theirModel);
					}
					List<Property<?>> coloringProperties = this.blockColors.getColoringProperties(theirState.getBlock()).stream()
							.filter(block::isValidProperty) //just in case
							.collect(Collectors.toList());
					return Pair.of(unbakedModel, () -> ModelBakery.ModelGroupKey.create(ourState, unbakedModel, coloringProperties));
				} else {
					AdditionalPlacementsMod.LOGGER.warn("Could not generate a model for {} as none exists for {}", ourState, theirState);
					return null;
				}
			} else return null;
		} else return modelPair;
	}
}
