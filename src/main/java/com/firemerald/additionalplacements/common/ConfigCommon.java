package com.firemerald.additionalplacements.common;

import java.util.Collections;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ConfigCommon
{
	public final BooleanValue generateSlabs;
	public final BooleanValue generateStairs;
	public final BooleanValue generateCarpets;
	public final BooleanValue generatePressurePlates;
	public final BooleanValue generateWeightedPressurePlates;
	public final BooleanValue disableAutomaticSlabPlacement;
	public final BooleanValue disableAutomaticStairPlacement;
	public final BooleanValue disableAutomaticCarpetPlacement;
	public final BooleanValue disableAutomaticPressurePlatePlacement;
	public final BooleanValue disableAutomaticWeightedPressurePlatePlacement;
	public final BooleanValue showTooltip;
	public final BooleanValue checkTags, autoRebuildTags, logTagMismatch;
	public final IntValue checkerPriority;
	public final ConfigValue<List<String>> blockBlacklist;
	public final ConfigValue<List<String>> modBlacklist;
	public final ConfigValue<List<String>> blockWhitelist;

	@SuppressWarnings("unchecked")
	public ConfigCommon(ForgeConfigSpec.Builder builder)
	{
        builder.comment("Common settings").push("common");
        generateSlabs = builder
        		.comment("Automatically generate vertical slabs")
        		.define("automatic_slabs", true);
        generateStairs = builder
        		.comment("Automatically generate vertical stairs")
        		.define("automatic_stairs", true);
        generateCarpets = builder
        		.comment("Automatically generate wall and ceiling carpets")
        		.define("automatic_carpets", true);
        generatePressurePlates = builder
        		.comment("Automatically generate wall and ceiling pressure plates")
        		.define("automatic_pressure_plates", true);
        generateWeightedPressurePlates = builder
        		.comment("Automatically generate wall and ceiling weighted pressure plates")
        		.define("automatic_weighted_pressure_plates", true);
        disableAutomaticSlabPlacement = builder
        		.comment("Disable automatic vertical slab placement. Use if a mod E.G. DoubleSlabs is conflicting.")
        		.define("disable_slab_placement", false);
        disableAutomaticStairPlacement = builder
        		.comment("Disable automatic vertical stair placement. Use if a mod is conflicting.")
        		.define("disable_stair_placement", false);
        disableAutomaticCarpetPlacement = builder
        		.comment("Disable automatic wall and ceiling carpet placement. Use if a mod is conflicting.")
        		.define("disable_carpet_placement", false);
        disableAutomaticPressurePlatePlacement = builder
        		.comment("Disable automatic wall and ceiling pressure plate placement. Use if a mod is conflicting.")
        		.define("disable_pressure_plate_placement", false);
        disableAutomaticWeightedPressurePlatePlacement = builder
        		.comment("Disable automatic wall and ceiling weighted pressure plate placement. Use if a mod is conflicting.")
        		.define("disable_weighted_pressure_plate_placement", false);
        showTooltip = builder
        		.comment("Show tooltip when a block has additional placements")
        		.define("tooltip", true);
        checkTags = builder
        		.comment("Check for and notify of mismatching tags. Only works when the same option in the server/world config is true.")
        		.define("check_tags", true);
        autoRebuildTags = builder
        		.comment("Automatically rebuild and reload the generated tags datapack when a tagging mismatch is detected. Only works when the same option in the server/world config is true.")
        		.define("auto_rebuild_tags", true);
        logTagMismatch = builder
        		.comment("Log missing or additional tags on generated blocks.")
        		.define("log_tag_mismatch", false);
        checkerPriority = builder
        		.comment("The thread priority of the mismatched tag checker. " + Thread.MIN_PRIORITY + " is lowest, " + Thread.MAX_PRIORITY + " is highest, " + Thread.NORM_PRIORITY + " is normal.")
        		.defineInRange("checker_priority", Thread.MIN_PRIORITY, Thread.MIN_PRIORITY, Thread.MAX_PRIORITY);
        blockBlacklist = (ConfigValue<List<String>>) (Object) builder
        		.comment("Blacklist for blocks to not have generated placement variants")
        		.defineList("blacklist", Collections.emptyList(), o -> {
        			return o instanceof String;
        		});
        modBlacklist = (ConfigValue<List<String>>) (Object) builder
        		.comment("Blacklist for mods to not have generated placement variants")
        		.defineList("mod_blacklist", Collections.emptyList(), o -> {
        			return o instanceof String;
        		});
        blockWhitelist = (ConfigValue<List<String>>) (Object) builder
        		.comment("Whitelist for blocks from blacklisted mods to still have generated placement variants")
        		.defineList("mod_block_whitelist", Collections.emptyList(), o -> {
        			return o instanceof String;
        		});
	}

	public boolean isValidForGeneration(ResourceLocation blockName)
	{
		return !blockBlacklist.get().contains(blockName.toString()) && (!modBlacklist.get().contains(blockName.getNamespace()) || blockWhitelist.get().contains(blockName.toString()));
	}
}