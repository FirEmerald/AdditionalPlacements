package com.firemerald.additionalplacements.network.server;

import com.firemerald.additionalplacements.network.APNetwork;
import com.firemerald.additionalplacements.network.APPacket;

import net.minecraftforge.network.NetworkDirection;

public abstract class ServerPlayPacket extends APPacket {
	@Override
	public NetworkDirection getDirection() {
		return NetworkDirection.PLAY_TO_SERVER;
	}
	
    public void sendToServer()
    {
    	APNetwork.sendToServer(this);
    }
}
