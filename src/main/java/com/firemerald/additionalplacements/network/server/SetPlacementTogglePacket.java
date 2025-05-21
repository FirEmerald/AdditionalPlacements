package com.firemerald.additionalplacements.network.server;

import com.firemerald.additionalplacements.client.APClientData;
import com.firemerald.additionalplacements.common.IAPServerPlayer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class SetPlacementTogglePacket extends ServerPlayPacket
{
	private final boolean state;

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
	public void handleServer(CustomPayloadEvent.Context context)
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