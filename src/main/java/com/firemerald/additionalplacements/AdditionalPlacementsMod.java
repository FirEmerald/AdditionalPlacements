package com.firemerald.additionalplacements;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firemerald.additionalplacements.client.ConfigClient;
import com.firemerald.additionalplacements.common.ConfigCommon;
import com.firemerald.additionalplacements.common.ConfigServer;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ConfigTracker;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.ModConfigSpec;

@Mod(AdditionalPlacementsMod.MOD_ID)
public class AdditionalPlacementsMod
{
	//TODO: fences walls panes bars
	public static final String MOD_ID = "additionalplacements";
    public static final Logger LOGGER = LoggerFactory.getLogger("Additional Placements");

    public static final ModConfigSpec commonSpec, serverSpec, clientSpec;
    public static final ConfigCommon COMMON_CONFIG;
    public static final ConfigServer SERVER_CONFIG;
    public static final ConfigClient CLIENT_CONFIG;
    static {
        final Pair<ConfigCommon, ModConfigSpec> commonSpecPair = new ModConfigSpec.Builder().configure(ConfigCommon::new);
        commonSpec = commonSpecPair.getRight();
        COMMON_CONFIG = commonSpecPair.getLeft();
        final Pair<ConfigServer, ModConfigSpec> serverSpecPair = new ModConfigSpec.Builder().configure(ConfigServer::new);
        serverSpec = serverSpecPair.getRight();
        SERVER_CONFIG = serverSpecPair.getLeft();
        final Pair<ConfigClient, ModConfigSpec> clientSpecPair = new ModConfigSpec.Builder().configure(ConfigClient::new);
        clientSpec = clientSpecPair.getRight();
        CLIENT_CONFIG = clientSpecPair.getLeft();
    }

    public static boolean dynamicRegistration = false;

    public AdditionalPlacementsMod(IEventBus bus)
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, commonSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, serverSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, clientSpec);
		try
		{
			LOGGER.info("Attempting to manually load Additional Placements config early.");
			Field f = ConfigTracker.class.getDeclaredField("configsByMod");
	        f.setAccessible(true);
	        @SuppressWarnings("unchecked")
			ConcurrentHashMap<String, Map<ModConfig.Type, ModConfig>> configsByMod = (ConcurrentHashMap<String, Map<ModConfig.Type, ModConfig>>) f.get(ConfigTracker.INSTANCE);
	        Method m = ConfigTracker.class.getDeclaredMethod("openConfig", ModConfig.class, Path.class, Path.class);
	        m.setAccessible(true);
	        m.invoke(ConfigTracker.INSTANCE, configsByMod.get(MOD_ID).get(ModConfig.Type.COMMON), FMLPaths.CONFIGDIR.get(), null);
			LOGGER.info("manual config load successful.");
		}
		catch (NoSuchFieldException | SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			LOGGER.error("Failed to load only Additional Placements config. Automatic block registrations settings will not be applied.", e);
		}
        LOGGER.warn("During block registration you may recieve several reports of \"Potentially Dangerous alternative prefix `additionalplacements`\". Ignore these, they are intended.");
    }
}
