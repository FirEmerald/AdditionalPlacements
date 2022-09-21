package com.firemerald.dvsas.common;

import java.util.Collections;
import java.util.List;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class ConfigCommon
{
	public final BooleanValue generateSlabs;
	public final BooleanValue generateStairs;
	public final BooleanValue disableAutomaticSlabPlacement;
	public final BooleanValue disableAutomaticStairPlacement;
	public final BooleanValue showTooltip;
	public final ConfigValue<List<String>> blacklist;

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
        disableAutomaticSlabPlacement = builder
        		.comment("Disable automatic vertical slab placement. Use if a mod E.G. DoubleSlabs is conflicting.")
        		.define("disable_slab_placement", false);
        disableAutomaticStairPlacement = builder
        		.comment("Disable automatic vertical stair placement. Use if a mod is conflicting.")
        		.define("disable_stair_placement", false);
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