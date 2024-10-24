package com.firemerald.additionalplacements.network;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

import net.minecraftforge.network.HandshakeHandler;
import net.minecraftforge.network.NetworkEvent;

public abstract class AbstractLoginPacket extends APPacket implements IntSupplier {
    private int loginIndex = -1;

    public void setLoginIndex(final int loginIndex) {
    	this.loginIndex = loginIndex;
    }

    public int getLoginIndex() {
        return loginIndex;
    }

    @Override
    public int getAsInt() {
        return getLoginIndex();
    }

	public static <T extends AbstractLoginPacket> void handle(HandshakeHandler handler, T msg, Supplier<NetworkEvent.Context> context) {
		msg.handle(context.get());
	}
}
