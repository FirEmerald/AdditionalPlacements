package com.firemerald.additionalplacements.generation;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.config.APConfigs;
import com.firemerald.additionalplacements.config.GenerationBlacklist;
import com.firemerald.additionalplacements.util.MessageTree;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public abstract class GenerationType<T extends Block, U extends AdditionalPlacementBlock<T>> {

	public abstract static class BuilderBase<T extends Block, U extends AdditionalPlacementBlock<T>, V extends GenerationType<T, U>, W extends BuilderBase<T, U, V, W>> {
		protected Set<String> addsProperties = Collections.emptySet();
		protected GenerationBlacklist blacklist = new GenerationBlacklist.Builder().build();
		protected boolean placementEnabled = true;

		@SuppressWarnings("unchecked")
		protected W me() {
			return (W) this;
		}

		public W addsProperties(String... properties) {
			return addsProperties(Arrays.asList(properties));
		}

		public W addsProperties(Collection<String> properties) {
			this.addsProperties = new HashSet<>(properties);
			return me();
		}

		public W blacklist(GenerationBlacklist blacklist) {
			this.blacklist = blacklist;
			return me();
		}

		public W placementEnabled() {
			placementEnabled = true;
			return me();
		}

		public W placementDisabled() {
			placementEnabled = false;
			return me();
		}

		public abstract V construct(ResourceLocation name, String description);
	}

	public final ResourceLocation name;
	public final String description;
	private final Set<String> addsProperties;
	private final GenerationBlacklist blacklist;
	private final boolean defaultPlacementEnabled;
	private BooleanValue placementEnabled;
	private final List<CreatedBlockEntry<T, U>> created = new ArrayList<>();
	private final List<IBlockBlacklister<? super T>> blacklisters = new LinkedList<>();

	protected GenerationType(ResourceLocation name, String description, BuilderBase<T, U, ?, ?> builder) {
		this.name = name;
		this.description = description;
		this.addsProperties = builder.addsProperties;
		this.blacklist = builder.blacklist;
		this.defaultPlacementEnabled = builder.placementEnabled;

	}

	protected void addBlacklister(IBlockBlacklister<? super T> blacklister) {
		blacklisters.add(blacklister);
	}

	public boolean placementEnabled() {
		return placementEnabled.get();
	}

	//The following method is for the "startup" config, a custom config that loads before block registration and doesn't support re-loading changed values in-game.
	//They should be used for options that affect the dynamic generation of additional placement blocks.
	public void buildStartupConfig(ForgeConfigSpec.Builder builder) {
		builder
		.comment("Options for controlling which blocks (that are valid for this type) will generate variants of this type")
		.push("enabled");
		blacklist.addToConfig(builder);
		builder.pop();
	}

	public final void onStartupConfigLoaded() {
		loadStartupConfig();
	}

	protected void loadStartupConfig() {
		blacklist.loadListsFromConfig();
	}

	public void buildCommonConfig(ForgeConfigSpec.Builder builder) {
		placementEnabled = builder
				.comment("Whether or not to allow for manual placement of the additional placement variants of this block type.")
				.define("enable_placement", defaultPlacementEnabled); //TODO make this also a blacklist
	}

	public final void onCommonConfigLoaded() {
		loadCommonConfig();
		updateCommonSettings();
	}

	protected void loadCommonConfig() {}

	protected void updateCommonSettings() {}

	public void buildClientConfig(ForgeConfigSpec.Builder builder) {}

	public final void onClientConfigLoaded() {
		loadClientConfig();
		updateClientSettings();
	}

	protected void loadClientConfig() {}

	protected void updateClientSettings() {}

	public void buildServerConfig(ForgeConfigSpec.Builder builder) {}

	public final void onServerConfigLoaded() {
		loadServerConfig();
		updateServerSettings();
	}

	protected void loadServerConfig() {}

	protected void updateServerSettings() {}

	public void onTagsUpdated(boolean isClient) {
		if (APConfigs.commonLoaded()) updateCommonSettings();
		if (isClient && APConfigs.clientLoaded()) updateClientSettings();
		if (APConfigs.serverLoaded()) updateServerSettings();
	}

	/**
	 * Data to check ON SERVER
	 *
	 * @return
	 */
	public CompoundTag getClientCheckData() {
		return null;
	}

	/**
	 * Check data FROM CLIENT
	 *
	 * @return
	 */
	public void checkClientData(CompoundTag tag, Consumer<MessageTree> logError) {}

	/**
	 * Data to check ON CLIENT
	 *
	 * @return
	 */
	public CompoundTag getServerCheckData() {
		return null;
	}

	/**
	 * Check data FROM SERVER
	 *
	 * @return
	 */
	public void checkServerData(CompoundTag tag, Consumer<MessageTree> logError) {}

	public final boolean enabledForBlock(T block, ResourceLocation blockId) {
		if (blacklisters.stream().anyMatch(blacklister -> blacklister.blacklist(block, blockId))) return false;
		if (blacklist.test(blockId)) {
			Collection<String> has = block.defaultBlockState().getProperties().stream().map(Property::getName).filter(addsProperties::contains).toList();
			if (!has.isEmpty()) {
                AdditionalPlacementsMod.LOGGER.warn("Generation type {} cannot generate for {} as it already contains the following properties that would be added: ", this.name, blockId);
				AdditionalPlacementsMod.LOGGER.warn(has.toString());
				AdditionalPlacementsMod.LOGGER.warn("Add it to the blacklist inside additionalplacements-startup.toml to stop this message from appearing in the future.");
				return false;
			} else return true;
		} else return false;
	}

	public final void apply(T block, ResourceLocation blockId, BiConsumer<ResourceLocation, U> action) {
		if (enabledForBlock(block, blockId)) {
			ResourceLocation newId = new ResourceLocation(name.getNamespace(), blockId.getNamespace() + "." + blockId.getPath());
			U created = construct(block, blockId);
			this.created.add(new CreatedBlockEntry<>(blockId, block, newId, created));
			action.accept(newId, created);
		}
	}

	public abstract U construct(T block, ResourceLocation blockId);

	public void forEachCreated(Consumer<? super CreatedBlockEntry<T, U>> action) {
		created.forEach(action);
	}

	public Stream<CreatedBlockEntry<T, U>> created() {
		return created.stream();
	}
}
