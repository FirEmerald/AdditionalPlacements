package com.firemerald.additionalplacements.network.server;

import com.firemerald.additionalplacements.client.APClientData;
import com.firemerald.additionalplacements.common.IAPServerPlayer;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SetPlacementTogglePacket extends ServerPlayPacket
{
	private final boolean state;

	public SetPlacementTogglePacket(boolean state)
	{
		this.state = state;
	}

	public SetPlacementTogglePacket(PacketBuffer buf)
	{
		this.state = buf.readBoolean();
	}

	@Override
	public void write(PacketBuffer buf)
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