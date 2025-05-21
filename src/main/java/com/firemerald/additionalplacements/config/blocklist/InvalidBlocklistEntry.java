package com.firemerald.additionalplacements.config.blocklist;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public class InvalidBlocklistEntry implements IBlocklistEntry {
    public final String key;

    public InvalidBlocklistEntry(String key) {
        this.key = key;
    }

    @Override
    public BlocklistResult apply(Block block, ResourceLocation id) {
        return BlocklistResult.DEFAULT;
    }

    @Override
    public String toString() {
        return key;
    }
}
