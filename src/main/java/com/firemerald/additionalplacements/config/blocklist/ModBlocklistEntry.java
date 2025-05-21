package com.firemerald.additionalplacements.config.blocklist;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public class ModBlocklistEntry extends BlocklistEntryBase {
    public final String modId;

    public ModBlocklistEntry(boolean value, String modId) {
        super(value);
        this.modId = modId;
    }

    @Override
    public boolean contains(Block block, ResourceLocation id) {
        return id.getNamespace().equals(modId);
    }

    @Override
    public String filterString() {
        return modId + ":*";
    }
}
