package com.firemerald.additionalplacements.network;

public abstract class PacketServer extends APPacket
{
    public void sendToServer()
    {
    	APNetwork.sendToServer(this);
    }
}
