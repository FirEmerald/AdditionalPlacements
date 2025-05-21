package com.firemerald.additionalplacements.common;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.IntPredicate;

import org.apache.commons.lang3.tuple.Triple;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.config.APConfigs;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.ClickEvent.Action;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.rcon.RconConsoleSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.ForgeRegistries;

public class TagMismatchChecker extends Thread
{
	private static TagMismatchChecker thread = null;
	public static final Component MESSAGE =
			Component.translatable("msg.additionalplacements.mismatchedtags.0")
			.append(Component.literal("/ap_tags_export").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(Action.RUN_COMMAND, "/ap_tags_export")).withColor(ChatFormatting.BLUE).withUnderlined(true)))
			.append(Component.translatable("msg.additionalplacements.mismatchedtags.1")).setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withUnderlined(false))
			.append(Component.literal("/reload").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(Action.RUN_COMMAND, "/reload")).withColor(ChatFormatting.BLUE).withUnderlined(true)))
			.append(Component.translatable("msg.additionalplacements.mismatchedtags.2")).setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withUnderlined(false));
	public static final Component FAILED = Component.translatable("msg.additionalplacements.generate.notfixed").setStyle(Style.EMPTY.withColor(ChatFormatting.RED));

	public static void startChecker(MinecraftServer server, boolean autoGenerate, boolean fromAutoGenerate)
	{
		setThread(new TagMismatchChecker(server, autoGenerate, fromAutoGenerate));
		thread.setPriority(APConfigs.common().checkerPriority.get());
		CommonEventHandler.misMatchedTags = false;
		thread.start();
	}

	public static void stopChecker()
	{
		setThread(null);
	}

	private static synchronized void setThread(TagMismatchChecker newThread) {
		if (thread != null) thread.halted = true;
		thread = newThread;
	}

	private final MinecraftServer server;
	private final boolean autoGenerate, fromAutoGenerate;
	private boolean halted = false;
	private final List<Triple<Block, Collection<TagKey<Block>>, Collection<TagKey<Block>>>> blockMissingExtra = new LinkedList<>();

	private TagMismatchChecker(MinecraftServer server, boolean autoGenerate, boolean fromAutoGenerate)
	{
		super("Additional Placements Tag Mismatch Checker");
		this.server = server;
		this.autoGenerate = autoGenerate;
		this.fromAutoGenerate = fromAutoGenerate;
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
		if (!halted) server.submit(this::process);
	}

	//this is only ever called on the server thread
	public void process()
	{
		if (!halted) //wasn't canceled
		{
			if (!blockMissingExtra.isEmpty())
			{
				CommonEventHandler.misMatchedTags = true;
				if (fromAutoGenerate) CommonEventHandler.autoGenerateFailed = true;
				if (!autoGenerate) {
					Component message = fromAutoGenerate ? FAILED : MESSAGE;
					server.getPlayerList().getPlayers().forEach(player -> {
						if (canGenerateTags(player)) player.sendSystemMessage(message);
					});
				}
				AdditionalPlacementsMod.LOGGER.warn("Found missing and/or extra tags on generated blocks. Use \"/ap_tags_export\" to generate the tags, then \"/reload\" to re-load them (or re-load the world if that fails).");
				if (APConfigs.common().logTagMismatch.get())
				{
					AdditionalPlacementsMod.LOGGER.warn("====== BEGIN LIST ======");
					blockMissingExtra.forEach(blockMissingExtra -> {
                        AdditionalPlacementsMod.LOGGER.warn("\t{}", ForgeRegistries.BLOCKS.getKey(blockMissingExtra.getLeft()));
						Collection<TagKey<Block>> missing = blockMissingExtra.getMiddle();
						if (!missing.isEmpty())
						{
							AdditionalPlacementsMod.LOGGER.warn("\t\tmissing");
							missing.forEach(tag -> AdditionalPlacementsMod.LOGGER.warn("\t\t\t{}", tag.location()));
						}
						Collection<TagKey<Block>> extra = blockMissingExtra.getRight();
						if (!extra.isEmpty())
						{
							AdditionalPlacementsMod.LOGGER.warn("\t\textra");
							extra.forEach(tag -> AdditionalPlacementsMod.LOGGER.warn("\t\t\t{}", tag.location()));
						}
					});
					AdditionalPlacementsMod.LOGGER.warn("====== END LIST ======");
				}
				else AdditionalPlacementsMod.LOGGER.info("Not logging tag mismatches as it is disabled in the common config");
				if (autoGenerate)
				{
					AdditionalPlacementsMod.LOGGER.info("Rebuilding block tags and reloading datapacks as automatic tag rebuilding is enabled");
					CommandDispatcher<CommandSourceStack> dispatch = server.getCommands().getDispatcher();
					CommandSourceStack source = server.createCommandSourceStack();
					try
					{
						CommonEventHandler.reloadedFromChecker = true;
						dispatch.execute("ap_tags_export", source);
						dispatch.execute("reload", source);
					}
					catch (CommandSyntaxException e)
					{
						AdditionalPlacementsMod.LOGGER.error("Unexpected message whilst automatically rebuilding tags", e);
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

	@OnlyIn(Dist.CLIENT)
	public static boolean canGenerateTagsClient(Player player)
	{
		Player clientPlayer = Minecraft.getInstance().player;
		return clientPlayer == null || player.getGameProfile().equals(clientPlayer.getGameProfile());
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