package com.firemerald.additionalplacements.networking;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;

import static com.firemerald.additionalplacements.AdditionalPlacementsMod.MOD_ID;
import static com.firemerald.additionalplacements.AdditionalPlacementsMod.AP_TOGGLE;

public class Network
{
    public static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id()
    {
        return packetId++;
    }

    public static void register()
    {

        INSTANCE = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(MOD_ID, AP_TOGGLE))
            .networkProtocolVersion(() -> "1.0")
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .simpleChannel();

        INSTANCE.messageBuilder(PacketToggleAP.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketToggleAP::new)
                .encoder(PacketToggleAP::toBytes)
                .consumerMainThread(PacketToggleAP::handle)
                .add();

        INSTANCE.messageBuilder(PacketSetAP.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketSetAP::new)
                .encoder(PacketSetAP::toBytes)
                .consumerMainThread(PacketSetAP::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message)
    {
        INSTANCE.sendToServer(message);
    }

    public static void sendToClient(Object packet, ServerPlayer player)
    {
        INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player)
    {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

}
