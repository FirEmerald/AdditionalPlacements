package com.firemerald.additionalplacements.network;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.client.APClientData;
import com.firemerald.additionalplacements.common.IAPServerPlayer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

public class PacketSetPlacementToggle extends PacketServer
{
	private boolean state;
	
	public PacketSetPlacementToggle(boolean state)
	{
		this.state = state;
	}
	
	public PacketSetPlacementToggle(FriendlyByteBuf buf)
	{
		this.state = buf.readBoolean();
	}
	
	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeBoolean(state);
	}

	@Override
	public void handle(CustomPayloadEvent.Context context)
	{
		if (context.getDirection() == NetworkDirection.PLAY_TO_SERVER) ((IAPServerPlayer) context.getSender()).setPlacementEnabled(state);
		else AdditionalPlacementsMod.LOGGER.error("Tried to handle PacketSetPlacementToggle with invalid direction " + context.getDirection());
	}
	
	@Override
    public void sendToServer()
    {
		super.sendToServer();
		APClientData.lastSynchronizedTime = System.currentTimeMillis();
    }
}