package com.firemerald.additionalplacements.mixin;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.client.models.PlacementModelState;
import com.firemerald.additionalplacements.client.models.UnbakedPlacementModel;
import com.firemerald.additionalplacements.client.models.definitions.StateModelDefinition;
import com.firemerald.additionalplacements.generation.Registration;
import com.firemerald.additionalplacements.util.BlockRotation;

import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.*;
import net.minecraft.client.resources.model.BlockStateModelLoader.LoadedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(ModelManager.class)
public class MixinModelManager {
	@Inject(method = "loadBlockStates(Lnet/minecraft/client/resources/model/BlockStateModelLoader;Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;", at = @At("RETURN"), cancellable = true)
	private static void postLoadBlockStates(BlockStateModelLoader modelLoader, ResourceManager resourceManager, Executor backgroundExecutor, CallbackInfoReturnable<CompletableFuture<BlockStateModelLoader.LoadedModels>> cir) {
		UnbakedModel missingModel = MissingBlockModel.missingModel();
		cir.setReturnValue(cir.getReturnValue().thenApply(loadedModels -> {
			Map<ModelResourceLocation, LoadedModel> models = loadedModels.models();
			Registration.forEachCreated(entry -> {
				AdditionalPlacementBlock<?> block = entry.newBlock();
				block.getStateDefinition().getPossibleStates().forEach(ourState -> {
					ModelResourceLocation ourModelLocation = BlockModelShaper.stateToModelLocation(ourState);
					models.computeIfAbsent(ourModelLocation, unused -> {
						BlockState theirState = block.getModelState(ourState);
						StateModelDefinition modelDefinition = block.getModelDefinition(ourState);
						ResourceLocation ourModel = modelDefinition.location(block.getBaseModelPrefix());
						ModelState ourModelRotation = PlacementModelState.by(modelDefinition.xRotation(), modelDefinition.yRotation());
						ModelResourceLocation theirModelLocation = BlockModelShaper.stateToModelLocation(theirState);
						UnbakedModel theirModel = models.containsKey(theirModelLocation) ? models.get(theirModelLocation).model() : missingModel;
						BlockRotation theirModelRotation = block.getRotation(ourState);
						return new BlockStateModelLoader.LoadedModel(
								ourState,
								UnbakedPlacementModel.of(block, ourModel, ourModelRotation, theirModel, theirModelRotation)
						);
					});
				});
			});
			UnbakedPlacementModel.clearCache();
			return loadedModels;
		}));
	}
}
