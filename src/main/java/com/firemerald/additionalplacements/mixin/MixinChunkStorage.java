package com.firemerald.additionalplacements.mixin;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.firemerald.additionalplacements.util.StateFixer;
import com.mojang.serialization.Codec;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.storage.ChunkStorage;
import net.minecraft.world.level.storage.DimensionDataStorage;

@Mixin(ChunkStorage.class)
public class MixinChunkStorage {
	
	@Inject(method = "upgradeChunkTag", at = @At("RETURN"), cancellable = true)
	public void upgradeChunkTag(ResourceKey<Level> pLevelKey, Supplier<DimensionDataStorage> pStorage, CompoundTag pChunkData, Optional<ResourceKey<Codec<? extends ChunkGenerator>>> pChunkGeneratorKey, CallbackInfoReturnable<CompoundTag> cli) {
		CompoundTag chunkData = cli.getReturnValue();
		if (chunkData.contains("sections", Tag.TAG_LIST)) {
			ListTag sections = chunkData.getList("sections", Tag.TAG_COMPOUND);
			sections.forEach(sectionTag -> {
				CompoundTag section = (CompoundTag) sectionTag;
				if (section.contains("block_states", Tag.TAG_COMPOUND)) {
					CompoundTag blockStates = section.getCompound("block_states");
					if (blockStates.contains("palette", Tag.TAG_LIST)) {
						ListTag palette = blockStates.getList("palette", Tag.TAG_COMPOUND);
						palette.forEach(blockTag -> {
							CompoundTag block = (CompoundTag) blockTag;
							if (block.contains("Name", Tag.TAG_STRING)) {
								String name = block.getString("Name");
								Block theBlock = BuiltInRegistries.BLOCK.get(new ResourceLocation(name));
								if (theBlock != null) {
									Function<CompoundTag, CompoundTag> fixer = StateFixer.getFixer(theBlock.getClass());
									if (fixer != null) {
										CompoundTag original = block.getCompound("Properties");
										CompoundTag fixed = fixer.apply(original);
										if (original != fixed) {
											if (fixed == null) block.remove("Properties");
											else block.put("Properties", fixed);
										}
									}
								}
							}
						});
					}
				}
			});
		}
		
	}
}
