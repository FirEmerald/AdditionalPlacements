package com.firemerald.additionalplacements.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor.PacketTarget;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public abstract class APPacket implements CustomPacketPayload
{
	public abstract void handle(PlayPayloadContext context);

    public void sendTo(PacketTarget target)
    {
    	APNetwork.sendTo(this, target);
    }
}
