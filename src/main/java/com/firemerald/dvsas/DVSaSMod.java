package com.firemerald.dvsas;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firemerald.dvsas.common.ConfigCommon;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(DVSaSMod.MOD_ID)
public class DVSaSMod
{
	public static final String MOD_ID = "dvsas";
    public static final Logger LOGGER = LoggerFactory.getLogger("DVSaS");

    static final ForgeConfigSpec commonSpec;
    public static final ConfigCommon COMMON_CONFIG;
    static {
        final Pair<ConfigCommon, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(ConfigCommon::new);
        commonSpec = clientSpecPair.getRight();
        COMMON_CONFIG = clientSpecPair.getLeft();
    }

    public DVSaSMod()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, commonSpec);
        ConfigTracker.INSTANCE.loadConfigs(ModConfig.Type.COMMON, FMLPaths.CONFIGDIR.get()); //hacky method to ensure config is loaded
    }
}
