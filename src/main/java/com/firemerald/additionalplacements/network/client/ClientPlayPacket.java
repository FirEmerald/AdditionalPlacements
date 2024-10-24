package com.firemerald.additionalplacements.network.client;

import com.firemerald.additionalplacements.network.APNetwork;
import com.firemerald.additionalplacements.network.APPacket;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;

public abstract class ClientPlayPacket extends APPacket {
	@Override
	public NetworkDirection getDirection() {
		return NetworkDirection.PLAY_TO_CLIENT;
	}
	
    public void sendToClient(ServerPlayer player) {
    	APNetwork.sendToClient(this, player);
    }

    public void sendToAllClients() {
    	APNetwork.sendToAllClients(this);
    }
}
