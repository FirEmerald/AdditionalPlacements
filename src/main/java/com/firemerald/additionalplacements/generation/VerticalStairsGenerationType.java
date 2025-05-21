package com.firemerald.additionalplacements.generation;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.block.interfaces.ISimpleRotationBlock;
import com.firemerald.additionalplacements.block.interfaces.IStairBlock;
import com.firemerald.additionalplacements.block.stairs.AdditionalStairBlock;
import com.firemerald.additionalplacements.block.stairs.StairConnectionsType;
import com.firemerald.additionalplacements.config.blocklist.Blocklist;
import com.firemerald.additionalplacements.util.MessageTree;
import com.firemerald.additionalplacements.util.TagIds;

import net.minecraft.block.StairsBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeConfigSpec;

public class VerticalStairsGenerationType<T extends StairsBlock, U extends AdditionalPlacementBlock<T> & ISimpleRotationBlock & IStairBlock<T>> extends SimpleRotatableGenerationType<T, U> {
	public abstract static class BuilderBase<T extends StairsBlock, U extends AdditionalPlacementBlock<T> & ISimpleRotationBlock & IStairBlock<T>, V extends SimpleRotatableGenerationType<T, U>, W extends BuilderBase<T, U, V, W>> extends SimpleRotatableGenerationType.BuilderBase<T, U, V, W> {
		protected Blocklist
				verticalConnectionsEnabled = new Blocklist(false, true),
				mixedConnectionsEnabled = new Blocklist(false, true);

		@Override
		public W constructor(Function<? super T, ? extends U> constructor) {
			throw new IllegalStateException("Function<? super T, ? extends U> constructor not supported");
		}

		public W verticalConnectionsEnabled(Blocklist enabled) {
			this.verticalConnectionsEnabled = enabled;
			return me();
		}

		public W mixedConnectionsEnabled(Blocklist enabled) {
			this.mixedConnectionsEnabled = enabled;
			return me();
		}
	}

	public static class Builder<T extends StairsBlock, U extends AdditionalPlacementBlock<T> & ISimpleRotationBlock & IStairBlock<T>> extends BuilderBase<T, U, VerticalStairsGenerationType<T, U>, Builder<T, U>> {
		@Override
		public VerticalStairsGenerationType<T, U> construct(ResourceLocation name, String description) {
			return new VerticalStairsGenerationType<>(name, description, this);
		}
	}

	private final Blocklist verticalConnectionsEnabled, mixedConnectionsEnabled;

	protected VerticalStairsGenerationType(ResourceLocation name, String description, BuilderBase<T, U, ?, ?> builder) {
		super(name, description, builder);
		this.verticalConnectionsEnabled = builder.verticalConnectionsEnabled;
		this.mixedConnectionsEnabled = builder.mixedConnectionsEnabled;
	}

	@Override
	public void buildStartupConfig(ForgeConfigSpec.Builder builder) {
		super.buildStartupConfig(builder);
		verticalConnectionsEnabled.addToConfig(builder, "allow_vertical_connections", "Blocklist to control which blocks will allow for vertical stair connections.\nKeep in mind vertical is RELATIVE to the placement of the stair - \"vertical\" for a vertically placed stair will be one of the two horizontal directions.");
		mixedConnectionsEnabled.addToConfig(builder, "allow_mixed_connections", "Blocklist to control which blocks will allow for mixed stair connections.\nThese are any valid combination of horizontal and vertical connection - as such, a stair that cannot connect vertically cannot connect complexly.\nThis also controls connections between stairs who's facings don't necessarily match up - I.E. a stair facing UP_EAST and one facing EAST_UP.\nKeep in mind horizontal and vertical are RELATIVE to the placement of the stair - \"vertical\" and \"horizontal\" for a vertically placed stair are both horizontal directions.");
	}

