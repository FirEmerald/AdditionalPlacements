package com.firemerald.additionalplacements.config.blocklist;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public class GlobalBlocklistEntry extends BlocklistEntryBase {
    public static final GlobalBlocklistEntry
    ALLOW = new GlobalBlocklistEntry(true),
    DENY = new GlobalBlocklistEntry(false);

    public static GlobalBlocklistEntry of(boolean value) {
        return value ? ALLOW : DENY;
    }

    private GlobalBlocklistEntry(boolean value) {
        super(value);
    }

    @Override
    public boolean contains(Block block, ResourceLocation id) {
        return true;
    }

    @Override
    public String filterString() {
        return "*";
    }
}
