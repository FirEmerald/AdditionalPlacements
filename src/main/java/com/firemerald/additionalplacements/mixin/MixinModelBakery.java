package com.firemerald.additionalplacements.mixin;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.client.models.PlacementModelState;
import com.firemerald.additionalplacements.client.models.UnbakedPlacementModel;
import com.firemerald.additionalplacements.client.models.definitions.StateModelDefinition;
import com.firemerald.additionalplacements.util.BlockRotation;
import com.mojang.datafixers.util.Pair;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.profiler.IProfiler;
import net.minecraft.state.Property;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

@Mixin(ModelBakery.class)
public class MixinModelBakery {
	@Shadow
	@Final
	private BlockColors blockColors;

	@Shadow
	public IUnbakedModel getModel(ResourceLocation modelLocation) {
		return null;
	}

	@Inject(method = "processLoading(Lnet/minecraft/profiler/IProfiler;I)V", at = @At("RETURN"), remap = false)
	public void processLoading(IProfiler profiler, int maxMipmapLevel, CallbackInfo cli) {
		UnbakedPlacementModel.clearCache();
	}

	@Redirect(
			method = "loadModel(Lnet/minecraft/util/ResourceLocation;)V",
			at = @At(value = "INVOKE", target = "org/apache/logging/log4j/Logger.warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"),
			slice = @Slice(
					from = @At(value = "CONSTANT", args = {"stringValue=Exception loading blockstate definition: {}: {}"}),
					to = @At(value = "INVOKE", target = "org/apache/logging/log4j/Logger.warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", shift = Shift.AFTER)
					)
			)
	private void loadModel(Logger logger, String message, Object blockStateJsonObj, Object eObj, ResourceLocation blockstateLocation) {
		if (eObj instanceof FileNotFoundException) {
			ResourceLocation blockId = new ResourceLocation(blockstateLocation.getNamespace(), blockstateLocation.getPath());
			Block block = ForgeRegistries.BLOCKS.getValue(blockId);
			if (block instanceof AdditionalPlacementBlock) return;
		}
		logger.warn(message, blockStateJsonObj, eObj);
	}

	@ModifyVariable(
			method = {
					"lambda$loadModel$25(Ljava/util/Map;Lnet/minecraft/util/ResourceLocation;Lcom/mojang/datafixers/util/Pair;Ljava/util/HashMap;Lnet/minecraft/client/renderer/model/ModelResourceLocation;Lnet/minecraft/block/BlockState;)V",
					          "m_119331_(Ljava/util/Map;Lnet/minecraft/util/ResourceLocation;Lcom/mojang/datafixers/util/Pair;Ljava/util/HashMap;Lnet/minecraft/client/renderer/model/ModelResourceLocation;Lnet/minecraft/block/BlockState;)V"
			},
			at = @At("STORE"),
			index = 7,
			remap = false
			)
	private Pair<IUnbakedModel, Supplier<ModelBakery.ModelListWrapper>> loadModelLambda(
			Pair<IUnbakedModel, Supplier<ModelBakery.ModelListWrapper>> modelPair,
			Map<ModelResourceLocation, BlockState> modelToState,
			ResourceLocation blockId,
			Pair<IUnbakedModel, Supplier<ModelBakery.ModelListWrapper>> missingModelPair,
			HashMap<ModelBakery.ModelListWrapper, Set<BlockState>> modelGroups,
			ModelResourceLocation currentModelLocation,
			BlockState ourState) {
		if (modelPair == null) { //replace only states which do not already have a model
			if (ourState != null && ourState.getBlock() instanceof AdditionalPlacementBlock) {
				AdditionalPlacementBlock<?> block = (AdditionalPlacementBlock<?>) ourState.getBlock();

				BlockState theirState = block.getModelState(ourState);
				StateModelDefinition modelDefinition = block.getModelDefinition(ourState);
				ResourceLocation ourModel = modelDefinition.location(block.getBaseModelPrefix());
				IModelTransform ourModelRotation = PlacementModelState.by(modelDefinition.xRotation, modelDefinition.yRotation);
				ModelResourceLocation theirModelLocation = BlockModelShapes.stateToModelLocation(theirState);
				IUnbakedModel theirModel = getModel(theirModelLocation);
				BlockRotation theirModelRotation = block.getRotation(ourState);
				UnbakedPlacementModel unbakedModel = UnbakedPlacementModel.of(block, ourModel, ourModelRotation, theirModelLocation, theirModel, theirModelRotation);

				List<Property<?>> coloringProperties = this.blockColors.getColoringProperties(theirState.getBlock()).stream()
						.filter(block::isValidProperty) //just in case
						.collect(Collectors.toList());
	            return Pair.of(unbakedModel, () -> ModelBakery.ModelListWrapper.create(ourState, unbakedModel, coloringProperties));
			}
		}
		return modelPair;
	}
}
