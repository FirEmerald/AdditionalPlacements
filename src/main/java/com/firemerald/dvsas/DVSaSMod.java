package com.firemerald.dvsas;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firemerald.dvsas.common.ConfigCommon;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
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
    
    public static boolean  dynamicRegistration = false;

    public DVSaSMod()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, commonSpec);
		try
		{
			LOGGER.info("Attempting to manually load DVSaS config early.");
			Field f = ConfigTracker.class.getDeclaredField("configsByMod");
	        f.setAccessible(true);
	        @SuppressWarnings("unchecked")
			ConcurrentHashMap<String, Map<ModConfig.Type, ModConfig>> configsByMod = (ConcurrentHashMap<String, Map<Type, ModConfig>>) f.get(ConfigTracker.INSTANCE);
	        Method m = ConfigTracker.class.getDeclaredMethod("openConfig", ModConfig.class, Path.class);
	        m.setAccessible(true);
	        m.invoke(ConfigTracker.INSTANCE, configsByMod.get(MOD_ID).get(ModConfig.Type.COMMON), FMLPaths.CONFIGDIR.get());
			LOGGER.info("manual config load successful.");
		}
		catch (NoSuchFieldException | SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			LOGGER.error("Failed to load only DVSaS config. Automatic block registrations settings will not be applied.", e);
		}
        LOGGER.warn("During block registration you may recieve several reports of \"Mod `<mod>` attempting to register `<block>` to the namespace `dvsas`\". Ignore these, they are intended.");
    }
}
