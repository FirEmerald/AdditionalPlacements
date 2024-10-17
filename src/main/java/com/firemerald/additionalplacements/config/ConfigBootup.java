package com.firemerald.additionalplacements.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.electronwill.nightconfig.core.ConfigFormat;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.firemerald.additionalplacements.generation.Registration;

import net.neoforged.neoforge.common.ModConfigSpec;

import com.firemerald.additionalplacements.generation.GenerationType;

public class ConfigBootup {
	public ConfigBootup(ModConfigSpec.Builder builder) {
        builder.comment("Bootup settings").push("bootup");
        Registration.buildConfig(builder, GenerationType::buildBootupConfig);
	}
	
	public void loadValues() {
		APConfigs.sendConfigEvent(GenerationType::onBootupConfigLoaded);
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
		loadValues();
		config.close();
	}

    private boolean setupConfigFile(final Path file, final ConfigFormat<?> conf) throws IOException {
        Files.createFile(file);
        conf.initEmptyFile(file);
        return true;
    }
}
