package com.firemerald.additionalplacements.generation;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.block.interfaces.ISimpleRotationBlock;
import com.firemerald.additionalplacements.block.interfaces.IStairBlock;
import com.firemerald.additionalplacements.block.stairs.AdditionalStairBlock;
import com.firemerald.additionalplacements.block.stairs.StairConnectionsType;
import com.firemerald.additionalplacements.config.GenerationBlacklist;
import com.firemerald.additionalplacements.util.MessageTree;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.StairBlock;
import net.minecraftforge.common.ForgeConfigSpec;

public class VerticalStairsGenerationType<T extends StairBlock, U extends AdditionalPlacementBlock<T> & ISimpleRotationBlock & IStairBlock<T>> extends SimpleRotatableGenerationType<T, U> {
	protected abstract static class BuilderBase<T extends StairBlock, U extends AdditionalPlacementBlock<T> & ISimpleRotationBlock & IStairBlock<T>, V extends SimpleRotatableGenerationType<T, U>, W extends BuilderBase<T, U, V, W>> extends SimpleRotatableGenerationType.BuilderBase<T, U, V, W> {
		protected GenerationBlacklist
		vertcialConnectionsBlacklist = new GenerationBlacklist.Builder().build(),
		mixedConnectionsBlacklist = new GenerationBlacklist.Builder().build();

		@Override
		public W constructor(Function<? super T, ? extends U> constructor) {
			throw new IllegalStateException("Function<? super T, ? extends U> constructor not supported");
		}

		public W blacklistVerticalConnections(GenerationBlacklist blacklist) {
			this.vertcialConnectionsBlacklist = blacklist;
			return me();
		}

		public W blacklistMixedConnections(GenerationBlacklist blacklist) {
			this.mixedConnectionsBlacklist = blacklist;
			return me();
		}
	}

	public static class Builder<T extends StairBlock, U extends AdditionalPlacementBlock<T> & ISimpleRotationBlock & IStairBlock<T>> extends BuilderBase<T, U, VerticalStairsGenerationType<T, U>, Builder<T, U>> {
		@Override
		public VerticalStairsGenerationType<T, U> construct(ResourceLocation name, String description) {
			return new VerticalStairsGenerationType<>(name, description, this);
		}
	}

	private final GenerationBlacklist vertcialConnectionsBlacklist;
	private final GenerationBlacklist mixedConnectionsBlacklist;

	protected VerticalStairsGenerationType(ResourceLocation name, String description, BuilderBase<T, U, ?, ?> builder) {
		super(name, description, builder);
		this.vertcialConnectionsBlacklist = builder.vertcialConnectionsBlacklist;
		this.mixedConnectionsBlacklist = builder.mixedConnectionsBlacklist;
	}

	@Override
	public void buildStartupConfig(ForgeConfigSpec.Builder builder) {
		super.buildStartupConfig(builder);
		builder
		.comment("Options to control which blocks will allow for vertical stair connections.\nKeep in mind vertical is RELATIVE to the placement of the stair - \"vertical\" for a vertically placed stair will be one of the two horizontal directions.")
		.push("allow_vertical_connections");
		vertcialConnectionsBlacklist.addToConfig(builder);
		builder.pop();
		builder
		.comment("Options to control which blocks will allow for mixed stair connections.\nThese are any valid combination of horizontal and vertical connection - as such, a stair that cannot connect vertically cannot connect complexly.\nThis also controls connections between stairs who's facings don't necessarily match up - I.E. a stair facing UP_EAST and one facing EAST_UP.\nKeep in mind horizontal and vertical are RELATIVE to the placement of the stair - \"vertical\" and \"horizontal\" for a vertically placed stair are both horizontal directions.")
		.push("allow_mixed_connections");
		mixedConnectionsBlacklist.addToConfig(builder);
		builder.pop();
	}

