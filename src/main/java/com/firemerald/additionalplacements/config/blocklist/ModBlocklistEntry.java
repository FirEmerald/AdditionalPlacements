package com.firemerald.additionalplacements.config.blocklist;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

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
