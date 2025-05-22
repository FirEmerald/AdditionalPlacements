package com.firemerald.additionalplacements.config.blocklist;

import net.minecraft.block.Block;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

public class TagBlocklistEntry extends BlocklistEntryBase {
    public final ITag<Block> tag;

    public TagBlocklistEntry(boolean value, ITag<Block> tag) {
        super(value);
        this.tag = tag;
    }

    @Override
    public boolean contains(Block block, ResourceLocation id) {
        return tag.contains(block);
    }

    @Override
    public String filterString() {
        return "#" + tag.toString();
    }
}
