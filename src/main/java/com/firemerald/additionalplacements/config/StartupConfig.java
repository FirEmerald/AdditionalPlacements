package com.firemerald.additionalplacements.config;

import com.firemerald.additionalplacements.config.blocklist.Blocklist;
import com.firemerald.additionalplacements.generation.GenerationType;
import com.firemerald.additionalplacements.generation.Registration;

import net.neoforged.neoforge.common.ModConfigSpec;

public class StartupConfig {
	public final Blocklist enabled = new Blocklist(false, true);

	public StartupConfig(ModConfigSpec.Builder builder) {
        builder.comment("Startup settings").push("startup");
		enabled.addToConfig(builder, "enabled", "Blocklist for controlling which blocks can generate variants of a their type (if one exists).");
        Registration.buildConfig(builder, GenerationType::buildStartupConfig);
	}

	public void onConfigLoaded() {
		enabled.loadListsFromConfig();
		Registration.forEach(GenerationType::onStartupConfigLoaded);
	}
}
