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

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Registration {
	private static final List<IBlockBlacklister<Block>> BLACKLISTERS = new LinkedList<>();
	private static List<RegistrationInitializer> registrators = new ArrayList<>();

	public static void addRegistration(RegistrationInitializer listener) {
		if (registrators == null) throw new IllegalStateException("A mod tried to register a registrator too late into the load sequence! Registrators should be registered in your mod constructor.");
		registrators.add(listener);
	}

	public static void registerTypes() {
		registrators.forEach(registrator -> {
			registrator.onInitializeRegistration(Registration::registerType);
			registrator.addGlobalBlacklisters(BLACKLISTERS::add);
		});
		registrators = null;
	}

	private static final Map<ResourceLocation, GenerationType<?, ?>> TYPES = new LinkedHashMap<>();
	private static final Map<Class<?>, GenerationType<?, ?>> TYPES_BY_CLASS = new HashMap<>();

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

	private static <T extends Block, U extends AdditionalPlacementBlock<T>, V extends GenerationType<T, U>> V registerType(Class<T> clazz, ResourceLocation name, String description, BuilderBase<T, U, V, ?> builder) {
		if (TYPES.containsKey(name)) throw new IllegalStateException("A generation type with name " + name + " is already registered!");
		V type = builder.construct(name, description);
		registrators.forEach(registrator -> registrator.addBlacklisters(clazz, type, blacklister -> type.addBlacklister(blacklister)));
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

	public static Stream<CreatedBlockEntry<?, ?>> created() {
		return types().flatMap(GenerationType::created);
	}

	public static void forEachCreated(Consumer<? super CreatedBlockEntry<?, ?>> action) {
		TYPES.values().forEach(type -> type.forEachCreated(action));
	}

	public static void buildConfig(ModConfigSpec.Builder builder, BiConsumer<GenerationType<?, ?>, ModConfigSpec.Builder> build) {
        builder.comment("Options for registered block types for additional placement generation.").push("types");
        Registration.forEach((name, type) -> {
        	List<String> path = split(name.getNamespace() + "." + name.getPath());
        	builder.comment(type.description).push(path);
        	build.accept(type, builder);
        	builder.pop(path.size());
        });
        builder.pop();
	}

	//from ModConfigSpec, used to ensure we can pop everything pushed in buildConfig
    private static final Splitter DOT_SPLITTER = Splitter.on(".");
    private static List<String> split(String path)
    {
        return Lists.newArrayList(DOT_SPLITTER.split(path));
    }
}