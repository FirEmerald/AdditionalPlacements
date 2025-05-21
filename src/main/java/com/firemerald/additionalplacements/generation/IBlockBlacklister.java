package com.firemerald.additionalplacements.generation;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

/**
 * A mod author may register an instance of this in a {@link RegistrationInitializer} to exercise more control over whether a block can generate additional placement states.</br>
 * It's recommended to use {@link com.firemerald.additionalplacements.block.interfaces.IGenerationControl IGenerationControl} instead if possible, for performance reasons.
 * @param T the block type this blacklist operates on.
 */
@FunctionalInterface
public interface IBlockBlacklister<T extends Block> {
	/**
	 * @param originalBlock the base block being checked
	 * @param originalId the ID of the block being checked
	 * @return if an additional placement block should not be generated for this block
	 */
	boolean blacklist(T originalBlock, ResourceLocation originalId);
}
