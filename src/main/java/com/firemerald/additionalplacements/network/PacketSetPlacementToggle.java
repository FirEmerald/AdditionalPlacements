package com.firemerald.additionalplacements.network;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.client.APClientData;
import com.firemerald.additionalplacements.common.IAPServerPlayer;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class PacketSetPlacementToggle extends PacketServer
{
	public static final PacketType<PacketSetPlacementToggle> TYPE = PacketType.create(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, "set_placement_toggle"), PacketSetPlacementToggle::new);

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
	public PacketType<?> getType()
	{
		return TYPE;
	}

	@Override
	public void handle(ServerPlayer player, PacketSender responseSender)
	{
		((IAPServerPlayer) player).setPlacementEnabled(state);
	}

	@Override
    public void sendToServer()
    {
		super.sendToServer();
		APClientData.lastSynchronizedTime = System.currentTimeMillis();
    }
}