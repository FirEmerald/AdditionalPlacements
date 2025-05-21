package com.firemerald.additionalplacements.network;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;

import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor.PacketTarget;

public abstract class APPacket
{
	public abstract void write(FriendlyByteBuf buf);

	public abstract NetworkDirection getDirection();

	public abstract void handleInternal(CustomPayloadEvent.Context context);

	public void handle(CustomPayloadEvent.Context context) {
		if (context.getDirection() == getDirection()) handleInternal(context);
		else
            AdditionalPlacementsMod.LOGGER.error("Tried to handle {} with invalid direction {}", getClass(), context.getDirection());
	}

    public void sendTo(PacketTarget target)
    {
    	APNetwork.sendTo(this, target);
    }

    public void send(Connection connection)
    {
    	APNetwork.send(this, connection);
    }

    public void reply(CustomPayloadEvent.Context context)
    {
    	APNetwork.reply(this, context);
    }
}
