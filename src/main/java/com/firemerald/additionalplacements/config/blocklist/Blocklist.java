package com.firemerald.additionalplacements.config.blocklist;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.generation.CreatedBlockEntry;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Blocklist {
    public final boolean allowTags;
    public final boolean defValue;
    protected final List<String> defaultBlocklistConfig;
    protected ModConfigSpec.ConfigValue<List<? extends String>> blocklistConfig;
    private IBlocklistEntry[] blocklist;

    public Blocklist(boolean allowTags, boolean defValue, IBlocklistEntry... entries) {
        this.allowTags = allowTags;
        this.defValue = defValue;
        blocklist = new IBlocklistEntry[entries.length + 1];
        defaultBlocklistConfig = new ArrayList<>(blocklist.length);
        String serialized = (blocklist[0] = GlobalBlocklistEntry.of(defValue)).toString();
        defaultBlocklistConfig.add(serialized);
        int i = 0, j = 1;
        while (i < entries.length) {
            defaultBlocklistConfig.add((blocklist[j] = entries[i]).toString());
            i = j;
            j++;
        }
    }

    public void addToConfig(ModConfigSpec.Builder builder, String key, String comment) {
        blocklistConfig = builder
                .comment(comment + "\nSee https://github.com/FirEmerald/AdditionalPlacements/wiki/Blocklist-Format for blocklist format")
                .defineListAllowEmpty(Collections.singletonList(key), () -> defaultBlocklistConfig, o -> o instanceof String);
    }

    public void loadListsFromConfig() {
        List<? extends String> loaded = blocklistConfig.get();
        blocklist = new IBlocklistEntry[loaded.size()];
        for (int i = 0; i < blocklist.length; ++i) blocklist[i] = parse(loaded.get(i));
    }

    public IBlocklistEntry parse(String key) {
        if (key.length() < 2) { //minimum two chars for +* or -*
            AdditionalPlacementsMod.LOGGER.warn("Invalid blocklist key {}: too short - must be at least two characters", key);
            return new InvalidBlocklistEntry(key);
        } else {
            char c = key.charAt(0);
            boolean value;
            if (c == '+') value = true;
            else if (c == '-') value = false;
            else {
                AdditionalPlacementsMod.LOGGER.warn("Invalid blocklist key {}: must start with + or -", key);
                return new InvalidBlocklistEntry(key);
            }
            c = key.charAt(1);
            if (c == '*') {
                if (key.length() > 2) {
                    AdditionalPlacementsMod.LOGGER.warn("Invalid blocklist key {}: global entry must be either +* or -* ONLY", key);
                    return new InvalidBlocklistEntry(key);
                } else return GlobalBlocklistEntry.of(value);
            } else if (c == '#') {
                if (!allowTags) {
                    AdditionalPlacementsMod.LOGGER.warn("Invalid blocklist key {}: tag entries are not allowed in this blocklist", key);
                    return new InvalidBlocklistEntry(key);
                }
                try {
                    ResourceLocation tag = new ResourceLocation(key.substring(2));
                    return new TagBlocklistEntry(value, TagKey.create(Registries.BLOCK, tag));
                } catch (ResourceLocationException e) {
                    AdditionalPlacementsMod.LOGGER.warn("Invalid blocklist key {}: invalid tag", key, e);
                    return new InvalidBlocklistEntry(key);
                }
            } else {
                int point = key.indexOf(':');
                if (point > 0 && point == key.length() - 2) { //$$$:$
                    c = key.charAt(point + 1);
                    if (c == '*') {
                        String mod = key.substring(1, point);
                        if (!isValidModId(mod)) {
                            AdditionalPlacementsMod.LOGGER.warn("Invalid blocklist key {}: invalid mod id {}", key, mod);
                            return new InvalidBlocklistEntry(key);
                        } else return new ModBlocklistEntry(value, mod);
                    }
                }
                try {
                    ResourceLocation id = new ResourceLocation(key.substring(1));
                    return new IDBlocklistEntry(value, id);
                } catch (ResourceLocationException e) {
                    AdditionalPlacementsMod.LOGGER.warn("Invalid blocklist key {}: invalid id", key, e);
                    return new InvalidBlocklistEntry(key);
                }
            }
        }
    }

    private static boolean isValidModId(String namespace) {
        for (int i = 0; i < namespace.length(); ++i) if (!isValidModIdChar(namespace.charAt(i))) return false;
        return true;
    }

    private static boolean isValidModIdChar(char c) {
        return c == '_' || c == '-' || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '.';
    }

    public boolean test(Block block, ResourceLocation id) {
        for (int i = blocklist.length - 1; i >= 0; --i) {
            BlocklistResult res = blocklist[i].apply(block, id);
            if (res == BlocklistResult.ALLOW) return true;
            else if (res == BlocklistResult.DENY) return false;
        }
        return defValue;
    }

    public boolean testOriginal(CreatedBlockEntry<?, ?> entry) {
        return test(entry.originalBlock(), entry.originalId());
    }

    public boolean testCreated(CreatedBlockEntry<?, ?> entry) {
        return test(entry.newBlock(), entry.newId());
    }
}
