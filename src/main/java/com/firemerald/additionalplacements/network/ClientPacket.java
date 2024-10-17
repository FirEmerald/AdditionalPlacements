package com.firemerald.additionalplacements.network;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

public abstract class ClientPacket extends APPacket
{
	public abstract void handleClient(CustomPayloadEvent.Context context);
	
	@Override
	public void handle(CustomPayloadEvent.Context context)
	{
		if (context.getDirection() == NetworkDirection.PLAY_TO_CLIENT) handleClient(context);
		else AdditionalPlacementsMod.LOGGER.error("Tried to handle " + getClass() + " with invalid direction " + context.getDirection());
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
