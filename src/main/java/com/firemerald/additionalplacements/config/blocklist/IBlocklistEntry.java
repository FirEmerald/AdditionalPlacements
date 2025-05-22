package com.firemerald.additionalplacements.config.blocklist;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public interface IBlocklistEntry {
    String toString(); //All entries MUST be serializable to string!

    BlocklistResult apply(Block block, ResourceLocation id);
}
