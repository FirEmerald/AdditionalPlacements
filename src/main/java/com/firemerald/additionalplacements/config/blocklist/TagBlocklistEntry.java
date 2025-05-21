package com.firemerald.additionalplacements.config.blocklist;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public class TagBlocklistEntry extends BlocklistEntryBase {
    public final ResourceLocation tag;

    public TagBlocklistEntry(boolean value, ResourceLocation tag) {
        super(value);
        this.tag = tag;
    }

    @Override
    public boolean contains(Block block, ResourceLocation id) {
        return block.getTags().contains(tag);
    }

    @Override
    public String filterString() {
        return "#" + tag.toString();
    }
}
