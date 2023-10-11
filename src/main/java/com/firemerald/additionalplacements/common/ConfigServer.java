package com.firemerald.additionalplacements.common;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class ConfigServer
{
	public final BooleanValue checkTags, autoRebuildTags;

	public ConfigServer(ForgeConfigSpec.Builder builder)
	{
        builder.comment("World settings").push("server");
        checkTags = builder
        		.comment("Check for and notify of mismatching tags. Only works when the same option in the common config is true.")
        		.define("check_tags", true);
        autoRebuildTags = builder
        		.comment("Automatically rebuild and reload the generated tags datapack when a tagging mismatch is detected. Only works when the same option in the server/world config is true.")
        		.define("auto_rebuild_tags", true);
	}
}