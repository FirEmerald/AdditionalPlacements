package com.firemerald.additionalplacements.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.electronwill.nightconfig.core.ConfigFormat;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.electronwill.nightconfig.toml.TomlFormat;
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

	public void loadConfig(Path configPath, ModConfigSpec spec) {
        final CommentedFileConfig config = CommentedFileConfig.builder(configPath, TomlFormat.instance())
        		.sync()
                .preserveInsertionOrder()
                .onFileNotFound(this::setupConfigFile)
                .writingMode(WritingMode.REPLACE)
                .build();
        config.load();
		spec.acceptConfig(config);
		onConfigLoaded();
		config.close();
	}

    private boolean setupConfigFile(final Path file, final ConfigFormat<?> conf) throws IOException {
        Files.createFile(file);
        conf.initEmptyFile(file);
        return true;
    }

	public void onConfigLoaded() {
		enabled.loadListsFromConfig();
		Registration.forEach(GenerationType::onStartupConfigLoaded);
	}
}
