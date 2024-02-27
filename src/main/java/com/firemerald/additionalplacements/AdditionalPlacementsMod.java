package com.firemerald.additionalplacements;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firemerald.additionalplacements.client.ConfigClient;
import com.firemerald.additionalplacements.common.ConfigCommon;
import com.firemerald.additionalplacements.common.ConfigServer;
import com.firemerald.additionalplacements.common.TagMismatchChecker;
import com.firemerald.additionalplacements.network.APNetwork;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public class AdditionalPlacementsMod implements ModInitializer
{
	//TODO: fences walls panes bars
	public static final String MOD_ID = "additionalplacements";
    public static final Logger LOGGER = LoggerFactory.getLogger("Additional Placements");

    public static final ForgeConfigSpec commonSpec, serverSpec, clientSpec;
    public static final ConfigCommon COMMON_CONFIG;
    public static final ConfigServer SERVER_CONFIG;
    public static final ConfigClient CLIENT_CONFIG;
    static {
        final Pair<ConfigCommon, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(ConfigCommon::new);
        commonSpec = commonSpecPair.getRight();
        COMMON_CONFIG = commonSpecPair.getLeft();
        final Pair<ConfigServer, ForgeConfigSpec> serverSpecPair = new ForgeConfigSpec.Builder().configure(ConfigServer::new);
        serverSpec = serverSpecPair.getRight();
        SERVER_CONFIG = serverSpecPair.getLeft();
        final Pair<ConfigClient, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(ConfigClient::new);
        clientSpec = clientSpecPair.getRight();
        CLIENT_CONFIG = clientSpecPair.getLeft();
    }

    public static boolean dynamicRegistration = false;

    public AdditionalPlacementsMod()
    {
        ModLoadingContext.registerConfig(MOD_ID, ModConfig.Type.COMMON, commonSpec);
        ModLoadingContext.registerConfig(MOD_ID, ModConfig.Type.SERVER, serverSpec);
        ModLoadingContext.registerConfig(MOD_ID, ModConfig.Type.CLIENT, clientSpec);
    }

    @Override
    public void onInitialize()
    {
    	ServerTickEvents.END_SERVER_TICK.register(TagMismatchChecker::onServerTickEnd);
    	APNetwork.register();
    }
}