	@Override
	public CompoundNBT getClientCheckData() {
		CompoundNBT tag = new CompoundNBT();
		CompoundNBT noMixed = new CompoundNBT();
		CompoundNBT noVertical = new CompoundNBT();
		this.forEachCreated(entry -> {
			if (!entry.newBlock.connectionsType().allowVertical) { //simple
				addBlockEntry(noVertical, entry.originalId);
			} else if (!entry.newBlock.connectionsType().allowMixed) { //simple + vertical
				addBlockEntry(noMixed, entry.originalId);
			} //common
		});
		if (!noVertical.isEmpty()) tag.put("noVertical", noVertical);
		if (!noMixed.isEmpty()) tag.put("noMixed", noMixed);
		return tag;
	}

	public static void addBlockEntry(CompoundNBT tag, ResourceLocation id) {
		ListNBT modList;
		if (tag.contains(id.getNamespace(), TagIds.TAG_LIST)) modList = tag.getList(id.getNamespace(), TagIds.TAG_STRING);
		else tag.put(id.getNamespace(), modList = new ListNBT());
		modList.add(StringNBT.valueOf(id.getPath()));
	}

	@Override
	public void checkClientData(CompoundNBT tag, Consumer<MessageTree> onError) {
		if (tag != null) {
			Set<ResourceLocation> noVertical = loadEntries(tag, "noVertical");
			Set<ResourceLocation> noMixed = loadEntries(tag, "noMixed");

			Map<String, List<ResourceLocation>> mismatched = new HashMap<>();
			this.forEachCreated(entry -> {
				ResourceLocation id = entry.originalId;
				if (!entry.newBlock.connectionsType().allowVertical) { //simple
					if (!noVertical.contains(id)) {
						mismatched.computeIfAbsent("no_vertical_connections", u -> new ArrayList<>()).add(id);
					}
				} else if (!entry.newBlock.connectionsType().allowMixed) { //simple + vertical
					if (!noMixed.contains(id)) {
						mismatched.computeIfAbsent("no_mixed_connections", u -> new ArrayList<>()).add(id);
					}
				} else if (noVertical.contains(id) || noMixed.contains(id)) {
					mismatched.computeIfAbsent("all_connections", u -> new ArrayList<>()).add(id);
				}
			});
			if (!mismatched.isEmpty()) {
				MessageTree errorListRoot = new MessageTree(new TranslationTextComponent("msg.additionalplacements.stairs.mismatched.header"));
				mismatched.forEach((type, blocks) -> {
					MessageTree errorListChild = new MessageTree(new TranslationTextComponent("additionalplacements.stairs.connections_type." + type).append(":"));
					blocks.forEach(block -> errorListChild.children.add(new MessageTree(new StringTextComponent(block.toString()))));
					errorListRoot.children.add(errorListChild);
				});
				onError.accept(errorListRoot);
				onError.accept(new MessageTree(new TranslationTextComponent("msg.additionalplacements.stairs.mismatched.footer")));
			}
		}
	}

	public static Set<ResourceLocation> loadEntries(CompoundNBT tag, String key) {
		if (tag.contains(key, TagIds.TAG_COMPOUND)) {
			CompoundNBT entries = tag.getCompound(key);
			Set<ResourceLocation> set = new HashSet<>();
			entries.getAllKeys().forEach(modId -> {
				ListNBT modList = entries.getList(modId, TagIds.TAG_STRING);
				modList.forEach(nameTag -> set.add(new ResourceLocation(modId, nameTag.getAsString())));
			});
			return set;
		} else return Collections.emptySet();
	}

	@Override
	public void loadStartupConfig() {
		super.loadStartupConfig();
		verticalConnectionsEnabled.loadListsFromConfig();
		mixedConnectionsEnabled.loadListsFromConfig();
	}

	@SuppressWarnings("unchecked")
	@Override
	public U construct(T block, ResourceLocation blockId) {
		return (U) AdditionalStairBlock.of(block,
				!verticalConnectionsEnabled.test(block, blockId) ? StairConnectionsType.SIMPLE :
					!mixedConnectionsEnabled.test(block, blockId) ? StairConnectionsType.EXTENDED :
						StairConnectionsType.COMPLEX);
	}
}
