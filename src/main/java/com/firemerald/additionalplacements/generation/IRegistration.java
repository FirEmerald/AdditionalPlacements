package com.firemerald.additionalplacements.generation;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.generation.GenerationType.BuilderBase;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

@FunctionalInterface
public interface IRegistration {
	<T extends Block, U extends AdditionalPlacementBlock<T>, V extends GenerationType<T, U>> V registerType(Class<T> clazz, ResourceLocation name, String description, BuilderBase<T, U, V, ?> builder);
}
