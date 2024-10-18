package com.firemerald.additionalplacements.network;

import java.util.function.Supplier;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public abstract class ClientPacket extends APPacket
{
	public abstract void handleClient(NetworkEvent.Context context);
	
	@Override
	public void handle(Supplier<NetworkEvent.Context> supplier)
	{
		NetworkEvent.Context context = supplier.get();
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
