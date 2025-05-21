package com.firemerald.additionalplacements.mixin;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.client.IBlockStateModelLoaderExtension;
import com.firemerald.additionalplacements.client.models.PlacementModelState;
import com.firemerald.additionalplacements.client.models.UnbakedPlacementModel;
import com.firemerald.additionalplacements.client.models.definitions.StateModelDefinition;
import com.firemerald.additionalplacements.util.BlockRotation;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

@Mixin(BlockStateModelLoader.class)
public class MixinBlockStateModelLoader implements IBlockStateModelLoaderExtension {
	private Map<ModelResourceLocation, UnbakedModel> topLevelModels;

	@Override
	public void setTopLevelModels(Map<ModelResourceLocation, UnbakedModel> topLevelModels) {
		this.topLevelModels = topLevelModels;
	}

	@Shadow
	@Final
    private BlockColors blockColors;

	@ModifyVariable(
			method = "method_61066(Ljava/util/Map;Lnet/minecraft/resources/ResourceLocation;Ljava/util/Map;Lnet/minecraft/client/resources/model/ModelResourceLocation;Lnet/minecraft/world/level/block/state/BlockState;)V",
			at = @At("STORE"),
			index = 6,
			remap = false
			)
	private BlockStateModelLoader.LoadedModel loadModelLambda(
			BlockStateModelLoader.LoadedModel loadedModel,
			Map<BlockState, BlockStateModelLoader.LoadedModel> ourLoadedModels,
			ResourceLocation blockId,
			Map<BlockStateModelLoader.ModelGroupKey, Set<BlockState>> modelGroups,
			ModelResourceLocation currentModelLocation,
			BlockState ourState) {
		if (loadedModel == null) { //replace only states which do not already have a model
			if (ourState != null && ourState.getBlock() instanceof AdditionalPlacementBlock<?> block) {

				BlockState theirState = block.getModelState(ourState);
				StateModelDefinition modelDefinition = block.getModelDefinition(ourState);
				ResourceLocation ourModel = modelDefinition.location(block.getBaseModelPrefix());
				ModelState ourModelRotation = PlacementModelState.by(modelDefinition.xRotation(), modelDefinition.yRotation());
				UnbakedModel theirModel = topLevelModels.get(BlockModelShaper.stateToModelLocation(theirState));
				BlockRotation theirModelRotation = block.getRotation(ourState);
				UnbakedPlacementModel unbakedModel = UnbakedPlacementModel.of(block, ourModel, ourModelRotation, theirModel, theirModelRotation);

				List<Property<?>> coloringProperties = this.blockColors.getColoringProperties(theirState.getBlock()).stream()
						.filter(block::isValidProperty) //just in case
						.collect(Collectors.toList());
	            return new BlockStateModelLoader.LoadedModel(unbakedModel, () -> BlockStateModelLoader.ModelGroupKey.create(ourState, unbakedModel, coloringProperties));
			}
		}
		return loadedModel;
	}

	@Inject(method = "loadBlockStateDefinitions(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/block/state/StateDefinition;)V", at = @At("RETURN"))
	private void loadBlockStateDefinitions(CallbackInfo cli) {
		UnbakedPlacementModel.clearCache();
	}
}
