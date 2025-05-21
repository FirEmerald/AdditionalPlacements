package com.firemerald.additionalplacements.network;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;

import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor.PacketTarget;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public abstract class APPacket<T extends IPayloadContext> implements CustomPacketPayload
{
	public abstract PacketFlow getDirection();

	public abstract void handleInternal(T context);

	public void handle(T context)
	{
		if (context.flow() == getDirection()) handleInternal(context);
		else
            AdditionalPlacementsMod.LOGGER.error("Tried to handle {} with invalid direction {}", getClass(), context.flow());
	}

    public void sendTo(PacketTarget target)
    {
    	APNetwork.sendTo(this, target);
    }

    public void reply(T context)
    {
    	context.replyHandler().send(this);
    }
}
