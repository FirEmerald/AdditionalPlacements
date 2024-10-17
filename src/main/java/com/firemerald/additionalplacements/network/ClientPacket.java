package com.firemerald.additionalplacements.network;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;

import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public abstract class ClientPacket<T extends IPayloadContext> extends APPacket<T>
{
	public abstract void handleClient(T context);
	
	@Override
	public void handle(T context)
	{
		if (context.flow() == PacketFlow.CLIENTBOUND) handleClient(context);
		else AdditionalPlacementsMod.LOGGER.error("Tried to handle " + getClass() + " with invalid direction " + context.flow());
	}
	
    public void sendToClient(ServerPlayer player)
    {
    	APNetwork.sendToClient(this, player);
    }

    public void sendToAllClients()
    {
    	APNetwork.sendToAllClients(this);
    }
}
