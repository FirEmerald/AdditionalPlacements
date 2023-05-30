package com.firemerald.additionalplacements.common;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class ConfigServer
{
	public final BooleanValue checkTags;

	public ConfigServer(ForgeConfigSpec.Builder builder)
	{
        builder.comment("World settings").push("server");
        checkTags = builder
        		.comment("Check for and notify of mismatching tags. Only works when the same option in the common config is true.")
        		.define("check_tags", true);
	}
}