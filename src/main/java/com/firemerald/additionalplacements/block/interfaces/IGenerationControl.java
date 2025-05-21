package com.firemerald.additionalplacements.block.interfaces;

/**
 * A mod author may choose to add the method below (<b>with or without implementing this interface</b>) on a custom block class to further control whether or not an additional placement block can be generated for it.</br>
 * If looking to blacklist blocks outside of config without using a custom class, see {@link com.firemerald.additionalplacements.generation.IBlockBlacklister IBlockBlacklister} instead.
 */
public interface IGenerationControl {
	/**
	 * @return whether to allow the generation of an additional states block for this block. Does not guarantee the creation of one!
	 */
    boolean generateAdditionalStates();
}
