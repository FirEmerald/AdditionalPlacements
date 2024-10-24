package com.firemerald.additionalplacements.config;

import java.util.*;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class BlockBlacklist extends GenerationBlacklist {
	public abstract static class BuilderBase<T extends BuilderBase<T>> extends GenerationBlacklist.BuilderBase<T> {
		protected List<String> 
		tagBlacklist = Collections.emptyList(), 
		tagWhitelist = Collections.emptyList();
		
		public T emptyTagBlacklist() {
			return tagBlacklist(Collections.emptyList());
		}
		
		public T tagBlacklist(List<String> entries) {
			tagBlacklist = entries;
			return me();
		}
		
		public T tagBlacklist(Collection<String> entries) {
			return tagBlacklist(new ArrayList<>(entries));
		}
		
		public T tagBlacklist(String... entries) {
			return tagBlacklist(Arrays.asList(entries));
		}
		
		
		public T emptyTagWhitelist() {
			return tagWhitelist(Collections.emptyList());
		}
		
		public T tagWhitelist(List<String> entries) {
			tagWhitelist = entries;
			return me();
		}
		
		public T tagWhitelist(Collection<String> entries) {
			return tagWhitelist(new ArrayList<>(entries));
		}
		
		public T tagWhitelist(String... entries) {
			return tagWhitelist(Arrays.asList(entries));
		}
	}
	
	public static class Builder extends BuilderBase<Builder> {
		public BlockBlacklist build() {
			return new BlockBlacklist(defaultState, modBlacklist, modWhitelist, tagBlacklist, tagWhitelist, blockBlacklist, blockWhitelist);
		}
	}
	
	private final List<? extends String> 
	defaultTagBlacklist, 
	defaultTagWhitelist;
	private ConfigValue<List<? extends String>>
	tagBlacklistConfig,
	tagWhitelistConfig;
	public Collection<String> 
	tagBlacklist = Collections.emptyList(),
	tagWhitelist = Collections.emptyList();
	
	public BlockBlacklist(boolean defaultState, List<? extends String> modBlacklist, List<? extends String> modWhitelist, List<? extends String> tagBlacklist, List<? extends String> tagWhitelist, List<? extends String> blockBlacklist, List<? extends String> blockWhitelist) {
		super(defaultState, modBlacklist, modWhitelist, blockBlacklist, blockWhitelist);
		defaultTagBlacklist = tagBlacklist;
		defaultTagWhitelist = tagWhitelist;
	}
	
	public void addToConfig(ForgeConfigSpec.Builder builder) {
		defaultState = builder
				.comment("Default state for blocks not matching any list. True is enabled, false is disabled.")
				.define("default", defaultDefaultState);
		modWhitelistConfig = builder
				.comment("Whitelist for mods whose blocks will be enabled.")
				.defineListAllowEmpty(Collections.singletonList("mod_whitelist"), () -> defaultModWhitelist, o -> {
					return o instanceof String;
				});
		modBlacklistConfig = builder
				.comment("Blacklist for mods whose blocks will be disabled. Takes priority over the mod whitelist.")
				.defineListAllowEmpty(Collections.singletonList("mod_blacklist"), () -> defaultModBlacklist, o -> {
					return o instanceof String;
				});
		tagWhitelistConfig = builder
				.comment("Whitelist for tags whose blocks will be enabled. Takes priority over the the mod blacklist and whitelist.")
				.defineListAllowEmpty(Collections.singletonList("tag_whitelist"), () -> defaultTagBlacklist, o -> {
					return o instanceof String;
				});
		tagBlacklistConfig = builder
				.comment("Blacklist for tags whose blocks will be disabled. Takes priority over the block whitelist and the mod blacklist and whitelist.")
				.defineListAllowEmpty(Collections.singletonList("tag_blacklist"), () -> defaultTagWhitelist, o -> {
					return o instanceof String;
				});
		blockWhitelistConfig = builder
				.comment("Whitelist for blocks that will be enabled. Takes priority over the the mod and tag blacklist and whitelist.")
				.defineListAllowEmpty(Collections.singletonList("block_whitelist"), () -> defaultBlockWhitelist, o -> {
					return o instanceof String;
				});
		blockBlacklistConfig = builder
				.comment("Blacklist for blocks that will be disabled. Takes priority over the block whitelist and the mod and tag blacklist and whitelist.")
				.defineListAllowEmpty(Collections.singletonList("block_blacklist"), () -> defaultBlockBlacklist, o -> {
					return o instanceof String;
				});
	}
	
	public void loadListsFromConfig() {
		super.loadListsFromConfig();
		tagBlacklist = new HashSet<>(tagBlacklistConfig.get());
		tagWhitelist = new HashSet<>(tagWhitelistConfig.get());
	}
	
	
	public boolean test(ResourceLocation id, Block block) {
		boolean flag = defaultState.get();
		if (modBlacklist.contains(id.getNamespace())) flag = false;
		else if (modWhitelist.contains(id.getNamespace())) flag = true;
		if (block.defaultBlockState().getTags().anyMatch(tagKey -> tagBlacklist.contains(tagKey.location().toString()))) flag = false;
		if (block.defaultBlockState().getTags().anyMatch(tagKey -> tagWhitelist.contains(tagKey.location().toString()))) flag = true;
		String name = id.toString();
		if (blockBlacklist.contains(name)) flag = false;
		else if (blockWhitelist.contains(name)) flag = true;
		return flag;
	}
}