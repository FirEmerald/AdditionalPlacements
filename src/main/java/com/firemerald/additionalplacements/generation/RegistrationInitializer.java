package com.firemerald.additionalplacements.generation;

import java.util.function.Consumer;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;

import net.minecraft.world.level.block.Block;

/**
 * A modder can use this to register new {@link GenerationType generation types} and/or {@link IBlockBlacklister block blacklisters} by adding an instance of this to the {@code additional-placements-generators} entrypoint in their {@code fabric.mod.json}.
 */
public interface RegistrationInitializer {
	default void onInitializeRegistration(IRegistration register) {}

	default void addGlobalBlacklisters(Consumer<IBlockBlacklister<Block>> register) {}

	default <T extends Block, U extends AdditionalPlacementBlock<T>> void addBlacklisters(Class<T> type, GenerationType<T, U> generationType, Consumer<IBlockBlacklister<? super T>> register) {}
}
