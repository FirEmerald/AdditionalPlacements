package com.firemerald.additionalplacements.mixin;

import java.util.Optional;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.firemerald.additionalplacements.block.interfaces.IStateFixer;
import com.firemerald.additionalplacements.config.APConfigs;
import com.firemerald.additionalplacements.util.NBTUtils;
import com.mojang.serialization.Codec;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.storage.ChunkStorage;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.registries.ForgeRegistries;

@Mixin(ChunkStorage.class)
public class MixinChunkStorage {
	@Inject(method = "upgradeChunkTag(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Supplier;Lnet/minecraft/nbt/CompoundTag;Ljava/util/Optional;)Lnet/minecraft/nbt/CompoundTag;", at = @At("RETURN"))
	public void upgradeChunkTag(ResourceKey<Level> pLevelKey, Supplier<DimensionDataStorage> pStorage, CompoundTag pChunkData, Optional<ResourceKey<Codec<? extends ChunkGenerator>>> pChunkGeneratorKey, CallbackInfoReturnable<CompoundTag> cli) {
		//AdditionalPlacementsMod.LOGGER.info(NBTUtils.toJson(pChunkData).toString());
		if (APConfigs.common().fixStates.get()) {
			CompoundTag chunkData = cli.getReturnValue();
			if (chunkData != null) {
				NBTUtils.ifListNotEmpty(chunkData, "sections", Tag.TAG_COMPOUND, sections -> sections.forEach(section -> NBTUtils.ifCompoundNotEmpty((CompoundTag) section, "block_states", blockStates -> NBTUtils.ifListNotEmpty(blockStates, "palette", Tag.TAG_COMPOUND, palette -> palette.forEach(blockTag -> {
					CompoundTag block = (CompoundTag) blockTag;
					if (block.contains("Name", Tag.TAG_STRING)) {
						String name = block.getString("Name");
						if (ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name)) instanceof IStateFixer fixer) {
							CompoundTag original = block.getCompound("Properties");
							CompoundTag fixed = fixer.fix(original, newBlock -> block.put("Name", StringTag.valueOf(ForgeRegistries.BLOCKS.getKey(newBlock).toString())));
							if (original != fixed) {
								if (fixed == null) block.remove("Properties");
								else block.put("Properties", fixed);
							}
						}
					}
				})))));
			}
		}
	}
}
