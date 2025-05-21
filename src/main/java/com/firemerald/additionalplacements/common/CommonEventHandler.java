package com.firemerald.additionalplacements.common;

import com.firemerald.additionalplacements.block.interfaces.IPlacementBlock;
import com.firemerald.additionalplacements.commands.CommandExportTags;
import com.firemerald.additionalplacements.commands.CommandGenerateStairsDebugger;
import com.firemerald.additionalplacements.config.APConfigs;
import com.firemerald.additionalplacements.generation.Registration;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent.UpdateCause;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEventHandler
{
	public static boolean misMatchedTags = false, autoGenerateFailed = false;
	protected static boolean reloadedFromChecker = false;

	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event)
	{
		if (event.getItemStack().getItem() instanceof BlockItem)
		{
			Block block = ((BlockItem) event.getItemStack().getItem()).getBlock();
			if (block instanceof IPlacementBlock<?> verticalBlock)
			{
                if (verticalBlock.hasAdditionalStates()) verticalBlock.appendHoverTextImpl(event.getItemStack(), event.getEntity() == null ? null : event.getEntity().level(), event.getToolTip(), event.getFlags());
			}
		}
	}

	@SubscribeEvent
	public static void onRegisterCommands(RegisterCommandsEvent event)
	{
		CommandExportTags.register(event.getDispatcher());
		CommandGenerateStairsDebugger.register(event.getDispatcher(), event.getBuildContext());
	}

	@SubscribeEvent
	public static void onTagsUpdated(TagsUpdatedEvent event)
	{
		if (event.getUpdateCause() == UpdateCause.SERVER_DATA_LOAD) {
			Registration.forEach(type -> type.onTagsUpdated(false));
			boolean fromAutoGenerate;
			if (reloadedFromChecker) {
				reloadedFromChecker = false;
				fromAutoGenerate = true;
			} else fromAutoGenerate = false;
			MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
			if (server != null) possiblyCheckTags(server, fromAutoGenerate);
		}
		else Registration.forEach(type -> type.onTagsUpdated(true));
	}

	@SubscribeEvent
	public static void onServerStarted(ServerStartedEvent event) {
		possiblyCheckTags(event.getServer(), false);
	}

	private static void possiblyCheckTags(MinecraftServer server, boolean fromAutoGenerate) {
		misMatchedTags = false;
		autoGenerateFailed = false;
		if (APConfigs.common().checkTags.get() && APConfigs.server().checkTags.get())
			TagMismatchChecker.startChecker(server, !fromAutoGenerate && APConfigs.common().autoRebuildTags.get() && APConfigs.server().autoRebuildTags.get(), fromAutoGenerate); //TODO halt on datapack reload
	}

	@SubscribeEvent
	public static void onPlayerLogin(PlayerLoggedInEvent event)
	{
		if (misMatchedTags && TagMismatchChecker.canGenerateTags(event.getEntity())) event.getEntity().sendSystemMessage(autoGenerateFailed ? TagMismatchChecker.FAILED : TagMismatchChecker.MESSAGE);
	}

	@SubscribeEvent
	public static void onServerStopping(ServerStoppingEvent event)
	{
		TagMismatchChecker.stopChecker();
		reloadedFromChecker = false;
	}
}