	@Override
	public CompoundTag getClientCheckData() {
		CompoundTag tag = new CompoundTag();
		CompoundTag noMixed = new CompoundTag();
		CompoundTag noVertical = new CompoundTag();
		this.forEachCreated(entry -> {
			if (!entry.newBlock().connectionsType().allowVertical) { //simple
				addBlockEntry(noVertical, entry.originalId());
			} else if (!entry.newBlock().connectionsType().allowMixed) { //simple + vertical
				addBlockEntry(noMixed, entry.originalId());
			} //common
		});
		if (!noVertical.isEmpty()) tag.put("noVertical", noVertical);
		if (!noMixed.isEmpty()) tag.put("noMixed", noMixed);
		return tag;
	}

	public static void addBlockEntry(CompoundTag tag, ResourceLocation id) {
		ListTag modList;
		if (tag.contains(id.getNamespace(), Tag.TAG_LIST)) modList = tag.getList(id.getNamespace(), Tag.TAG_STRING);
		else tag.put(id.getNamespace(), modList = new ListTag());
		modList.add(StringTag.valueOf(id.getPath()));
	}

	@Override
	public void checkClientData(CompoundTag tag, Consumer<MessageTree> onError) {
		if (tag != null) {
			Set<ResourceLocation> noVertical = loadEntries(tag, "noVertical");
			Set<ResourceLocation> noMixed = loadEntries(tag, "noMixed");

			Map<String, List<ResourceLocation>> mismatched = new HashMap<>();
			this.forEachCreated(entry -> {
				ResourceLocation id = entry.originalId();
				if (!entry.newBlock().connectionsType().allowVertical) { //simple
					if (!noVertical.contains(id)) {
						mismatched.computeIfAbsent("no_vertical_connections", u -> new ArrayList<>()).add(id);
					}
				} else if (!entry.newBlock().connectionsType().allowMixed) { //simple + vertical
					if (!noMixed.contains(id)) {
						mismatched.computeIfAbsent("no_mixed_connections", u -> new ArrayList<>()).add(id);
					}
				} else if (noVertical.contains(id) || noMixed.contains(id)) {
					mismatched.computeIfAbsent("all_connections", u -> new ArrayList<>()).add(id);
				}
			});
			if (!mismatched.isEmpty()) {
				MessageTree errorListRoot = new MessageTree(Component.translatable("msg.additionalplacements.stairs.mismatched.header"));
				mismatched.forEach((type, blocks) -> {
					MessageTree errorListChild = new MessageTree(Component.translatable("additionalplacements.stairs.connections_type." + type).append(":"));
					blocks.forEach(block -> errorListChild.children.add(new MessageTree(Component.literal(block.toString()))));
					errorListRoot.children.add(errorListChild);
				});
				onError.accept(errorListRoot);
				onError.accept(new MessageTree(Component.translatable("msg.additionalplacements.stairs.mismatched.footer")));
			}
		}
	}

	public static Set<ResourceLocation> loadEntries(CompoundTag tag, String key) {
		if (tag.contains(key, Tag.TAG_COMPOUND)) {
			CompoundTag entries = tag.getCompound(key);
			Set<ResourceLocation> set = new HashSet<>();
			entries.getAllKeys().forEach(modId -> {
				ListTag modList = entries.getList(modId, Tag.TAG_STRING);
				modList.forEach(nameTag -> set.add(new ResourceLocation(modId, nameTag.getAsString())));
			});
			return set;
		} else return Collections.emptySet();
	}

	@Override
	public void loadStartupConfig() {
		super.loadStartupConfig();
		vertcialConnectionsBlacklist.loadListsFromConfig();
		mixedConnectionsBlacklist.loadListsFromConfig();
	}

	@SuppressWarnings("unchecked")
	@Override
	public U construct(T block, ResourceLocation blockId) {
		return (U) AdditionalStairBlock.of(block,
				!vertcialConnectionsBlacklist.test(blockId) ? StairConnectionsType.SIMPLE :
					!mixedConnectionsBlacklist.test(blockId) ? StairConnectionsType.EXTENDED :
						StairConnectionsType.COMPLEX);
	}
}
