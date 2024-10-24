package com.firemerald.additionalplacements.network;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.network.client.CheckDataClientPacket;
import com.firemerald.additionalplacements.network.client.ClientLoginPacket;
import com.firemerald.additionalplacements.network.client.ClientPlayPacket;
import com.firemerald.additionalplacements.network.client.ConfigurationCheckFailedPacket;
import com.firemerald.additionalplacements.network.server.CheckDataServerPacket;
import com.firemerald.additionalplacements.network.server.ServerLoginPacket;
import com.firemerald.additionalplacements.network.server.ServerPlayPacket;
import com.firemerald.additionalplacements.network.server.SetPlacementTogglePacket;

import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.*;
import net.minecraftforge.network.NetworkRegistry.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor.PacketTarget;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.network.simple.SimpleChannel.MessageBuilder;

public class APNetwork
{
    public static SimpleChannel INSTANCE;
    public static final String VERSION = "1";

    private static int packetId = 0;
    private static int id()
    {
        return packetId++;
    }

    public static void register()
    {
        INSTANCE = ChannelBuilder
            .named(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, "network"))
            .networkProtocolVersion(() -> VERSION)
            .clientAcceptedVersions(s -> VERSION.equals(s) || NetworkRegistry.ABSENT.toString().equals(s))
            .serverAcceptedVersions(VERSION::equals)
            .simpleChannel();
        registerServerPlayPacket(SetPlacementTogglePacket.class, SetPlacementTogglePacket::new);
        registerClientLoginPacket(CheckDataClientPacket.class, CheckDataClientPacket::new);
        registerServerLoginReplyPacket(CheckDataServerPacket.class, CheckDataServerPacket::new);
        registerClientLoginReplyPacket(ConfigurationCheckFailedPacket.class, ConfigurationCheckFailedPacket::new);
    }

    public static <T extends ClientPlayPacket> void registerClientPlayPacket(Class<T> clazz, Function<FriendlyByteBuf, T> decoder)
    {
    	registerPlayPacket(clazz, decoder, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <T extends ServerPlayPacket> void registerServerPlayPacket(Class<T> clazz, Function<FriendlyByteBuf, T> decoder)
    {
    	registerPlayPacket(clazz, decoder, NetworkDirection.PLAY_TO_SERVER);
    }

    public static <T extends APPacket> void registerPlayPacket(Class<T> clazz, Function<FriendlyByteBuf, T> decoder, NetworkDirection direction)
    {
    	registerPlayPacket(INSTANCE.messageBuilder(clazz, id(), direction), decoder);
    }

    public static <T extends APPacket> void registerPlayPacket(MessageBuilder<T> builder, Function<FriendlyByteBuf, T> decoder)
    {
    	builder
    	.decoder(decoder)
    	.encoder(APPacket::write)
    	.consumerMainThread(APPacket::handle)
    	.add();
    }

    public static <T extends ClientPlayPacket> void registerClientPlayPacketAsync(Class<T> clazz, Function<FriendlyByteBuf, T> decoder)
    {
    	registerPlayPacketAsync(clazz, decoder, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <T extends ServerPlayPacket> void registerServerPlayPacketAsync(Class<T> clazz, Function<FriendlyByteBuf, T> decoder)
    {
    	registerPlayPacketAsync(clazz, decoder, NetworkDirection.PLAY_TO_SERVER);
    }

    public static <T extends APPacket> void registerPlayPacketAsync(Class<T> clazz, Function<FriendlyByteBuf, T> decoder, NetworkDirection direction)
    {
    	registerPlayPacketAsync(INSTANCE.messageBuilder(clazz, id(), direction), decoder);
    }

    public static <T extends APPacket> void registerPlayPacketAsync(MessageBuilder<T> builder, Function<FriendlyByteBuf, T> decoder)
    {
    	builder
    	.decoder(decoder)
    	.encoder(APPacket::write)
    	.consumerNetworkThread((BiConsumer<T, Supplier<NetworkEvent.Context>>) APPacket::handle)
    	.add();
    }

    public static <T extends ClientLoginPacket> void registerClientLoginPacket(Class<T> clazz, Function<FriendlyByteBuf, T> decoder)
    {
    	registerClientLoginPacket(INSTANCE.messageBuilder(clazz, id(), NetworkDirection.LOGIN_TO_CLIENT), decoder);
    }

    public static <T extends ClientLoginPacket> void registerClientLoginPacket(MessageBuilder<T> builder, Function<FriendlyByteBuf, T> decoder)
    {
    	builder
    	.loginIndex(AbstractLoginPacket::getLoginIndex, AbstractLoginPacket::setLoginIndex)
    	.decoder(decoder)
    	.encoder(APPacket::write)
        .markAsLoginPacket()
    	.consumerNetworkThread(HandshakeHandler.biConsumerFor(AbstractLoginPacket::handle))
    	.add();
    }

    public static <T extends ClientLoginPacket> void registerClientLoginReplyPacket(Class<T> clazz, Function<FriendlyByteBuf, T> decoder)
    {
    	registerClientLoginReplyPacket(INSTANCE.messageBuilder(clazz, id(), NetworkDirection.LOGIN_TO_CLIENT), decoder);
    }

    public static <T extends ClientLoginPacket> void registerClientLoginReplyPacket(MessageBuilder<T> builder, Function<FriendlyByteBuf, T> decoder)
    {
    	builder
    	.loginIndex(AbstractLoginPacket::getLoginIndex, AbstractLoginPacket::setLoginIndex)
    	.decoder(decoder)
    	.encoder(APPacket::write)
    	.consumerNetworkThread(HandshakeHandler.biConsumerFor(AbstractLoginPacket::handle))
    	.add();
    }

    public static <T extends ServerLoginPacket> void registerServerLoginReplyPacket(Class<T> clazz, Function<FriendlyByteBuf, T> decoder)
    {
    	registerServerLoginReplyPacket(INSTANCE.messageBuilder(clazz, id(), NetworkDirection.LOGIN_TO_SERVER), decoder);
    }

    public static <T extends ServerLoginPacket> void registerServerLoginReplyPacket(MessageBuilder<T> builder, Function<FriendlyByteBuf, T> decoder)
    {
    	builder
    	.loginIndex(AbstractLoginPacket::getLoginIndex, AbstractLoginPacket::setLoginIndex)
    	.decoder(decoder)
    	.encoder(APPacket::write)
    	.consumerNetworkThread(HandshakeHandler.indexFirst(AbstractLoginPacket::handle))
    	.add();
    }

    public static void sendToServer(Object packet)
    {
    	sendTo(packet, PacketDistributor.SERVER.noArg());
    }

    public static void sendToClient(Object packet, ServerPlayer player)
    {
    	sendTo(packet, PacketDistributor.PLAYER.with(() -> player));
    }

    public static void sendToAllClients(Object packet)
    {
    	sendTo(packet, PacketDistributor.ALL.noArg());
    }

    public static void sendTo(Object packet, PacketTarget target)
    {
    	INSTANCE.send(target, packet);
    }

    public static void send(Object packet, Connection connection, NetworkDirection direction)
    {
    	INSTANCE.sendTo(packet, connection, direction);
    }

    public static void reply(Object packet, NetworkEvent.Context context) {
    	INSTANCE.reply(packet, context);
    }
}