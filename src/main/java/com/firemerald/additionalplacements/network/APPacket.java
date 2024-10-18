package com.firemerald.additionalplacements.network;

import java.util.function.Supplier;

import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor.PacketTarget;

public abstract class APPacket
{
	public abstract void write(FriendlyByteBuf buf);

	public abstract void handle(Supplier<NetworkEvent.Context> supplier);

    public void sendTo(PacketTarget target)
    {
    	APNetwork.sendTo(this, target);
    }

    public void send(Connection connection, NetworkDirection direction)
    {
    	APNetwork.send(this, connection, direction);
    }

    public void reply(NetworkEvent.Context context)
    {
    	APNetwork.reply(this, context);
    }
}
