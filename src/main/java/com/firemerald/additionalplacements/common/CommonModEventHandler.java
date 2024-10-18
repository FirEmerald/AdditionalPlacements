package com.firemerald.additionalplacements.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang3.tuple.Pair;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.interfaces.IPlacementBlock;
import com.firemerald.additionalplacements.config.APConfigs;
import com.firemerald.additionalplacements.datagen.ModelGenerator;
import com.firemerald.additionalplacements.generation.Registration;
import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.level.block.*;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEventHandler
{
	public static boolean lockGenerationTypeRegistry = false;
	
	@SubscribeEvent
	public static void onNewRegistry(NewRegistryEvent event) { //best hook I could find for loading a config after all mods have been processed but before registries are built
		if (!lockGenerationTypeRegistry) {
			lockGenerationTypeRegistry = true;
			APConfigs.init();
			APConfigs.BOOTUP.loadConfig(FMLPaths.CONFIGDIR.get().resolve("additionalplacements-bootup.toml"), APConfigs.BOOTUP_SPEC);
		}
	}
	
	@SubscribeEvent
	public static void onBlockRegistry(RegisterEvent event)
	{
		if (event.getForgeRegistry() != null && ForgeRegistries.BLOCKS.getRegistryKey().equals(event.getForgeRegistry().getRegistryKey()))
		{
			IForgeRegistry<Block> registry = event.getForgeRegistry();
			List<Pair<ResourceLocation, Block>> created = new ArrayList<>();
			registry.getEntries().forEach(entry -> {
				ResourceLocation name = entry.getKey().location();
				Block block = entry.getValue();
				Registration.tryApply(block, name, (id, obj) -> created.add(Pair.of(id, obj)));
			});
			created.forEach(pair -> registry.register(pair.getLeft(), pair.getRight()));
			AdditionalPlacementsMod.dynamicRegistration = true;
		}
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event)
	{
		try //we need to do this hack because we can't have non-final static fields on interfaces, because Java doesn't let us have nice things. However, it is volatile, and should be replaced when it becomes possible.
		{
			Class<?> clazz = Class.forName("com.google.common.base.Suppliers$NonSerializableMemoizingSupplier");
			Field delegate = clazz.getDeclaredField("delegate");
			delegate.setAccessible(true);
			Field value = clazz.getDeclaredField("value");
			value.setAccessible(true);
			Field successfullyComputedField = clazz.getDeclaredField("SUCCESSFULLY_COMPUTED");
			successfullyComputedField.setAccessible(true);
			Object successfullyComputed = successfullyComputedField.get(null);
			Function<BiMap<Block, Block>, BiMap<Block, Block>> withAdditionalStates = oldMap -> {
				BiMap<Block, Block> newMap = HashBiMap.create(oldMap);
				oldMap.forEach((b1, b2) -> {
					if (b1 instanceof IPlacementBlock && b2 instanceof IPlacementBlock)
					{
						IPlacementBlock<?> p1 = (IPlacementBlock<?>) b1;
						IPlacementBlock<?> p2 = (IPlacementBlock<?>) b2;
						if (p1.hasAdditionalStates() && p2.hasAdditionalStates()) newMap.put(p1.getOtherBlock(), p2.getOtherBlock());
					}
				});
				return newMap;
			};
			try
			{
				modifyMap(WeatheringCopper.NEXT_BY_BLOCK, WeatheringCopper.PREVIOUS_BY_BLOCK, withAdditionalStates, delegate, value, successfullyComputed);
			}
			catch (IllegalArgumentException | IllegalAccessException e)
			{
				AdditionalPlacementsMod.LOGGER.error("Failed to update WeatheringCopper maps, copper slabs and stairs will weather into vanilla states. Sorry.", e);
			}
		}
		catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalAccessException e)
		{
			AdditionalPlacementsMod.LOGGER.error("Failed to update WeatheringCopper maps, copper slabs and stairs will weather into vanilla states. Sorry.", e);
		}
		Supplier<BiMap<Block, Block>> waxables = HoneycombItem.WAXABLES;
		HoneycombItem.WAXABLES = Suppliers.memoize(() -> addVariants(waxables.get()));
		HoneycombItem.WAX_OFF_BY_BLOCK = Suppliers.memoize(() -> HoneycombItem.WAXABLES.get().inverse());
	}

	public static BiMap<Block, Block> addVariants(Map<Block, Block> oldMap)
	{
		BiMap<Block, Block> newMap = HashBiMap.create(oldMap);
		oldMap.forEach((b1, b2) -> {
			if (b1 instanceof IPlacementBlock && b2 instanceof IPlacementBlock)
			{
				IPlacementBlock<?> p1 = (IPlacementBlock<?>) b1;
				IPlacementBlock<?> p2 = (IPlacementBlock<?>) b2;
				if (p1.hasAdditionalStates() && p2.hasAdditionalStates()) newMap.put(p1.getOtherBlock(), p2.getOtherBlock());
			}
		});
		return newMap;
	}

	public static <T, U> void modifyMap(Supplier<BiMap<T, U>> forwardMemoized, Supplier<BiMap<U, T>> backwardMemoized, Function<BiMap<T, U>, BiMap<T, U>> modify, Field delegate, Field value, Object successfullyComputed) throws IllegalArgumentException, IllegalAccessException
	{
		@SuppressWarnings("unchecked")
		com.google.common.base.Supplier<BiMap<T, U>> forwardSupplier = (com.google.common.base.Supplier<BiMap<T, U>>) delegate.get(forwardMemoized);
		if (forwardSupplier == successfullyComputed) //already computed
		{
			@SuppressWarnings("unchecked")
			BiMap<T, U> map = (BiMap<T, U>) value.get(forwardMemoized); //get existing map
			value.set(forwardMemoized, null); //clear value
			delegate.set(forwardMemoized, (com.google.common.base.Supplier<BiMap<T, U>>) () -> modify.apply(map)); //replace with supplier that modifies the existing map
		}
		else delegate.set(forwardMemoized, (com.google.common.base.Supplier<BiMap<T, U>>) () -> modify.apply(forwardSupplier.get())); //replace with supplier that modifies the result of the existing supplier
		@SuppressWarnings("unchecked")
		com.google.common.base.Supplier<BiMap<U, T>> backwardSupplier = (com.google.common.base.Supplier<BiMap<U, T>>) delegate.get(backwardMemoized);
		if (backwardSupplier == successfullyComputed) value.set(backwardMemoized, null); //clear computed value
		delegate.set(backwardMemoized, (com.google.common.base.Supplier<BiMap<U, T>>) () -> forwardMemoized.get().inverse()); //replace with supplier that gets the inverse of the forward map
	}

	@SubscribeEvent
	public static void onGatherData(GatherDataEvent event)
	{
		if (event.includeClient())
		{
			event.getGenerator().addProvider(true, (DataProvider.Factory<ModelGenerator>) (PackOutput pack) -> new ModelGenerator(pack, AdditionalPlacementsMod.MOD_ID, event.getExistingFileHelper()));
		}
	}

	public static boolean doubleslabsLoaded;

	@SubscribeEvent
	public static void onFMLCommonSetup(FMLCommonSetupEvent event)
	{
		doubleslabsLoaded = ModList.get().isLoaded("doubleslabs");
	}
}