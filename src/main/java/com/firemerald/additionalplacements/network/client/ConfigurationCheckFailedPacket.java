package com.firemerald.additionalplacements.network.client;

import java.util.List;

import org.apache.commons.lang3.tuple.Triple;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.client.gui.screen.ConnectionErrorsScreen;
import com.firemerald.additionalplacements.util.MessageTree;
import com.mojang.realmsclient.RealmsMainScreen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking.Context;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ConfigurationCheckFailedPacket extends ClientConfigurationPacket
{
	public static final Type<ConfigurationCheckFailedPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(AdditionalPlacementsMod.MOD_ID, "configuration_check_failed"));

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
	public Type<ConfigurationCheckFailedPacket> type()
	{
		return TYPE;
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
	@Environment(EnvType.CLIENT)
	public void handleClient(Context context) {
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
		//TODO move disconnect to a point that works?
		Minecraft client = context.client();
		client.execute(new HandleErrors(client, rootError));
	}

	@Environment(EnvType.CLIENT)
	static class HandleErrors implements Runnable {
		final MessageTree rootError;
		final Minecraft client;

		HandleErrors(Minecraft client, MessageTree rootError) {
			this.client = client;
			this.rootError = rootError;
		}

		@Override
		public void run() {
			Screen desScreen = new TitleScreen();
			if (!client.isLocalServer()) {
				if (client.getCurrentServer().isRealm()) {
					desScreen = new RealmsMainScreen(desScreen);
				} else {
					desScreen = new JoinMultiplayerScreen(desScreen);
				}
			}
			client.disconnect(new ConnectionErrorsScreen(rootError, desScreen));
		}
	}
}