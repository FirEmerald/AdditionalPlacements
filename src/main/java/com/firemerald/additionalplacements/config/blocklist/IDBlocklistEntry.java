package com.firemerald.additionalplacements.config.blocklist;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

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
