package com.firemerald.additionalplacements.network.server;

import com.firemerald.additionalplacements.network.AbstractLoginPacket;

import net.minecraftforge.network.NetworkDirection;

public abstract class ServerLoginPacket extends AbstractLoginPacket {
	@Override
	public NetworkDirection getDirection() {
		return NetworkDirection.LOGIN_TO_CLIENT;
	}
}
