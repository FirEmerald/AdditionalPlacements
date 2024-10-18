package com.firemerald.additionalplacements.network;

import java.util.function.Function;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;

import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.NetworkRegistry.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
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
        registerServerPacket(SetPlacementTogglePacket.class, SetPlacementTogglePacket::new);
        registerClientPacketAsync(CheckDataClientPacket.class, CheckDataClientPacket::new);
        registerServerPacketAsync(CheckDataServerPacket.class, CheckDataServerPacket::new);
        registerClientPacketAsync(ConfigurationCheckFailedPacket.class, ConfigurationCheckFailedPacket::new);
    }

    public static <T extends ClientPacket> void registerClientPacket(Class<T> clazz, Function<FriendlyByteBuf, T> decoder)
    {
    	registerPacket(clazz, decoder, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <T extends ServerPacket> void registerServerPacket(Class<T> clazz, Function<FriendlyByteBuf, T> decoder)
    {
    	registerPacket(clazz, decoder, NetworkDirection.PLAY_TO_SERVER);
    }

    public static <T extends APPacket> void registerPacket(Class<T> clazz, Function<FriendlyByteBuf, T> decoder, NetworkDirection direction)
    {
    	registerPacket(INSTANCE.messageBuilder(clazz, id(), direction), decoder);
    }

    public static <T extends APPacket> void registerPacket(MessageBuilder<T> builder, Function<FriendlyByteBuf, T> decoder)
    {
    	builder
    	.decoder(decoder)
    	.encoder(APPacket::write)
    	.consumerMainThread(APPacket::handle)
    	.add();
    }

    public static <T extends ClientPacket> void registerClientPacketAsync(Class<T> clazz, Function<FriendlyByteBuf, T> decoder)
    {
    	registerPacketAsync(clazz, decoder, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <T extends ServerPacket> void registerServerPacketAsync(Class<T> clazz, Function<FriendlyByteBuf, T> decoder)
    {
    	registerPacketAsync(clazz, decoder, NetworkDirection.PLAY_TO_SERVER);
    }

    public static <T extends APPacket> void registerPacketAsync(Class<T> clazz, Function<FriendlyByteBuf, T> decoder, NetworkDirection direction)
    {
    	registerPacketAsync(INSTANCE.messageBuilder(clazz, id(), direction), decoder);
    }

    public static <T extends APPacket> void registerPacketAsync(MessageBuilder<T> builder, Function<FriendlyByteBuf, T> decoder)
    {
    	builder
    	.decoder(decoder)
    	.encoder(APPacket::write)
    	.consumerNetworkThread(APPacket::handle)
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