package com.firemerald.additionalplacements.config.blocklist;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public record InvalidBlocklistEntry(String key) implements IBlocklistEntry {

    @Override
    public BlocklistResult apply(Block block, ResourceLocation id) {
        return BlocklistResult.DEFAULT;
    }

    @Override
    public String toString() {
        return key;
    }
}
