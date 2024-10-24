package com.firemerald.additionalplacements.config;

import java.util.function.Consumer;

import org.apache.commons.lang3.tuple.Pair;

import com.firemerald.additionalplacements.generation.GenerationType;
import com.firemerald.additionalplacements.generation.Registration;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class APConfigs {
    public static final ConfigBootup BOOTUP;
    public static final ForgeConfigSpec BOOTUP_SPEC;
    public static final ConfigCommon COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final ConfigServer SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final ConfigClient CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;
    
    static {
        final Pair<ConfigBootup, ForgeConfigSpec> bootupSpecPair = new ForgeConfigSpec.Builder().configure(ConfigBootup::new);
        BOOTUP = bootupSpecPair.getLeft();
        BOOTUP_SPEC = bootupSpecPair.getRight();
        final Pair<ConfigCommon, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(ConfigCommon::new);
        COMMON = commonSpecPair.getLeft();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC = commonSpecPair.getRight());
        final Pair<ConfigServer, ForgeConfigSpec> serverSpecPair = new ForgeConfigSpec.Builder().configure(ConfigServer::new);
        SERVER = serverSpecPair.getLeft();
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_SPEC = serverSpecPair.getRight());
        final Pair<ConfigClient, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(ConfigClient::new);
        CLIENT = clientSpecPair.getLeft();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC = clientSpecPair.getRight());
    }
    
    public static void init() {}
    
    public static void onModConfigsLoaded(ModConfigEvent.Loading event) {
    	if (event.getConfig().getSpec() == COMMON_SPEC) sendConfigEvent(GenerationType::onCommonConfigLoaded);
    	else if (event.getConfig().getSpec() == SERVER_SPEC) sendConfigEvent(GenerationType::onServerConfigLoaded);
    	else if (event.getConfig().getSpec() == CLIENT_SPEC) sendConfigEvent(GenerationType::onClientConfigLoaded);
    }
    
    public static void onModConfigsReloaded(ModConfigEvent.Reloading event) {
    	if (event.getConfig().getSpec() == COMMON_SPEC) sendConfigEvent(GenerationType::onCommonConfigReloaded);
    	else if (event.getConfig().getSpec() == SERVER_SPEC) sendConfigEvent(GenerationType::onServerConfigReloaded);
    	else if (event.getConfig().getSpec() == CLIENT_SPEC) sendConfigEvent(GenerationType::onClientConfigReloaded);
    }
    
    public static void sendConfigEvent(Consumer<GenerationType<?, ?>> action) {
    	Registration.forEach((name, type) -> action.accept(type));
    }
}