package com.firemerald.additionalplacements.config;

import com.firemerald.additionalplacements.generation.Registration;
import com.firemerald.additionalplacements.generation.GenerationType;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ConfigCommon
{
	public final BooleanValue showTooltip;
	public final BooleanValue checkTags, autoRebuildTags, logTagMismatch;
	public final IntValue checkerPriority;

	public ConfigCommon(ForgeConfigSpec.Builder builder)
	{
        builder.comment("Common settings").push("common");
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
        Registration.buildConfig(builder, GenerationType::buildCommonConfig);
	}
}