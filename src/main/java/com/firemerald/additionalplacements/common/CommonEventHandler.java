package com.firemerald.additionalplacements.common;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.command.CommandExportTags;
import com.firemerald.additionalplacements.command.CommandGenerateStairsDebugger;
import com.firemerald.additionalplacements.config.APConfigs;
import com.firemerald.additionalplacements.generation.GenerationType;
import com.firemerald.additionalplacements.generation.Registration;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEventHandler
{
	public static boolean misMatchedTags = false, autoGenerateFailed = false;
	protected static boolean reloadedFromChecker = false;

	@SubscribeEvent
	public static void onRegisterCommands(RegisterCommandsEvent event)
	{
		CommandExportTags.register(event.getDispatcher());
		CommandGenerateStairsDebugger.register(event.getDispatcher());
	}

	@SubscribeEvent
	public static void onTagsUpdated(TagsUpdatedEvent.CustomTagTypes event)
	{
		Registration.forEach(GenerationType::onTagsUpdated);
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if (server == null || event.getTagManager() == server.getTags()) {
			boolean fromAutoGenerate;
			if (reloadedFromChecker) {
				reloadedFromChecker = false;
				fromAutoGenerate = true;
			} else fromAutoGenerate = false;
			possiblyCheckTags(server, fromAutoGenerate);
		}
	}

	@SubscribeEvent
	public static void onServerStarted(FMLServerStartedEvent event) {
		possiblyCheckTags(event.getServer(), false);
	}

	private static void possiblyCheckTags(MinecraftServer server, boolean fromAutoGenerate) {
		misMatchedTags = false;
		autoGenerateFailed = false;
		if (APConfigs.common().checkTags.get() && APConfigs.server().checkTags.get())
			TagMismatchChecker.startChecker(server, !fromAutoGenerate && APConfigs.common().autoRebuildTags.get() && APConfigs.server().autoRebuildTags.get(), fromAutoGenerate); //TODO halt on datapack reload
	}

	@SubscribeEvent
	public static void onMissingBlockMappings(RegistryEvent.MissingMappings<Block> event)
	{
		event.getMappings(AdditionalPlacementsMod.OLD_ID).forEach(mapping -> {
			String oldPath = mapping.key.getPath();
			if (oldPath.indexOf('.') < 0) //remap original format
			{
				String newPath = "minecraft." + oldPath;
				Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, newPath));
				if (block != Blocks.AIR)
				{
					mapping.remap(block);
                }
			}
			else //remap old mod ID
			{
				Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, oldPath));
				if (block != Blocks.AIR)
				{
					mapping.remap(block);
                }
			}
		});
	}

	@SubscribeEvent
	public static void onPlayerLogin(PlayerLoggedInEvent event)
	{
		if (misMatchedTags && TagMismatchChecker.canGenerateTags(event.getPlayer())) event.getPlayer().sendMessage(autoGenerateFailed ? TagMismatchChecker.FAILED : TagMismatchChecker.MESSAGE, Util.NIL_UUID);
	}

	@SubscribeEvent
	public static void onServerStopping(FMLServerStoppingEvent event)
	{
		TagMismatchChecker.stopChecker();
		reloadedFromChecker = false;
	}
}