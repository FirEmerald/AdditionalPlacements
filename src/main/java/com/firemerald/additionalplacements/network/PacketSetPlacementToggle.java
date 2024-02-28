package com.firemerald.additionalplacements.network;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.client.APClientData;
import com.firemerald.additionalplacements.common.IAPServerPlayer;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class PacketSetPlacementToggle extends PacketServer
{
	public static final ResourceLocation ID = new ResourceLocation(AdditionalPlacementsMod.MOD_ID, "set_placement_toggle");

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
	public ResourceLocation getID()
	{
		return ID;
	}

	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeBoolean(state);
	}

	@Override
	public void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, PacketSender responseSender)
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