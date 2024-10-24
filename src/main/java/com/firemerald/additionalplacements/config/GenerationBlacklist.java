package com.firemerald.additionalplacements.config;

import java.util.*;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class GenerationBlacklist {
	public abstract static class BuilderBase<T extends BuilderBase<T>> {
		protected boolean defaultState = true;
		protected List<String> 
		modBlacklist = Collections.emptyList(), 
		modWhitelist = Collections.emptyList(), 
		blockBlacklist = Collections.emptyList(), 
		blockWhitelist = Collections.emptyList();
		
		@SuppressWarnings("unchecked")
		public T me() {
			return (T) this;
		}
		
		public T defaultEnabled() {
			defaultState = true;
			return me();
		}
		
		public T defaultDisabled() {
			defaultState = false;
			return me();
		}
		
		
		
		public T emptyModBlacklist() {
			return modBlacklist(Collections.emptyList());
		}
		
		public T modBlacklist(List<String> entries) {
			modBlacklist = entries;
			return me();
		}
		
		public T modBlacklist(Collection<String> entries) {
			return modBlacklist(new ArrayList<>(entries));
		}
		
		public T modBlacklist(String... entries) {
			return modBlacklist(Arrays.asList(entries));
		}
		
		
		public T emptyModWhitelist() {
			return modWhitelist(Collections.emptyList());
		}
		
		public T modWhitelist(List<String> entries) {
			modWhitelist = entries;
			return me();
		}
		
		public T modWhitelist(Collection<String> entries) {
			return modWhitelist(new ArrayList<>(entries));
		}
		
		public T modWhitelist(String... entries) {
			return modWhitelist(Arrays.asList(entries));
		}
		
		
		
		public T emptyBlockBlacklist() {
			return blockBlacklist(Collections.emptyList());
		}
		
		public T blockBlacklist(List<String> entries) {
			blockBlacklist = entries;
			return me();
		}
		
		public T blockBlacklist(Collection<String> entries) {
			return blockBlacklist(new ArrayList<>(entries));
		}
		
		public T blockBlacklist(String... entries) {
			return blockBlacklist(Arrays.asList(entries));
		}
		
		
		public T emptyBlockWhitelist() {
			return blockWhitelist(Collections.emptyList());
		}
		
		public T blockWhitelist(List<String> entries) {
			blockWhitelist = entries;
			return me();
		}
		
		public T blockWhitelist(Collection<String> entries) {
			return blockWhitelist(new ArrayList<>(entries));
		}
		
		public T blockWhitelist(String... entries) {
			return blockWhitelist(Arrays.asList(entries));
		}
	}
	
	public static class Builder extends BuilderBase<Builder> {
		public GenerationBlacklist build() {
			return new GenerationBlacklist(defaultState, modBlacklist, modWhitelist, blockBlacklist, blockWhitelist);
		}
	}
	
	protected final boolean defaultDefaultState;
	protected BooleanValue defaultState;
	protected final List<? extends String> 
	defaultModBlacklist, 
	defaultModWhitelist, 
	defaultBlockBlacklist, 
	defaultBlockWhitelist;
	protected ConfigValue<List<? extends String>>
	modBlacklistConfig,
	modWhitelistConfig,
	blockBlacklistConfig,
	blockWhitelistConfig;
	protected Collection<String>
	modBlacklist = Collections.emptyList(), 
	modWhitelist = Collections.emptyList(),
	blockBlacklist = Collections.emptyList(),
	blockWhitelist = Collections.emptyList();
	
	public GenerationBlacklist(boolean defaultState, List<? extends String> modBlacklist, List<? extends String> modWhitelist, List<? extends String> blockBlacklist, List<? extends String> blockWhitelist) {
		defaultDefaultState = defaultState;
		defaultModBlacklist = modBlacklist;
		defaultModWhitelist = modWhitelist;
		defaultBlockBlacklist = blockBlacklist;
		defaultBlockWhitelist = blockWhitelist;
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
		blockWhitelistConfig = builder
				.comment("Whitelist for blocks that will be enabled. Takes priority over the the mod blacklist and whitelist.")
				.defineListAllowEmpty(Collections.singletonList("block_whitelist"), () -> defaultBlockWhitelist, o -> {
					return o instanceof String;
				});
		blockBlacklistConfig = builder
				.comment("Blacklist for blocks that will be disabled. Takes priority over the block whitelist, the mod blacklist, and the mod whitelist.")
				.defineListAllowEmpty(Collections.singletonList("block_blacklist"), () -> defaultBlockBlacklist, o -> {
					return o instanceof String;
				});
	}
	
	public void loadListsFromConfig() {
		modBlacklist = new HashSet<>(modBlacklistConfig.get());
		modWhitelist = new HashSet<>(modWhitelistConfig.get());
		blockBlacklist = new HashSet<>(blockBlacklistConfig.get());
		blockWhitelist = new HashSet<>(blockWhitelistConfig.get());
	}
	
	public boolean test(ResourceLocation id) {
		boolean flag = defaultState.get();
		if (modBlacklist.contains(id.getNamespace())) flag = false;
		else if (modWhitelist.contains(id.getNamespace())) flag = true;
		String name = id.toString();
		if (blockBlacklist.contains(name)) flag = false;
		else if (blockWhitelist.contains(name)) flag = true;
		return flag;
	}
}