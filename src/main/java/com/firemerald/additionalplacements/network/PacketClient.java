package com.firemerald.additionalplacements.network;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;

public abstract class PacketClient implements FabricPacket
{
	public abstract void handle(LocalPlayer player, PacketSender responseSender);

    public void sendToClient(ServerPlayer player)
    {
    	APNetwork.sendToClient(this, player);
    }
}
