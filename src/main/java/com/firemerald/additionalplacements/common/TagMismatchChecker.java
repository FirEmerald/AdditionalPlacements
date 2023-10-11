package com.firemerald.additionalplacements.common;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntPredicate;

import org.apache.commons.lang3.tuple.Triple;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.ClickEvent.Action;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.rcon.RconConsoleSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;

public class TagMismatchChecker extends Thread implements Consumer<ServerTickEvent>
{
	private static TagMismatchChecker thread = null;
	public static final Component MESSAGE = 
			Component.translatable("msg.additionalplacements.mismatchedtags.0")
			.append(Component.literal("/ap_tags_export").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(Action.RUN_COMMAND, "/ap_tags_export")).withColor(ChatFormatting.BLUE).withUnderlined(true)))
			.append(Component.translatable("msg.additionalplacements.mismatchedtags.1")).setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withUnderlined(false))
			.append(Component.literal("/reload").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(Action.RUN_COMMAND, "/reload")).withColor(ChatFormatting.BLUE).withUnderlined(true)))
			.append(Component.translatable("msg.additionalplacements.mismatchedtags.2")).setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withUnderlined(false));
	
	public static void startChecker()
	{
		TagMismatchChecker old = thread;
		thread = new TagMismatchChecker();
		if (old != null) old.halted = true;
		thread.setPriority(AdditionalPlacementsMod.COMMON_CONFIG.checkerPriority.get());
		CommonEventHandler.misMatchedTags = false;
		thread.start();
	}
	
	public static void stopChecker()
	{
		if (thread != null)
		{
			TagMismatchChecker old = thread;
			thread = null;
			old.halted = true;
		}
	}
	
	private boolean halted = false;
	private final List<Triple<Block, Collection<TagKey<Block>>, Collection<TagKey<Block>>>> blockMissingExtra = new LinkedList<>();
	
	private TagMismatchChecker()
	{
		super("Additional Placements Tag Mismatch Checker");
	}
	
	@Override
	public void run()
	{
		for (Block block : ForgeRegistries.BLOCKS)
		{
			if (halted) return;
			if (block instanceof AdditionalPlacementBlock)
			{
				Triple<Block, Collection<TagKey<Block>>, Collection<TagKey<Block>>> mismatch = ((AdditionalPlacementBlock<?>) block).checkTagMismatch();
				if (mismatch != null) blockMissingExtra.add(mismatch);
			}
		}
		MinecraftForge.EVENT_BUS.addListener(this); //listen for next server tick
	}
	
	//this is only ever called on the server thread
	public void accept(ServerTickEvent event)
	{
		MinecraftForge.EVENT_BUS.unregister(this); //only listen once
		if (!halted) //wasn't canceled
		{
			if (!blockMissingExtra.isEmpty())
			{
				CommonEventHandler.misMatchedTags = true;
				boolean autoRebuild = AdditionalPlacementsMod.COMMON_CONFIG.autoRebuildTags.get() && AdditionalPlacementsMod.SERVER_CONFIG.autoRebuildTags.get();
				MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
				if (!autoRebuild) server.getPlayerList().getPlayers().forEach(player -> {
					if (canGenerateTags(player)) player.sendSystemMessage(MESSAGE);
				});
				AdditionalPlacementsMod.LOGGER.warn("Found missing and/or extra tags on generated blocks. Use \"/ap_tags_export\" to generate the tags, then \"/reload\" to re-load them (or re-load the world if that fails).");
				if (AdditionalPlacementsMod.COMMON_CONFIG.logTagMismatch.get())
				{
					AdditionalPlacementsMod.LOGGER.warn("====== BEGIN LIST ======");
					blockMissingExtra.forEach(blockMissingExtra -> {
						AdditionalPlacementsMod.LOGGER.warn("\t" + ForgeRegistries.BLOCKS.getKey(blockMissingExtra.getLeft()));
						Collection<TagKey<Block>> missing = blockMissingExtra.getMiddle();
						if (!missing.isEmpty())
						{
							AdditionalPlacementsMod.LOGGER.warn("\t\tmissing");
							missing.forEach(tag -> AdditionalPlacementsMod.LOGGER.warn("\t\t\t" + tag.location()));
						}
						Collection<TagKey<Block>> extra = blockMissingExtra.getRight();
						if (!extra.isEmpty())
						{
							AdditionalPlacementsMod.LOGGER.warn("\t\textra");
							extra.forEach(tag -> AdditionalPlacementsMod.LOGGER.warn("\t\t\t" + tag.location()));
						}
					});
					AdditionalPlacementsMod.LOGGER.warn("====== END LIST ======");
				}
				else AdditionalPlacementsMod.LOGGER.info("Not logging tag mismatches as it is disabled in the common config");
				if (autoRebuild)
				{
					AdditionalPlacementsMod.LOGGER.info("Rebuilding block tags and reloading datapacks as automatic tag rebuilding is enabled");
					CommandDispatcher<CommandSourceStack> dispatch = server.getCommands().getDispatcher();
					CommandSourceStack source = server.createCommandSourceStack();
					try
					{
						dispatch.execute("ap_tags_export", source);
						dispatch.execute("reload", source);
					}
					catch (CommandSyntaxException e)
					{
						AdditionalPlacementsMod.LOGGER.error("Unexpected error whilst automatically rebuilding tags", e);
					}
				}
			}
		}
	}
	
	public static boolean canGenerateTags(Player player, IntPredicate hasPermission)
	{
		if (FMLLoader.getDist().isClient()) return canGenerateTagsClient(player);
		else return hasPermission.test(2);
	}

	@SuppressWarnings("resource")
	@OnlyIn(Dist.CLIENT)
	public static boolean canGenerateTagsClient(Player player)
	{
		Player clientPlayer = Minecraft.getInstance().player;
		return clientPlayer == null || player.getGameProfile().getId().equals(clientPlayer.getGameProfile().getId());
	}
	
	public static boolean canGenerateTags(Player player)
	{
		return canGenerateTags(player, player::hasPermissions);
	}
	
	public static boolean canGenerateTags(CommandSourceStack source)
	{
		return source.source instanceof RconConsoleSource || source.source instanceof MinecraftServer || (source.isPlayer() && canGenerateTags((Player) source.getEntity(), source::hasPermission));
	}
}