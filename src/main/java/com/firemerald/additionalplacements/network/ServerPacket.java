package com.firemerald.additionalplacements.network;

import java.util.function.Supplier;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;

import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public abstract class ServerPacket extends APPacket
{
	public abstract void handleServer(NetworkEvent.Context context);
	
	@Override
	public void handle(Supplier<NetworkEvent.Context> supplier)
	{
		NetworkEvent.Context context = supplier.get();
		if (context.getDirection() == NetworkDirection.PLAY_TO_SERVER) handleServer(context);
		else AdditionalPlacementsMod.LOGGER.error("Tried to handle " + getClass() + " with invalid direction " + context.getDirection());
	}
	
    public void sendToServer()
    {
    	APNetwork.sendToServer(this);
    }
}
