package com.firemerald.additionalplacements;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.firemerald.additionalplacements.networking.Network;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firemerald.additionalplacements.common.ConfigCommon;
import com.firemerald.additionalplacements.common.ConfigServer;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(AdditionalPlacementsMod.MOD_ID)
public class AdditionalPlacementsMod
{
	//TODO: fences walls panes bars
	public static final String MOD_ID = "additionalplacements";
	public static final String OLD_ID = "dvsas";
    public static final Logger LOGGER = LoggerFactory.getLogger("Additional Placements");
    public static final String AP_TOGGLE = "ap_toggle";

    public static final ForgeConfigSpec commonSpec, serverSpec;
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

    public AdditionalPlacementsMod()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, commonSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, serverSpec);

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::networkRegistry);

		try
		{
			LOGGER.info("Attempting to manually load Additional Placements config early.");
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
			LOGGER.error("Failed to load only Additional Placements config. Automatic block registrations settings will not be applied.", e);
		}
        LOGGER.warn("During block registration you may recieve several reports of \"Potentially Dangerous alternative prefix `additionalplacements`\". Ignore these, they are intended.");
    }

    private void networkRegistry(final FMLCommonSetupEvent event)
    {
        Network.register();
    }
}
