package com.firemerald.additionalplacements.client.gui.screen;

import com.firemerald.additionalplacements.client.gui.ConnectionErrorsList;
import com.firemerald.additionalplacements.util.MessageTree;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ConnectionErrorsScreen extends Screen {
	private final MessageTree rootError;
    private ConnectionErrorsList errorList;
    private Button okButton;
    
	public ConnectionErrorsScreen(MessageTree rootError) {
		super(Component.translatable("msg.additionalplacements.configurationerrors"));
		this.rootError = rootError;
	}

    @Override
    public void init() {
        super.init();
        clearWidgets();
        errorList = new ConnectionErrorsList(this, 10, 10, width - 20, height - 40, rootError);
        okButton = Button.builder(CommonComponents.GUI_OK, (button) -> {
        	minecraft.setScreen(new TitleScreen()); //TODO previous screen
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
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        errorList.render(guiGraphics, mouseX, mouseY, partialTick);
        okButton.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}
