package com.firemerald.additionalplacements.network;

import java.util.function.Supplier;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor.PacketTarget;

public abstract class APPacket
{
	public abstract void write(PacketBuffer buf);

	public abstract NetworkDirection getDirection();

	public abstract void handle(NetworkEvent.Context context);

	public void handle(Supplier<NetworkEvent.Context> supplier)
	{
		NetworkEvent.Context context = supplier.get();
		if (context.getDirection() == getDirection()) handle(context);
		else AdditionalPlacementsMod.LOGGER.error("Tried to handle {} with invalid direction {}", getClass(), context.getDirection());
	}

    public void sendTo(PacketTarget target)
    {
    	APNetwork.sendTo(this, target);
    }

    public void send(NetworkManager connection, NetworkDirection direction)
    {
    	APNetwork.send(this, connection, direction);
    }

    public void reply(NetworkEvent.Context context)
    {
    	APNetwork.reply(this, context);
    }
}
