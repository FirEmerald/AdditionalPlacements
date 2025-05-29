package com.firemerald.additionalplacements.mixin;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.client.models.PlacementModelState;
import com.firemerald.additionalplacements.client.models.UnbakedRetexturedPlacementModel;
import com.firemerald.additionalplacements.client.models.UnbakedRotatedPlacementModel;
import com.firemerald.additionalplacements.client.models.definitions.StateModelDefinition;
import com.firemerald.additionalplacements.generation.Registration;
import com.firemerald.additionalplacements.util.BlockRotation;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.UnbakedBlockStateModel;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(BlockStateModelLoader.class)
public class MixinBlockStateModelLoader {
    @Inject(method = "loadBlockStates(Lnet/minecraft/client/resources/model/UnbakedModel;Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;", at = @At("RETURN"), cancellable = true)
    private static void postLoadModels(UnbakedModel model, ResourceManager resourceManager, Executor executor, CallbackInfoReturnable<CompletableFuture<BlockStateModelLoader.LoadedModels>> cir) {
        cir.setReturnValue(cir.getReturnValue().thenApply(loadedModels -> {
            Map<ModelResourceLocation, BlockStateModelLoader.LoadedModel> models = loadedModels.models();
            Registration.forEachCreated(entry -> {
                AdditionalPlacementBlock<?> block = entry.newBlock();
                block.getStateDefinition().getPossibleStates().forEach(ourState -> {
                    ModelResourceLocation ourModelLocation = BlockModelShaper.stateToModelLocation(ourState);
                    models.computeIfAbsent(ourModelLocation, unused -> {
                        BlockState theirState = block.getModelState(ourState);
                        ModelResourceLocation theirModelLocation = BlockModelShaper.stateToModelLocation(theirState);
                        if (models.containsKey(theirModelLocation)) {
                            UnbakedBlockStateModel theirModel = models.get(theirModelLocation).model();
                            UnbakedBlockStateModel unbakedModel;
                            if (block.rotatesModel(ourState)) {
                                BlockRotation theirModelRotation = block.getRotation(ourState);
                                unbakedModel = UnbakedRotatedPlacementModel.of(theirModel, theirModelRotation, block.rotatesTexture(ourState));
                            } else {
                                StateModelDefinition modelDefinition = block.getModelDefinition(ourState);
                                ResourceLocation ourModel = modelDefinition.location(block.getBaseModelPrefix());
                                ModelState ourModelRotation = PlacementModelState.by(modelDefinition.xRotation().value, modelDefinition.yRotation().value);
                                unbakedModel = UnbakedRetexturedPlacementModel.of(ourModel, ourModelRotation, theirModel);
                            }
                            return new BlockStateModelLoader.LoadedModel(ourState, unbakedModel);
                        } else {
                            AdditionalPlacementsMod.LOGGER.warn("Could not generate a model for {} as none exists for {}", ourState, theirState);
                            return null;
                        }
                    });
                });
            });
            UnbakedRotatedPlacementModel.clearCache();
            UnbakedRetexturedPlacementModel.clearCache();
            return loadedModels;
        }));
    }
}
