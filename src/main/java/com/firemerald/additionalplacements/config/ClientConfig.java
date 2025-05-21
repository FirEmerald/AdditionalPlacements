package com.firemerald.additionalplacements.config;

import com.firemerald.additionalplacements.generation.GenerationType;
import com.firemerald.additionalplacements.generation.Registration;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import net.neoforged.neoforge.common.ModConfigSpec.LongValue;

public class ClientConfig
{
	public final BooleanValue defaultPlacementLogicState, loginPlacementLogicStateMessage, togglePlacementLogicStateMessage, enablePlacementHighlight;
	public final LongValue toggleQuickpressTime;
	public final ConfigValue<String> gridColor, previewColor;
	private float[] gridColorVal = {.4f, 0, 0, 0}, previewColorVal = {.4f, 1, 1, 1};

	public ClientConfig(ModConfigSpec.Builder builder)
	{
        builder.comment("Client settings").push("client");
        defaultPlacementLogicState = builder
        		.comment("Default enabled state for Additional Placement placement logic. Please note that this value takes effect any time you load a world or log in to a server.")
        		.define("default_placement_logic_state", true);
        loginPlacementLogicStateMessage = builder
        		.comment("Whether to display the logic placement enabled state when first initially loading up a world or logging in to a server. This does NOT stop it from showing when toggled via the keybind later.")
        		.define("login_placement_logic_state_message", true);
        togglePlacementLogicStateMessage = builder
        		.comment("Whether to display the logic placement enabled state when toggled with the keybind. This does NOT stop it from showing when you initially load up a world or log in to a server.")
        		.define("toggle_placement_logic_state_message", true);
        toggleQuickpressTime = builder
        		.comment("The length of time in milliseconds for which the placement toggle key must be held for it to automatically return to the previous state when the key is released. setting to 0 turns the key into hold only, setting it to a high value (such as 1000000) will make it generally behave as always a toggle")
        		.defineInRange("toggle_quickpress_time", 500L, 0, Long.MAX_VALUE);
        enablePlacementHighlight = builder
        		.comment("Whether to enable the rendering of the placement grid and/or preview.")
        		.define("enable_placement_highlights", true);
        gridColor = builder
        		.comment("The color of the placement grid, in AARRGGBB hex format.")
        		.define("grid_color", "66000000", APConfigs::isColorString);
        previewColor = builder
        		.comment("The color of the placement preview (currently used in stairs without mixed placement), in AARRGGBB hex format.")
        		.define("preview_color", "66FFFFFF", APConfigs::isColorString);
        Registration.buildConfig(builder, GenerationType::buildClientConfig);
	}

	public void onConfigLoaded() {
		gridColorVal = APConfigs.parseColorString(gridColor);
		previewColorVal = APConfigs.parseColorString(previewColor);
		Registration.forEach(GenerationType::onClientConfigLoaded);
	}

	public float[] gridColor() {
		return gridColorVal;
	}

	public float[] previewColor() {
		return previewColorVal;
	}
}