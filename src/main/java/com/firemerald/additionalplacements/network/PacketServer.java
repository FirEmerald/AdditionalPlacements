package com.firemerald.additionalplacements.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public abstract class PacketServer extends APPacket
{
	public abstract void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, PacketSender responseSender);

    public void sendToServer()
    {
    	APNetwork.sendToServer(this);
    }
}
