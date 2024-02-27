package com.firemerald.additionalplacements.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public class APNetwork
{
    public static void register()
    {
        registerServerPacket(PacketSetPlacementToggle.TYPE);
    }
    
    public static <T extends PacketClient> void registerClientPacket(PacketType<T> type)
    {
    	ClientPlayNetworking.registerGlobalReceiver(type, PacketClient::handle);
    }
    
    public static <T extends PacketServer> void registerServerPacket(PacketType<T> type)
    {
    	ServerPlayNetworking.registerGlobalReceiver(type, PacketServer::handle);
    }

    public static <T extends FabricPacket> void sendToServer(T packet)
    {
    	ClientPlayNetworking.send(packet);
    }

    public static <T extends FabricPacket> void sendToClient(T packet, ServerPlayer player)
    {
    	ServerPlayNetworking.send(player, packet);
    }
}