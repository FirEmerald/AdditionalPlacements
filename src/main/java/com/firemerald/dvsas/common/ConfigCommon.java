package com.firemerald.dvsas.common;

import java.util.Collections;
import java.util.List;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class ConfigCommon
{
	public final BooleanValue showTooltip;
	public final ConfigValue<List<String>> blacklist;

	@SuppressWarnings("unchecked")
	public ConfigCommon(ForgeConfigSpec.Builder builder)
	{
        builder.comment("Common settings").push("common");
        showTooltip = builder
        		.comment("Show tooltip when a block can be placed vertically")
        		.define("tooltip", true);
        blacklist = (ConfigValue<List<String>>) (Object) builder
        		.comment("Blacklist for blocks to not have generated vertical variants")
        		.defineList("blacklist", Collections.emptyList(), o -> {
        			return o instanceof String;
        		});
	}
}