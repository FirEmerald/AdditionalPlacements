package com.firemerald.additionalplacements;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firemerald.additionalplacements.common.ConfigCommon;
import com.firemerald.additionalplacements.common.ConfigServer;
import com.firemerald.additionalplacements.common.TagMismatchChecker;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import fuzs.forgeconfigapiport.impl.core.CommonAbstractions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;

public class AdditionalPlacementsMod implements ModInitializer
{
	//TODO: fences walls panes bars
	public static final String MOD_ID = "additionalplacements";
    public static final Logger LOGGER = LoggerFactory.getLogger("Additional Placements");

    static final ForgeConfigSpec commonSpec;
	public static final ForgeConfigSpec serverSpec;
    public static final ConfigCommon COMMON_CONFIG;
    public static final ConfigServer SERVER_CONFIG;
    static {
        final Pair<ConfigCommon, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(ConfigCommon::new);
        commonSpec = commonSpecPair.getRight();
        COMMON_CONFIG = commonSpecPair.getLeft();
        final Pair<ConfigServer, ForgeConfigSpec> serverSpecPair = new ForgeConfigSpec.Builder().configure(ConfigServer::new);
        serverSpec = serverSpecPair.getRight();
        SERVER_CONFIG = serverSpecPair.getLeft();
    }
    
    public static boolean dynamicRegistration = false;

    public void onInitialize()
    {
    	ServerTickEvents.END_SERVER_TICK.register(TagMismatchChecker::onServerTickEnd);
    	ForgeConfigRegistry.INSTANCE.register(MOD_ID, ModConfig.Type.COMMON, commonSpec);
    	ForgeConfigRegistry.INSTANCE.register(MOD_ID, ModConfig.Type.SERVER, serverSpec);
		try
		{
			LOGGER.info("Attempting to manually load Additional Placements config early.");
			Field f = ConfigTracker.class.getDeclaredField("configsByMod");
	        f.setAccessible(true);
	        @SuppressWarnings("unchecked")
	        Map<String, Map<ModConfig.Type, Collection<ModConfig>>> configsByMod = (Map<String, Map<Type, Collection<ModConfig>>>) f.get(ConfigTracker.INSTANCE);
	        Method m = ConfigTracker.class.getDeclaredMethod("openConfig", ModConfig.class, Path.class);
	        m.setAccessible(true);
	        m.invoke(ConfigTracker.INSTANCE, configsByMod.get(MOD_ID).get(ModConfig.Type.COMMON).iterator().next(), CommonAbstractions.INSTANCE.getCommonConfigDirectory());
			LOGGER.info("manual config load successful.");
		}
		catch (NoSuchFieldException | SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			LOGGER.error("Failed to load only Additional Placements config. Automatic block registrations settings will not be applied.", e);
		}
        //LOGGER.warn("During block registration you may recieve several reports of \"Potentially Dangerous alternative prefix `additionalplacements`\". Ignore these, they are intended.");
    }
}
