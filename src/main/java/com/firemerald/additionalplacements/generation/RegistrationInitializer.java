package com.firemerald.additionalplacements.generation;

import java.util.function.Consumer;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;

import net.minecraft.block.Block;

/**
 * A modder can use this to register new {@link GenerationType generation types} and/or {@link IBlockBlacklister block blacklisters} by registering an instance of this via {@link Registration#addRegistration(RegistrationInitializer)} during their mod construction.</br>
 * Please note is it incorrect to register these at any other point, and doing so may cause an exception to be thrown!
 */

public interface RegistrationInitializer {
	default void onInitializeRegistration(IRegistration register) {}

	default void addGlobalBlacklisters(Consumer<IBlockBlacklister<Block>> register) {}

	default <T extends Block, U extends AdditionalPlacementBlock<T>> void addBlacklisters(Class<T> type, GenerationType<T, U> generationType, Consumer<IBlockBlacklister<? super T>> register) {}
}
