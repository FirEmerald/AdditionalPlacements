package com.firemerald.additionalplacements.common;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntPredicate;

import org.apache.commons.lang3.tuple.Triple;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.util.ResourceLocation;

public class TagMismatchChecker extends Thread implements Consumer<ServerTickEvent>
{
	private static TagMismatchChecker thread = null;
	public static final ITextComponent MESSAGE = new TranslationTextComponent("msg.additionalplacements.mismatchedtags.0").append(
			new StringTextComponent("/ap_tags_export").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(Action.RUN_COMMAND, "/ap_tags_export")).withColor(TextFormatting.BLUE).withUnderlined(true)).append(
					new TranslationTextComponent("msg.additionalplacements.mismatchedtags.1").withStyle(Style.EMPTY.withUnderlined(false).withColor(TextFormatting.WHITE)).append(
							new StringTextComponent("/reload").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(Action.RUN_COMMAND, "/reload")).withColor(TextFormatting.BLUE).withUnderlined(true)).append(
									new TranslationTextComponent("msg.additionalplacements.mismatchedtags.2").withStyle(Style.EMPTY.withUnderlined(false).withColor(TextFormatting.WHITE))
									)
							)
					)
			);
	
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
	private final List<Triple<Block, Collection<ResourceLocation>, Collection<ResourceLocation>>> blockMissingExtra = new LinkedList<>();
	
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
				Triple<Block, Collection<ResourceLocation>, Collection<ResourceLocation>> mismatch = ((AdditionalPlacementBlock<?>) block).checkTagMismatch();
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
				ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers().forEach(player -> {
					if (canGenerateTags(player)) player.sendMessage(MESSAGE, Util.NIL_UUID);
				});
				AdditionalPlacementsMod.LOGGER.warn("Found missing and/or extra tags on generated blocks. Use \"/ap_tags_export\" to generate the tags, then \"/reload\" to re-load them (or re-load the world if that fails).");
				if (AdditionalPlacementsMod.COMMON_CONFIG.logTagMismatch.get())
				{
					AdditionalPlacementsMod.LOGGER.warn("====== BEGIN LIST ======");
					blockMissingExtra.forEach(blockMissingExtra -> {
						AdditionalPlacementsMod.LOGGER.warn("\t" + blockMissingExtra.getLeft().getRegistryName());
						Collection<ResourceLocation> missing = blockMissingExtra.getMiddle();
						if (!missing.isEmpty())
						{
							AdditionalPlacementsMod.LOGGER.warn("\t\tmissing");
							missing.forEach(tag -> AdditionalPlacementsMod.LOGGER.warn("\t\t\t" + tag));
						}
						Collection<ResourceLocation> extra = blockMissingExtra.getRight();
						if (!extra.isEmpty())
						{
							AdditionalPlacementsMod.LOGGER.warn("\t\textra");
							extra.forEach(tag -> AdditionalPlacementsMod.LOGGER.warn("\t\t\t" + tag));
						}
					});
					AdditionalPlacementsMod.LOGGER.warn("====== END LIST ======");
				}
				else AdditionalPlacementsMod.LOGGER.info("Not logging tag mismatches as it is disabled in the common config");
			}
		}
	}
	
	@SuppressWarnings("resource")
	public static boolean canGenerateTags(PlayerEntity player, IntPredicate hasPermission)
	{
		if (FMLLoader.getDist().isClient()) //client world, only world host
		{
			PlayerEntity clientPlayer = Minecraft.getInstance().player;
			return clientPlayer == null || player.getGameProfile().getId().equals(clientPlayer.getGameProfile().getId());
		}
		else return hasPermission.test(2);
	}
	
	public static boolean canGenerateTags(PlayerEntity player)
	{
		return canGenerateTags(player, player::hasPermissions);
	}
	
	public static boolean canGenerateTags(CommandSource source)
	{
		return source.getEntity() instanceof PlayerEntity && canGenerateTags((PlayerEntity) source.getEntity(), source::hasPermission);
	}
}