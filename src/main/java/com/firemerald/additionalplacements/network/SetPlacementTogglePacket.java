package com.firemerald.additionalplacements.network;

import com.firemerald.additionalplacements.client.APClientData;
import com.firemerald.additionalplacements.common.IAPServerPlayer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SetPlacementTogglePacket extends ServerPacket
{
	private boolean state;

	public SetPlacementTogglePacket(boolean state)
	{
		this.state = state;
	}

	public SetPlacementTogglePacket(FriendlyByteBuf buf)
	{
		this.state = buf.readBoolean();
	}

	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeBoolean(state);
	}

	@Override
	public void handleServer(NetworkEvent.Context context)
	{
		((IAPServerPlayer) context.getSender()).setPlacementEnabled(state);
	}

	@Override
    public void sendToServer()
    {
		super.sendToServer();
		APClientData.lastSynchronizedTime = System.currentTimeMillis();
    }
}