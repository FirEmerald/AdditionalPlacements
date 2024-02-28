package com.firemerald.additionalplacements.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.server.level.ServerPlayer;

public abstract class PacketClient extends APPacket
{
	public abstract void handle(Minecraft client, ClientPacketListener handler, PacketSender responseSender);

    public void sendToClient(ServerPlayer player)
    {
    	APNetwork.sendToClient(this, player);
    }
}
