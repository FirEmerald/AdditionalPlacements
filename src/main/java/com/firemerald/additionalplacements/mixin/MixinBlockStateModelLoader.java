package com.firemerald.additionalplacements.mixin;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.client.models.UnbakedRetexturedPlacementModel;
import com.firemerald.additionalplacements.client.models.UnbakedRotatedPlacementModel;
import com.firemerald.additionalplacements.client.models.definitions.StateModelDefinition;
import com.firemerald.additionalplacements.generation.Registration;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.client.resources.model.ModelState;
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
    @Inject(method = "loadBlockStates(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;", at = @At("RETURN"), cancellable = true)
    private static void postLoadModels(ResourceManager resourceManager, Executor executor, CallbackInfoReturnable<CompletableFuture<BlockStateModelLoader.LoadedModels>> cir) {
        cir.setReturnValue(cir.getReturnValue().thenApply(loadedModels -> {
            Map<BlockState, BlockStateModel.UnbakedRoot> models = loadedModels.models();
            Registration.forEachCreated(entry -> {
                AdditionalPlacementBlock<?> block = entry.newBlock();
                block.getStateDefinition().getPossibleStates().forEach(ourState -> {
                    models.computeIfAbsent(ourState, unused -> {
                        BlockState theirState = block.getModelState(ourState);
                        if (models.containsKey(theirState)) {
                            BlockStateModel.UnbakedRoot theirModel = models.get(theirState);
                            if (block.rotatesModel(ourState)) {
                                return UnbakedRotatedPlacementModel.of(theirModel);
                            } else {
                                StateModelDefinition modelDefinition = block.getModelDefinition(ourState);
                                ResourceLocation ourModel = modelDefinition.location(block.getBaseModelPrefix());
                                ModelState ourModelRotation = modelDefinition.getModelState();
                                return UnbakedRetexturedPlacementModel.of(ourModel, ourModelRotation, theirModel);
                            }
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
