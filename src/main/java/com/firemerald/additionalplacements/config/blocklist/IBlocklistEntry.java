package com.firemerald.additionalplacements.config.blocklist;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public interface IBlocklistEntry {
    String toString(); //All entries MUST be serializable to string!

    BlocklistResult apply(Block block, ResourceLocation id);
}
