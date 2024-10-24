package com.firemerald.additionalplacements.client.gui.screen;

import com.firemerald.additionalplacements.client.gui.ConnectionErrorsList;
import com.firemerald.additionalplacements.util.MessageTree;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ConnectionErrorsScreen extends Screen {
	private final MessageTree rootError;
	private final Screen nextScreen;
	private final boolean wasSinglePlayer;
    private ConnectionErrorsList errorList;
    private Button okButton;
    
	public ConnectionErrorsScreen(MessageTree rootError, Screen nextScreen, boolean wasSinglePlayer) {
		super(Component.translatable("msg.additionalplacements.configurationerrors"));
		this.rootError = rootError;
		this.nextScreen = nextScreen;
		this.wasSinglePlayer = wasSinglePlayer;
	}

    @Override
    public void init() {
        super.init();
        clearWidgets();
        errorList = new ConnectionErrorsList(this, 10, 10, width - 20, height - 40, rootError, wasSinglePlayer);
        okButton = Button.builder(CommonComponents.GUI_OK, (button) -> {
        	minecraft.setScreen(nextScreen);
        }).bounds(10, height - 30, width - 20, 20).build();
        addWidget(errorList);
        addWidget(okButton);
        setFocused(errorList);
    }

    public Font getFont() {
        return font;
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        errorList.render(guiGraphics, mouseX, mouseY, partialTick);
        okButton.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}
