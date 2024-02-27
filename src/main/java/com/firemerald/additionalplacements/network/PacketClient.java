package com.firemerald.additionalplacements.network;

import net.minecraft.entity.player.ServerPlayerEntity;

public abstract class PacketClient extends APPacket
{
    public void sendToClient(ServerPlayerEntity player)
    {
    	APNetwork.sendToClient(this, player);
    }

    public void sendToAllClients()
    {
    	APNetwork.sendToAllClients(this);
    }
}
