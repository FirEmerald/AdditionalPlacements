package com.firemerald.additionalplacements.network;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;

import net.minecraft.network.protocol.PacketFlow;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public abstract class ServerPacket<T extends IPayloadContext> extends APPacket<T>
{
	public abstract void handleServer(T context);
	
	@Override
	public void handle(T context)
	{
		if (context.flow() == PacketFlow.SERVERBOUND) handleServer(context);
		else AdditionalPlacementsMod.LOGGER.error("Tried to handle " + getClass() + " with invalid direction " + context.flow());
	}
	
    public void sendToServer()
    {
    	APNetwork.sendToServer(this);
    }
}
