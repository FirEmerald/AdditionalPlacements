package com.firemerald.additionalplacements.network;

import net.minecraft.server.level.ServerPlayer;

public abstract class PacketClient extends APPacket
{
    public void sendToClient(ServerPlayer player)
    {
    	APNetwork.sendToClient(this, player);
    }
    
    public void sendToAllClients()
    {
    	APNetwork.sendToAllClients(this);
    }
}
