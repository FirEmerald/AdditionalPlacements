package com.firemerald.additionalplacements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firemerald.additionalplacements.config.APConfigs;
import com.firemerald.additionalplacements.generation.APGenerationTypes;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(AdditionalPlacementsMod.MOD_ID)
public class AdditionalPlacementsMod
{
	//TODO: fences walls panes bars
	public static final String MOD_ID = "additionalplacements";
    public static final Logger LOGGER = LoggerFactory.getLogger("Additional Placements");

    public static boolean dynamicRegistration = false;

    public AdditionalPlacementsMod(IEventBus bus)
    {
        APGenerationTypes.init();
        bus.addListener(APConfigs::onModConfigsLoaded);
        bus.addListener(APConfigs::onModConfigsReloaded);
        LOGGER.warn("During block registration you may recieve several reports of \"Potentially Dangerous alternative prefix `additionalplacements`\". Ignore these, they are intended.");
    }
}
