package com.firemerald.additionalplacements.network;

import java.util.function.Supplier;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;

import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor.PacketTarget;

public abstract class APPacket
{
	public abstract void write(FriendlyByteBuf buf);

	public abstract NetworkDirection getDirection();

	public abstract void handle(NetworkEvent.Context context);

	public void handle(Supplier<NetworkEvent.Context> supplier)
	{
		NetworkEvent.Context context = supplier.get();
		if (context.getDirection() == getDirection()) handle(context);
		else AdditionalPlacementsMod.LOGGER.error("Tried to handle " + getClass() + " with invalid direction " + context.getDirection());
	}

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
