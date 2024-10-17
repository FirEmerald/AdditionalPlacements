package com.firemerald.additionalplacements.generation;

import java.util.*;
import java.util.function.BiConsumer;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.block.interfaces.IPlacementBlock;
import com.firemerald.additionalplacements.common.CommonModEventHandler;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Registration {
	private static final Map<ResourceLocation, GenerationType<?, ?>> TYPES = new LinkedHashMap<>();
	private static final Map<Class<?>, GenerationType<?, ?>> TYPES_BY_CLASS = new HashMap<>();
	private static final Object PROTECTION_KEY = new Object(); //this simple object is used to determine if a GenerationType is being instantiated improperly
	
	protected static boolean isProtectionKey(Object obj) {
		return obj == PROTECTION_KEY;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Block, U extends AdditionalPlacementBlock<T>> void tryApply(Block block, ResourceLocation blockId, BiConsumer<ResourceLocation, AdditionalPlacementBlock<?>> action) {
		if (block instanceof IPlacementBlock && !((IPlacementBlock<?>) block).hasAdditionalStates()) {
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
	
	public static <T extends Block, U extends AdditionalPlacementBlock<T>, V extends GenerationType<T, U>> V registerType(Class<T> clazz, ResourceLocation name, String description, GenerationTypeConstructor<V> typeConstructor) {
		if (CommonModEventHandler.lockGenerationTypeRegistry) throw new IllegalStateException("A mod tried to register an Additional Placements generation type too late into the load sequence! Types must be registered before net.minecraftforge.registries.NewRegistryEvent is fired. The offending GenerationType name (whose namespace is generally the offending mod ID) is " + name);
		if (TYPES.containsKey(name)) throw new IllegalStateException("A generation type with name " + name + " is already registered!");
		V type = typeConstructor.construct(PROTECTION_KEY, name, description);
		TYPES.put(name, type);
		if (TYPES_BY_CLASS.containsKey(clazz)) AdditionalPlacementsMod.LOGGER.warn("A generation type for class " + clazz + " is already registered. The registration with name " + name + " will not be used.");
		else TYPES_BY_CLASS.put(clazz, type);
		return type;
	}
	
	public static void forEach(@SuppressWarnings("rawtypes") BiConsumer<? super ResourceLocation, ? super GenerationType> action) {
		TYPES.forEach(action);
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