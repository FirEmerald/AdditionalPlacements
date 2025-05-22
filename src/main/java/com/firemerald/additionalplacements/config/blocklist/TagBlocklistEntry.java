package com.firemerald.additionalplacements.config.blocklist;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class TagBlocklistEntry extends BlocklistEntryBase {
    public final TagKey<Block> tag;

    public TagBlocklistEntry(boolean value, TagKey<Block> tag) {
        super(value);
        this.tag = tag;
    }

    @Override
    public boolean contains(Block block, ResourceLocation id) {
        return block.defaultBlockState().is(tag);
    }

    @Override
    public String filterString() {
        return "#" + tag.toString();
    }
}
