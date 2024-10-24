package com.firemerald.additionalplacements.client.gui.screen;

import com.firemerald.additionalplacements.client.gui.ConnectionErrorsList;
import com.firemerald.additionalplacements.util.MessageTree;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.Font;
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
        okButton = new Button(10, height - 30, width - 20, 20, CommonComponents.GUI_ACKNOWLEDGE, (button) -> {
        	minecraft.setScreen(nextScreen);
        });
        addWidget(errorList);
        addWidget(okButton);
        setFocused(errorList);
    }

    public Font getFont() {
        return font;
    }


    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        renderBackground(poseStack);
        errorList.render(poseStack, mouseX, mouseY, partialTick);
        okButton.render(poseStack, mouseX, mouseY, partialTick);
    }
}
