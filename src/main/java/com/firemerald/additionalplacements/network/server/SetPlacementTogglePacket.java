package com.firemerald.additionalplacements.network.server;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.client.APClientData;
import com.firemerald.additionalplacements.common.IAPServerPlayer;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SetPlacementTogglePacket extends ServerPacket<RegistryFriendlyByteBuf>
{
	public static final Type<SetPlacementTogglePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(AdditionalPlacementsMod.MOD_ID, "set_placement_toggle"));

	private final boolean state;

	public SetPlacementTogglePacket(boolean state)
	{
		this.state = state;
	}

	public SetPlacementTogglePacket(RegistryFriendlyByteBuf buf)
	{
		this.state = buf.readBoolean();
	}

	@Override
	public Type<SetPlacementTogglePacket> type()
	{
		return TYPE;
	}

	@Override
	public void write(RegistryFriendlyByteBuf buf)
	{
		buf.writeBoolean(state);
	}

	@Override
	public void handleServer(IPayloadContext context)
	{
		((IAPServerPlayer) context.player()).setPlacementEnabled(state);
	}

	@Override
    public void sendToServer()
    {
		super.sendToServer();
		APClientData.lastSynchronizedTime = System.currentTimeMillis();
    }
}