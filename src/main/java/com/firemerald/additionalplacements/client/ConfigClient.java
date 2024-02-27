package com.firemerald.additionalplacements.client;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class ConfigClient
{
	public final BooleanValue defaultPlacementLogicState;

	public ConfigClient(ForgeConfigSpec.Builder builder)
	{
        builder.comment("Client settings").push("client");
        defaultPlacementLogicState = builder
        		.comment("Default enabled state for Additional Placement placement logic. Please note that this value takes effect any time you load a world or log in to a server.")
        		.define("default_placement_logic_state", true);
	}
}