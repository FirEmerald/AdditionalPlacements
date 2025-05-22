package com.firemerald.additionalplacements.generation;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.block.interfaces.IPlacementBlock;
import com.firemerald.additionalplacements.config.APConfigs;
import com.firemerald.additionalplacements.generation.GenerationType.BuilderBase;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;

public class Registration {
	private static final List<IBlockBlacklister<Block>> BLACKLISTERS = new LinkedList<>();
	private static final Map<ResourceLocation, GenerationType<?, ?>> TYPES = new LinkedHashMap<>();
	private static final Map<Class<?>, GenerationType<?, ?>> TYPES_BY_CLASS = new HashMap<>();

	public static void gatherTypes() {
		FabricLoader.getInstance().invokeEntrypoints("additional-placements-generators", RegistrationInitializer.class, instance -> {
			instance.onInitializeRegistration(Registration::registerType);
			instance.addGlobalBlacklisters(BLACKLISTERS::add);
		});
	}

	@SuppressWarnings("unchecked")
	public static <T extends Block, U extends AdditionalPlacementBlock<T>> void tryApply(Block block, ResourceLocation blockId, BiConsumer<ResourceLocation, AdditionalPlacementBlock<?>> action) {
		if (block instanceof IPlacementBlock<?> placement && placement.canGenerateAdditionalStates() && BLACKLISTERS.stream().noneMatch(blacklister -> blacklister.blacklist(block, blockId)) && APConfigs.startup().enabled.test(block, blockId)) {
			GenerationType<T, U> type = (GenerationType<T, U>) getType(block);
			if (type != null) type.apply((T) block, blockId, (BiConsumer<ResourceLocation, U>) action);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends Block, U extends AdditionalPlacementBlock<T>> GenerationType<T, U> getType(T block) {
		return getType((Class<? extends T>) block.getClass());
	}

	@SuppressWarnings("unchecked")
	public static <T extends Block, U extends AdditionalPlacementBlock<T>> GenerationType<T, U> getType(Class<? extends T> clazz) {
		GenerationType<T, U> type = (GenerationType<T, U>) TYPES_BY_CLASS.get(clazz);
		if (type != null) return type;
		else {
			Class<?> parent = clazz.getSuperclass();
			if (parent == Block.class) return null;
			else return getType((Class<? extends T>) parent);
		}
	}

	protected static <T extends Block, U extends AdditionalPlacementBlock<T>, V extends GenerationType<T, U>> V registerType(Class<T> clazz, ResourceLocation name, String description, BuilderBase<T, U, V, ?> builder) {
		if (TYPES.containsKey(name)) throw new IllegalStateException("A generation type with name " + name + " is already registered!");
		V type = builder.construct(name, description);
		FabricLoader.getInstance().invokeEntrypoints("additional-placements-generators", RegistrationInitializer.class, instance -> instance.addBlacklisters(clazz, type, blacklister -> type.addBlacklister(blacklister)));
		TYPES.put(name, type);
		if (TYPES_BY_CLASS.containsKey(clazz))
            AdditionalPlacementsMod.LOGGER.warn("A generation type for class {} is already registered. The registration with name {} will not be used.", clazz, name);
		else TYPES_BY_CLASS.put(clazz, type);
		return type;
	}

	public static void forEach(BiConsumer<? super ResourceLocation, ? super GenerationType<?, ?>> action) {
		TYPES.forEach(action);
	}

	public static void forEach(Consumer<? super GenerationType<?, ?>> action) {
		TYPES.values().forEach(action);
	}

	public static Stream<GenerationType<?, ?>> types() {
		return TYPES.values().stream();
	}

	public static void forEachCreated(Consumer<? super CreatedBlockEntry<?, ?>> action) {
		TYPES.values().forEach(type -> type.forEachCreated(action));
	}

	public static Stream<CreatedBlockEntry<?, ?>> created() {
		return types().flatMap(GenerationType::created);
	}

	public static void buildConfig(ForgeConfigSpec.Builder builder, BiConsumer<GenerationType<?, ?>, ForgeConfigSpec.Builder> build) {
        builder.comment("Options for registered block types for additional placement generation.").push("types");
        Registration.forEach((name, type) -> {
        	List<String> path = split(name.getNamespace() + "." + name.getPath());
        	builder.comment(type.description).push(path);
        	build.accept(type, builder);
        	builder.pop(path.size());
        });
        builder.pop();
	}

	//from ForgeConfigSpec, used to ensure we can pop everything pushed in buildConfig
    private static final Splitter DOT_SPLITTER = Splitter.on(".");
    private static List<String> split(String path)
    {
        return Lists.newArrayList(DOT_SPLITTER.split(path));
    }
}