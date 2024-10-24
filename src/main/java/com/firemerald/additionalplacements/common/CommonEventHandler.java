package com.firemerald.additionalplacements.common;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.interfaces.IPlacementBlock;
import com.firemerald.additionalplacements.commands.CommandExportTags;
import com.firemerald.additionalplacements.config.APConfigs;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.MissingMappingsEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEventHandler
{
	public static boolean misMatchedTags = false;

	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event)
	{
		if (event.getItemStack().getItem() instanceof BlockItem)
		{
			Block block = ((BlockItem) event.getItemStack().getItem()).getBlock();
			if (block instanceof IPlacementBlock)
			{
				IPlacementBlock<?> verticalBlock = ((IPlacementBlock<?>) block);
				if (verticalBlock.hasAdditionalStates()) verticalBlock.appendHoverTextImpl(event.getItemStack(), event.getEntity() == null ? null : event.getEntity().level(), event.getToolTip(), event.getFlags());
			}
		}
	}

	@SubscribeEvent
	public static void onRegisterCommands(RegisterCommandsEvent event)
	{
		CommandExportTags.register(event.getDispatcher());
	}

	@SubscribeEvent
	public static void onTagsUpdated(TagsUpdatedEvent event)
	{
		misMatchedTags = false;
		if (APConfigs.COMMON.checkTags.get() && (!APConfigs.SERVER_SPEC.isLoaded() || APConfigs.SERVER.checkTags.get()))
			TagMismatchChecker.startChecker(); //TODO halt on datapack reload
	}

	@SubscribeEvent
	public static void onMissingBlockMappings(MissingMappingsEvent event)
	{
		event.getMappings(ForgeRegistries.BLOCKS.getRegistryKey(), AdditionalPlacementsMod.OLD_ID).forEach(mapping -> {
			String oldPath = mapping.getKey().getPath();
			if (oldPath.indexOf('.') < 0) //remap original format
			{
				String newPath = "minecraft." + oldPath;
				Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, newPath));
				if (block != Blocks.AIR)
				{
					mapping.remap(block);
					return;
				}
			}
			else //remap old mod ID
			{
				Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, oldPath));
				if (block != Blocks.AIR)
				{
					mapping.remap(block);
					return;
				}
			}
		});
	}

	@SubscribeEvent
	public static void onPlayerLogin(PlayerLoggedInEvent event)
	{
		if (misMatchedTags && !(APConfigs.COMMON.autoRebuildTags.get() && APConfigs.SERVER.autoRebuildTags.get()) && TagMismatchChecker.canGenerateTags(event.getEntity())) event.getEntity().sendSystemMessage(TagMismatchChecker.MESSAGE);
	}

	@SubscribeEvent
	public static void onServerStopping(ServerStoppingEvent event)
	{
		TagMismatchChecker.stopChecker();
	}
}