package com.firemerald.additionalplacements.network;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;

import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

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
    
    @Override
	public void handle(PlayPayloadContext context)
	{
		if (context.flow() == PacketFlow.CLIENTBOUND) handleClient(context);
		else AdditionalPlacementsMod.LOGGER.error("Tried to handle " + this.getClass().getName() + " with invalid direction " + context.flow());
	}
    
    @OnlyIn(Dist.CLIENT)
    public abstract void handleClient(PlayPayloadContext context);
}
