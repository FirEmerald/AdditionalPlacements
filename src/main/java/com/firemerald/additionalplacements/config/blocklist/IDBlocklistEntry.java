package com.firemerald.additionalplacements.config.blocklist;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public class IDBlocklistEntry extends BlocklistEntryBase {
    public final ResourceLocation id;

    public IDBlocklistEntry(boolean value, ResourceLocation id) {
        super(value);
        this.id = id;
    }

    @Override
    public boolean contains(Block block, ResourceLocation id) {
        return id.equals(this.id);
    }

    @Override
    public String filterString() {
        return id.toString();
    }
}
