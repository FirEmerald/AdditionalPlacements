package com.firemerald.additionalplacements.network;

import java.util.function.Function;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class APNetwork
{
    public static void register()
    {
        registerServerPacket(PacketSetPlacementToggle.ID, PacketSetPlacementToggle::new);
    }

    public static <T extends PacketClient> void registerClientPacket(ResourceLocation id, Function<FriendlyByteBuf, T> fromBuffer)
    {
    	ClientPlayNetworking.registerGlobalReceiver(id, (Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) -> fromBuffer.apply(buf).handle(client, handler, responseSender));
    }

    public static <T extends PacketServer> void registerServerPacket(ResourceLocation id, Function<FriendlyByteBuf, T> fromBuffer)
    {
    	ServerPlayNetworking.registerGlobalReceiver(id, (MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) -> fromBuffer.apply(buf).handle(server, player, handler, responseSender));
    }

    public static <T extends PacketServer> void sendToServer(T packet)
    {
    	ClientPlayNetworking.send(packet.getID(), packet.getBuf());
    }

    public static <T extends PacketClient> void sendToClient(T packet, ServerPlayer player)
    {
    	ServerPlayNetworking.send(player, packet.getID(), packet.getBuf());
    }
}