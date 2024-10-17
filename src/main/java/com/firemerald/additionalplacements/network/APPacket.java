package com.firemerald.additionalplacements.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor.PacketTarget;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public abstract class APPacket<T extends IPayloadContext> implements CustomPacketPayload
{
	public abstract void handle(T context);

    public void sendTo(PacketTarget target)
    {
    	APNetwork.sendTo(this, target);
    }
    
    public void reply(T context)
    {
    	context.replyHandler().send(this);
    }
}
