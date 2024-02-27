package com.firemerald.additionalplacements.network;

import java.util.function.Supplier;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.client.APClientData;
import com.firemerald.additionalplacements.common.IAPServerPlayer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

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
	public void handle(Supplier<NetworkEvent.Context> supplier)
	{
		NetworkEvent.Context context = supplier.get();
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