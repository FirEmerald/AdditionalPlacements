package com.firemerald.additionalplacements.mixin;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.firemerald.additionalplacements.block.interfaces.IStateFixer;
import com.firemerald.additionalplacements.config.APConfigs;
import com.firemerald.additionalplacements.util.NBTUtils;
import com.firemerald.additionalplacements.util.TagIds;

import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.ChunkLoader;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraftforge.registries.ForgeRegistries;

@Mixin(ChunkLoader.class)
public class MixinChunkLoader {
	@Inject(method = "upgradeChunkTag(Lnet/minecraft/util/RegistryKey;Ljava/util/function/Supplier;Lnet/minecraft/nbt/CompoundNBT;)Lnet/minecraft/nbt/CompoundNBT;", at = @At("RETURN"))
	public void upgradeChunkTag(RegistryKey<World> pLevelKey, Supplier<DimensionSavedDataManager> pStorage, CompoundNBT pChunkData, CallbackInfoReturnable<CompoundNBT> cli) {
		//AdditionalPlacementsMod.LOGGER.info(NBTUtils.toJson(pChunkData).toString());
		if (APConfigs.common().fixStates.get()) {
			CompoundNBT chunkData = cli.getReturnValue();
			if (chunkData != null) {
				NBTUtils.ifCompoundNotEmpty(chunkData, "Level", level -> NBTUtils.ifListNotEmpty(level, "Sections", TagIds.TAG_COMPOUND, sections -> sections.forEach(section -> NBTUtils.ifListNotEmpty((CompoundNBT) section, "Palette", TagIds.TAG_COMPOUND, palette -> palette.forEach(blockTag -> {
					CompoundNBT block = (CompoundNBT) blockTag;
					if (block.contains("Name", TagIds.TAG_STRING)) {
						String name = block.getString("Name");
						Block theBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name));
						if (theBlock instanceof IStateFixer) {
							IStateFixer fixer = (IStateFixer) theBlock;
							CompoundNBT original = block.getCompound("Properties");
							CompoundNBT fixed = fixer.fix(original, newBlock -> block.put("Name", StringNBT.valueOf(ForgeRegistries.BLOCKS.getKey(newBlock).toString())));
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
