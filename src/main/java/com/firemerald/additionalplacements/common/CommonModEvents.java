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
import com.firemerald.additionalplacements.commands.CommandExportTags;
import com.firemerald.additionalplacements.commands.CommandGenerateStairsDebugger;
import com.firemerald.additionalplacements.config.APConfigs;
import com.firemerald.additionalplacements.generation.Registration;
import com.firemerald.additionalplacements.network.APNetwork;
import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.brigadier.CommandDispatcher;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;

public class CommonModEvents implements ModInitializer
{
	@Override
    public void onInitialize()
    {
    	APNetwork.register();
    	loadRegistry();
    	CommandRegistrationCallback.EVENT.register(CommonModEvents::onRegisterCommands);
    	CommonLifecycleEvents.TAGS_LOADED.register(CommonModEvents::onTagsUpdated);
    	ServerLifecycleEvents.SERVER_STARTED.register(CommonModEvents::onServerStarted);
    	ServerLifecycleEvents.SERVER_STOPPING.register(CommonModEvents::onServerStopping);
    	ServerPlayConnectionEvents.JOIN.register(CommonModEvents::onPlayerLogin);
    }

	private static void loadRegistry() {
		Registration.gatherTypes();
		APConfigs.init();
		List<Pair<ResourceKey<Block>, Block>> created = new ArrayList<>();
		BuiltInRegistries.BLOCK.entrySet().forEach(entry -> Registration.tryApply(entry.getValue(), entry.getKey().location(), (key, obj) -> created.add(Pair.of(key, obj))));
		created.forEach(pair -> Registry.register(BuiltInRegistries.BLOCK, pair.getLeft(), pair.getRight()));
		RegistryEntryAddedCallback.event(BuiltInRegistries.BLOCK).register((rawId, id, block) -> Registration.tryApply(block, id, (blockId, obj) -> Registry.register(BuiltInRegistries.BLOCK, blockId, obj)));
	}

	private static boolean hasInit = false;

	public static void init()
	{
		if (!hasInit)
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
						if (b1 instanceof IPlacementBlock<?> p1 && b2 instanceof IPlacementBlock<?> p2)
						{
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
			hasInit = true;
		}
	}

	public static BiMap<Block, Block> addVariants(Map<Block, Block> oldMap)
	{
		BiMap<Block, Block> newMap = HashBiMap.create(oldMap);
		oldMap.forEach((b1, b2) -> {
			if (b1 instanceof IPlacementBlock<?> p1 && b2 instanceof IPlacementBlock<?> p2)
			{
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

	public static boolean misMatchedTags = false, autoGenerateFailed = false;
	protected static boolean reloadedFromChecker = false;

	public static void onRegisterCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment)
	{
		CommandExportTags.register(dispatcher);
		CommandGenerateStairsDebugger.register(dispatcher, registryAccess);
	}

	public static void onTagsUpdated(RegistryAccess registries, boolean client)
	{
		if (!client) {
			Registration.forEach(type -> type.onTagsUpdated(false));
			boolean fromAutoGenerate;
			if (reloadedFromChecker) {
				reloadedFromChecker = false;
				fromAutoGenerate = true;
			} else fromAutoGenerate = false;
			if (currentServer != null) possiblyCheckTags(fromAutoGenerate);
		}
		else Registration.forEach(type -> type.onTagsUpdated(true));
	}

	private static void possiblyCheckTags(boolean fromAutoGenerate) {
		misMatchedTags = false;
		autoGenerateFailed = false;
		if (APConfigs.common().checkTags.get() && APConfigs.server().checkTags.get())
			TagMismatchChecker.startChecker(currentServer, !fromAutoGenerate && APConfigs.common().autoRebuildTags.get() && APConfigs.server().autoRebuildTags.get(), fromAutoGenerate); //TODO halt on datapack reload
	}

	public static void onPlayerLogin(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server)
	{
		if (misMatchedTags && TagMismatchChecker.canGenerateTags(handler.getPlayer())) handler.getPlayer().sendSystemMessage(autoGenerateFailed ? TagMismatchChecker.FAILED : TagMismatchChecker.MESSAGE);
	}

	private static MinecraftServer currentServer = null;

	public static void onServerStarted(MinecraftServer server) {
		currentServer = server;
		init();
		possiblyCheckTags(false);
	}

	public static void onServerStopping(MinecraftServer server)
	{
		currentServer = null;
		TagMismatchChecker.stopChecker();
		reloadedFromChecker = false;
	}
}