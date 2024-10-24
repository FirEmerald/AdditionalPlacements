package com.firemerald.additionalplacements.network;

import java.util.List;

import org.apache.commons.lang3.tuple.Triple;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.client.gui.screen.ConnectionErrorsScreen;
import com.firemerald.additionalplacements.util.MessageTree;
import com.mojang.realmsclient.RealmsMainScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.ConfigurationPayloadContext;

public class ConfigurationCheckFailedPacket extends ClientPacket<ConfigurationPayloadContext>
{
	public static final ResourceLocation ID = new ResourceLocation(AdditionalPlacementsMod.MOD_ID, "configuration_check_failed");
	
	private final List<Triple<ResourceLocation, List<MessageTree>, List<MessageTree>>> compiledErrors;
	
	public ConfigurationCheckFailedPacket(List<Triple<ResourceLocation, List<MessageTree>, List<MessageTree>>> compiledErrors) {
		this.compiledErrors = compiledErrors;
	}

	public ConfigurationCheckFailedPacket(FriendlyByteBuf buf)
	{
		compiledErrors = buf.readList(buf2 -> {
			ResourceLocation id = buf2.readResourceLocation();
			List<MessageTree> clientErrors = buf2.readList(MessageTree::new);
			List<MessageTree> serverErrors = buf2.readList(MessageTree::new);
			return Triple.of(id, clientErrors, serverErrors);
		});
	}

	@Override
	public ResourceLocation id()
	{
		return ID;
	}

	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeCollection(compiledErrors, (buf2, data) -> {
			buf2.writeResourceLocation(data.getLeft());
			buf2.writeCollection(data.getMiddle(), MessageTree::write);
			buf2.writeCollection(data.getRight(), MessageTree::write);
		});
	}

	@Override
	public void handleClient(ConfigurationPayloadContext context)
	{
		MessageTree rootError = new MessageTree(Component.translatable("msg.additionalplacements.errors.type"));
		compiledErrors.forEach(data -> {
			MessageTree typeError = new MessageTree(Component.literal(data.getLeft().toString())); //TODO friendly name?
			List<MessageTree> clientErrors = data.getMiddle();
			if (!clientErrors.isEmpty()) {
				MessageTree clientError = new MessageTree(Component.translatable("msg.additionalplacements.errors.client"), clientErrors);
				typeError.children.add(clientError);
			}
			List<MessageTree> serverErrors = data.getRight();
			if (!serverErrors.isEmpty()) {
				MessageTree serverError = new MessageTree(Component.translatable("msg.additionalplacements.errors.server"), serverErrors);
				typeError.children.add(serverError);
			}
			rootError.children.add(typeError);
		});
		rootError.output(AdditionalPlacementsMod.LOGGER::warn);
		context.workHandler().submitAsync(() -> {
			Minecraft minecraft = Minecraft.getInstance();
			Screen desScreen = new TitleScreen();
			if (!minecraft.isLocalServer()) {
				if (minecraft.getCurrentServer().isRealm()) {
					desScreen = new RealmsMainScreen(desScreen);
				} else {
					desScreen = new JoinMultiplayerScreen(desScreen);
				}
			}
			minecraft.disconnect(new ConnectionErrorsScreen(rootError, desScreen));
		});
	}
}