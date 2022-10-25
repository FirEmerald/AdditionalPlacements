package com.firemerald.additionalplacements.common;

import java.util.Collections;
import java.util.List;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

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
        blacklist = (ConfigValue<List<String>>) (Object) builder
        		.comment("Blacklist for blocks to not have generated placement variants")
        		.defineList("blacklist", Collections.emptyList(), o -> {
        			return o instanceof String;
        		});
	}
}