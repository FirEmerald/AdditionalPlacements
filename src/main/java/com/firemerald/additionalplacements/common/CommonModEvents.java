package com.firemerald.additionalplacements.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang3.tuple.Pair;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.*;
import com.firemerald.additionalplacements.block.interfaces.IPlacementBlock;
import com.firemerald.additionalplacements.commands.CommandExportTags;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.brigadier.CommandDispatcher;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.level.block.*;

public class CommonModEvents implements ModInitializer
{
	@Override
    public void onInitialize()
    {
    	registerBlocks();
    	CommandRegistrationCallback.EVENT.register(CommonModEvents::onRegisterCommands);
    	CommonLifecycleEvents.TAGS_LOADED.register(CommonModEvents::onTagsUpdated);
    	ServerLifecycleEvents.SERVER_STARTED.register(server -> CommonModEvents.init());
    	ServerLifecycleEvents.SERVER_STOPPING.register(CommonModEvents::onServerStopping);
    	ServerPlayConnectionEvents.JOIN.register(CommonModEvents::onPlayerLogin);
    }
	
	private static boolean hasInit = false;

	public static void init() //TODO find better point to put this
	{
		if (!hasInit)
		{
			try
			{
				Class<?> clazz = Class.forName("com.google.common.base.Suppliers$NonSerializableMemoizingSupplier");
				Field delegate = clazz.getDeclaredField("delegate");
				delegate.setAccessible(true);
				Field initialized = clazz.getDeclaredField("initialized");
				initialized.setAccessible(true);
				Field value = clazz.getDeclaredField("value");
				value.setAccessible(true);
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
					modifyMap(WeatheringCopper.NEXT_BY_BLOCK, WeatheringCopper.PREVIOUS_BY_BLOCK, withAdditionalStates, delegate, initialized, value);
				}
				catch (IllegalArgumentException | IllegalAccessException e)
				{
					AdditionalPlacementsMod.LOGGER.error("Failed to update WeatheringCopper maps, copper slabs and stairs will weather into vanilla states. Sorry.", e);
				}
				try
				{
					modifyMap(HoneycombItem.WAXABLES, HoneycombItem.WAX_OFF_BY_BLOCK, withAdditionalStates, delegate, initialized, value);
				}
				catch (IllegalArgumentException | IllegalAccessException e)
				{
					AdditionalPlacementsMod.LOGGER.error("Failed to update WeatheringCopper maps, copper slabs and stairs will weather into vanilla states. Sorry.", e);
				}
			}
			catch (ClassNotFoundException | NoSuchFieldException | SecurityException e)
			{
				AdditionalPlacementsMod.LOGGER.error("Failed to update WeatheringCopper and HoneycombItem maps, copper slabs and stairs will weather into vanilla states and cannot be waxed. Sorry.", e);
			}
			hasInit = true;
		}
	}
	public static void registerBlocks()
	{
		boolean generateSlabs = AdditionalPlacementsMod.COMMON_CONFIG.generateSlabs.get();
		boolean generateStairs = AdditionalPlacementsMod.COMMON_CONFIG.generateStairs.get();
		boolean generateCarpets = AdditionalPlacementsMod.COMMON_CONFIG.generateCarpets.get();
		boolean generatePressurePlates = AdditionalPlacementsMod.COMMON_CONFIG.generatePressurePlates.get();
		boolean generateWeightedPressurePlates = AdditionalPlacementsMod.COMMON_CONFIG.generateWeightedPressurePlates.get();
		List<Pair<ResourceLocation, Block>> created = new ArrayList<>();
		BuiltInRegistries.BLOCK.entrySet().forEach(entry -> {
			ResourceLocation name = entry.getKey().location();
			Block block = entry.getValue();
			if (block instanceof SlabBlock)
			{
				if (generateSlabs) tryAdd((SlabBlock) block, name, VerticalSlabBlock::of, created);
			}
			else if (block instanceof StairBlock)
			{
				if (generateStairs) tryAdd((StairBlock) block, name, VerticalStairBlock::of, created);
			}
			else if (block instanceof CarpetBlock)
			{
				if (generateCarpets) tryAdd((CarpetBlock) block, name, AdditionalCarpetBlock::of, created);
			}
			else if (block instanceof PressurePlateBlock)
			{
				if (generatePressurePlates) tryAdd((PressurePlateBlock) block, name, AdditionalPressurePlateBlock::of, created);
			}
			else if (block instanceof WeightedPressurePlateBlock)
			{
				if (generateWeightedPressurePlates) tryAdd((WeightedPressurePlateBlock) block, name, AdditionalWeightedPressurePlateBlock::of, created);
			}
		});
		created.forEach(pair -> Registry.register(BuiltInRegistries.BLOCK, pair.getLeft(), pair.getRight()));
		AdditionalPlacementsMod.dynamicRegistration = true;
	}
	
	private static <T extends Block, U extends AdditionalPlacementBlock<T>> void tryAdd(T block, ResourceLocation name, Function<T, U> construct, List<Pair<ResourceLocation, Block>> list)
	{
		if (!((IPlacementBlock<?>) block).hasAdditionalStates() && AdditionalPlacementsMod.COMMON_CONFIG.isValidForGeneration(name))
			list.add(Pair.of(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, name.getNamespace() + "." + name.getPath()), construct.apply(block)));
	}
	
	public static <T, U> void modifyMap(Supplier<BiMap<T, U>> forwardMemoized, Supplier<BiMap<U, T>> backwardMemoized, Function<BiMap<T, U>, BiMap<T, U>> modify, Field delegate, Field initialized, Field value) throws IllegalArgumentException, IllegalAccessException
	{
		@SuppressWarnings("unchecked")
		com.google.common.base.Supplier<BiMap<T, U>> supplier = (com.google.common.base.Supplier<BiMap<T, U>>) delegate.get(forwardMemoized);
		if (supplier == null)
		{
			@SuppressWarnings("unchecked")
			BiMap<T, U> map = (BiMap<T, U>) value.get(forwardMemoized);
			supplier = () -> map;
		}
		final com.google.common.base.Supplier<BiMap<T, U>> oldSupplier = supplier;
		delegate.set(forwardMemoized, (com.google.common.base.Supplier<BiMap<T, U>>) () -> modify.apply(oldSupplier.get()));
		initialized.setBoolean(forwardMemoized, false);
		value.set(forwardMemoized, null);
		delegate.set(backwardMemoized, (com.google.common.base.Supplier<BiMap<U, T>>) () -> forwardMemoized.get().inverse());
		initialized.setBoolean(backwardMemoized, false);
		value.set(backwardMemoized, null);
	}
    
	public static boolean misMatchedTags = false;

	public static void onRegisterCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment)
	{
		CommandExportTags.register(dispatcher);
	}

	public static void onTagsUpdated(RegistryAccess registries, boolean client)
	{
		misMatchedTags = false;
		if (AdditionalPlacementsMod.COMMON_CONFIG.checkTags.get() && (!AdditionalPlacementsMod.serverSpec.isLoaded() || AdditionalPlacementsMod.SERVER_CONFIG.checkTags.get()))
			TagMismatchChecker.startChecker(); //TODO halt on datapack reload
	}

	public static void onPlayerLogin(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server)
	{
		if (misMatchedTags && !(AdditionalPlacementsMod.COMMON_CONFIG.autoRebuildTags.get() && AdditionalPlacementsMod.SERVER_CONFIG.autoRebuildTags.get()) && TagMismatchChecker.canGenerateTags(handler.getPlayer())) handler.getPlayer().sendSystemMessage(TagMismatchChecker.MESSAGE);
	}

	public static void onServerStopping(MinecraftServer server)
	{
		TagMismatchChecker.stopChecker();
	}
}