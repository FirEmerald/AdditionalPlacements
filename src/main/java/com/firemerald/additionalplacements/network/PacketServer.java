package com.firemerald.additionalplacements.network;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.server.level.ServerPlayer;

public abstract class PacketServer implements FabricPacket
{
	public abstract void handle(ServerPlayer player, PacketSender responseSender);

    public void sendToServer()
    {
    	APNetwork.sendToServer(this);
    }
}
