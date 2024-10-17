package com.firemerald.additionalplacements.network;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;

import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

public abstract class ServerPacket extends APPacket
{
	public abstract void handleServer(CustomPayloadEvent.Context context);
	
	@Override
	public void handle(CustomPayloadEvent.Context context)
	{
		if (context.getDirection() == NetworkDirection.PLAY_TO_SERVER) handleServer(context);
		else AdditionalPlacementsMod.LOGGER.error("Tried to handle " + getClass() + " with invalid direction " + context.getDirection());
	}
	
    public void sendToServer()
    {
    	APNetwork.sendToServer(this);
    }
}
