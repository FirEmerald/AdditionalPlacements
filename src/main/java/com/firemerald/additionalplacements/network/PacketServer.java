package com.firemerald.additionalplacements.network;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;

import net.minecraft.network.protocol.PacketFlow;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public abstract class PacketServer extends APPacket
{
    public void sendToServer()
    {
    	APNetwork.sendToServer(this);
    }
    
    @Override
	public void handle(PlayPayloadContext context)
	{
		if (context.flow() == PacketFlow.SERVERBOUND) handleServer(context);
		else AdditionalPlacementsMod.LOGGER.error("Tried to handle " + this.getClass().getName() + " with invalid direction " + context.flow());
	}
    
    public abstract void handleServer(PlayPayloadContext context);
}
