package com.firemerald.additionalplacements.config.blocklist;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public abstract class BlocklistEntryBase implements IBlocklistEntry {
    public final boolean value;

    public BlocklistEntryBase(boolean value) {
        this.value = value;
    }

    @Override
    public BlocklistResult apply(Block block, ResourceLocation id) {
        if (contains(block, id)) {
           return value ? BlocklistResult.ALLOW : BlocklistResult.DENY;
        } else return BlocklistResult.DEFAULT;
    }

    public abstract boolean contains(Block block, ResourceLocation id);

    public abstract String filterString();

    @Override
    public String toString() {
        return (value ? "+" : "-") + filterString();
    }
}
