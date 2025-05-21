package com.firemerald.additionalplacements.client.gui.screen;

import com.firemerald.additionalplacements.client.gui.ConnectionErrorsList;
import com.firemerald.additionalplacements.util.MessageTree;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;

public class ConnectionErrorsScreen extends Screen {
	private final MessageTree rootError;
	private final Screen nextScreen;
	private final boolean wasSinglePlayer;
    private ConnectionErrorsList errorList;
    private Button okButton;
    
	public ConnectionErrorsScreen(MessageTree rootError, Screen nextScreen, boolean wasSinglePlayer) {
		super(new TranslationTextComponent("msg.additionalplacements.configurationerrors"));
		this.rootError = rootError;
		this.nextScreen = nextScreen;
		this.wasSinglePlayer = wasSinglePlayer;
	}

    @Override
    public void init() {
        super.init();
        errorList = new ConnectionErrorsList(this, 10, 10, width - 20, height - 40, rootError, wasSinglePlayer);
        int buttonWidth = Math.min(200, width - 20);
        okButton = new Button((width - buttonWidth) / 2, height - 30, buttonWidth, 20, DialogTexts.GUI_DONE, (button) -> minecraft.setScreen(nextScreen));
        addWidget(errorList);
        addWidget(okButton);
        setFocused(errorList);
    }

    public FontRenderer getFont() {
        return font;
    }


    @Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTick) {
        renderBackground(poseStack);
        errorList.render(poseStack, mouseX, mouseY, partialTick);
        okButton.render(poseStack, mouseX, mouseY, partialTick);
    }
}
