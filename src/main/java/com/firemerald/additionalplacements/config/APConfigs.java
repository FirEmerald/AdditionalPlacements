package com.firemerald.additionalplacements.config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.loading.FMLPaths;

public class APConfigs {
    private static StartupConfig startup;
    private static ForgeConfigSpec startupSpec;
    private static CommonConfig common;
    private static ForgeConfigSpec commonSpec;
    private static ServerConfig server;
    private static ForgeConfigSpec serverSpec;
    private static ClientConfig client;
    private static ForgeConfigSpec clientSpec;

    public static void init() {
        final Pair<StartupConfig, ForgeConfigSpec> startupSpecPair = new ForgeConfigSpec.Builder().configure(StartupConfig::new);
        startup = startupSpecPair.getLeft();
        startupSpec = startupSpecPair.getRight();
        final Pair<CommonConfig, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        common = commonSpecPair.getLeft();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, commonSpec = commonSpecPair.getRight());
        final Pair<ServerConfig, ForgeConfigSpec> serverSpecPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        server = serverSpecPair.getLeft();
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, serverSpec = serverSpecPair.getRight());
        final Pair<ClientConfig, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        client = clientSpecPair.getLeft();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, clientSpec = clientSpecPair.getRight());

		startup.loadConfig(FMLPaths.CONFIGDIR.get().resolve("additionalplacements-startup.toml"), startupSpecPair.getRight());
    }

    public static StartupConfig startup() {
    	return startup;
    }

    public static boolean startupLoaded() {
    	return startupSpec.isLoaded();
    }

    public static CommonConfig common() {
    	return common;
    }

    public static boolean commonLoaded() {
    	return commonSpec.isLoaded();
    }

    public static ServerConfig server() {
    	return server;
    }

    public static boolean serverLoaded() {
    	return serverSpec.isLoaded();
    }

    public static ClientConfig client() {
    	return client;
    }

    public static boolean clientLoaded() {
    	return clientSpec.isLoaded();
    }

    public static void onModConfigsLoaded(ModConfigEvent.Loading event) {
    	onModConfigsLoaded(event.getConfig().getSpec());
    }

    public static void onModConfigsReloaded(ModConfigEvent.Reloading event) {
    	onModConfigsLoaded(event.getConfig().getSpec());
    }

    private static void onModConfigsLoaded(IConfigSpec<?> configSpec) {
    	if (configSpec == commonSpec) common.onConfigLoaded();
    	else if (configSpec == serverSpec) server.onConfigLoaded();
    	else if (configSpec == clientSpec) client.onConfigLoaded();
    }

	public static boolean isColorString(Object o) {
		if (o instanceof String s) {
            if (s.length() == 8) { //must be 8 characters (AARRGGBB)
				for (int i = 0; i < 8; ++i) {
					char c = s.charAt(i);
                    if ((c < '0' || c > '9') && (c < 'a' || c > 'f') && (c < 'A' || c > 'F')) return false; //only 0-9, a-f, or A-F allowed
				}
				return true;
			}
		}
		return false;
	}

	public static float[] parseColorString(String s) {
		return new float[] {
				Integer.parseInt(s.substring(2, 4), 16) / 255f, //XXRRXXXX
				Integer.parseInt(s.substring(4, 6), 16) / 255f, //XXXXGGXX
				Integer.parseInt(s.substring(6, 8), 16) / 255f, //XXXXXXBB
				Integer.parseInt(s.substring(0, 2), 16) / 255f  //AAXXXXXX
		};
	}

	public static float[] parseColorString(ConfigValue<String> s) {
		return parseColorString(s.get());
	}
}