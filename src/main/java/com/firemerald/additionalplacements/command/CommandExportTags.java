package com.firemerald.additionalplacements.command;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.*;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.common.TagMismatchChecker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.storage.FolderName;
import net.minecraft.block.Block;

public class CommandExportTags
{
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	public static final String PACK_FOLDER_NAME = "additional_placements_generated_tags";
	public static final ResourceLocation PACK_META_LOC = new ResourceLocation(AdditionalPlacementsMod.MOD_ID, "generated_datapack_meta.mcmeta");
	
	public static void optionalMakeDirectory(Path path) throws IOException
	{
        if (!Files.exists(path)) Files.createDirectory(path);
        else if (!Files.isDirectory(path))
        {
        	Files.delete(path);
        	Files.createDirectory(path);
        }
	}
	
	public static void optionalMakeDirectories(Path path) throws IOException
	{
        if (!Files.exists(path)) Files.createDirectories(path);
        else if (!Files.isDirectory(path))
        {
        	Files.delete(path);
        	Files.createDirectory(path);
        }
	}
	
	public static void emptyDirectory(Path path) throws IOException
	{
		Iterator<Path> it = Files.list(path).iterator();
		while (it.hasNext())
		{
			Path file = it.next();
			if (Files.isDirectory(file)) emptyDirectory(file);
			Files.delete(file);
		}
	}
	
	@SuppressWarnings("resource")
	public static void register(CommandDispatcher<CommandSource> dispatch)
	{
		dispatch.register(Commands.literal("ap_tags_export").requires(TagMismatchChecker::canGenerateTags).executes(context -> {
			CommandSource source = context.getSource();
			MinecraftServer server = source.getServer();
			Path packPath = server.getWorldPath(FolderName.DATAPACK_DIR).resolve(PACK_FOLDER_NAME);
			try
			{
				optionalMakeDirectory(packPath);
				try
				{
					Files.copy(CommandExportTags.class.getResource("/assets/" + PACK_META_LOC.getNamespace() + "/" + PACK_META_LOC.getPath()).openStream(), packPath.resolve("pack.mcmeta"), StandardCopyOption.REPLACE_EXISTING);
				}
				catch (IOException e)
				{
					AdditionalPlacementsMod.LOGGER.error("Error generating datapack: failed to copy pack definition", e);
					source.sendFailure(new TranslationTextComponent("msg.additionalplacements.generate.failure.definition"));
				}
				Path dataPath = packPath.resolve("data");
				if (Files.exists(dataPath))
				{
					if (Files.isDirectory(dataPath)) emptyDirectory(dataPath);
					else
					{
						Files.delete(dataPath);
						Files.createDirectory(dataPath);
					}
				}
				else Files.createDirectory(dataPath);
				Map<ResourceLocation, List<Block>> tagMap = new HashMap<>();
				ForgeRegistries.BLOCKS.forEach(block -> {
					if (block instanceof AdditionalPlacementBlock)
					{
						Set<ResourceLocation> tags = ((AdditionalPlacementBlock<?>) block).getDesiredTags();
						tags.forEach(tag -> tagMap.computeIfAbsent(tag, key -> new LinkedList<>()).add(block));
					}
				});
				tagMap.forEach((tag, blocks) -> {
					try
					{
						Path tagPath = dataPath.resolve(tag.getNamespace() + "/tags/blocks/" + tag.getPath() + ".json");
						optionalMakeDirectories(tagPath.getParent());
						JsonObject obj = new JsonObject();
						obj.addProperty("replace", false);
						JsonArray array = new JsonArray();
						blocks.forEach(block -> array.add(block.getRegistryName().toString()));
						obj.add("values", array);
						Files.write(tagPath, GSON.toJson(obj).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
					}
					catch (IOException e)
					{
						AdditionalPlacementsMod.LOGGER.error("Error generating datapack: failed to save tag " + tag.toString(), e);
						source.sendFailure(new TranslationTextComponent("msg.additionalplacements.generate.failure.tag", tag.toString()));
					}
				});
				AdditionalPlacementsMod.LOGGER.info("Finished exporting tags");
				source.sendSuccess(new TranslationTextComponent("msg.additionalplacements.generate.success"), true);
			}
			catch (IOException e)
			{
				AdditionalPlacementsMod.LOGGER.error("Error generating datapack: failed to initialize datapack", e);
				source.sendFailure(new TranslationTextComponent("msg.additionalplacements.generate.failure.initialization"));
			}
			return 0;
		}));
	}
}