package com.firemerald.additionalplacements.network;

import java.util.function.Function;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.NetworkRegistry.ChannelBuilder;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.PacketDistributor.PacketTarget;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.network.simple.SimpleChannel.MessageBuilder;

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
        registerServerPacket(PacketSetPlacementToggle.class, PacketSetPlacementToggle::new);
    }
    
    public static <T extends PacketClient> void registerClientPacket(Class<T> clazz, Function<PacketBuffer, T> decoder)
    {
    	registerPacket(clazz, decoder, NetworkDirection.PLAY_TO_CLIENT);
    }
    
    public static <T extends PacketServer> void registerServerPacket(Class<T> clazz, Function<PacketBuffer, T> decoder)
    {
    	registerPacket(clazz, decoder, NetworkDirection.PLAY_TO_SERVER);
    }
    
    public static <T extends APPacket> void registerPacket(Class<T> clazz, Function<PacketBuffer, T> decoder, NetworkDirection direction)
    {
    	registerPacket(INSTANCE.messageBuilder(clazz, id(), direction), decoder);
    }
    
    public static <T extends APPacket> void registerPacket(MessageBuilder<T> builder, Function<PacketBuffer, T> decoder)
    {
    	builder
    	.decoder(decoder)
    	.encoder(APPacket::write)
    	.consumer(APPacket::handle)
    	.add();
    }

    public static void sendToServer(Object packet)
    {
    	sendTo(packet, PacketDistributor.SERVER.noArg());
    }

    public static void sendToClient(Object packet, ServerPlayerEntity player)
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
}