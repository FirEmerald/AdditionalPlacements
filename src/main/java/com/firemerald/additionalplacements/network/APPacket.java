package com.firemerald.additionalplacements.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.PacketDistributor.PacketTarget;

public abstract class APPacket
{
	public abstract void write(FriendlyByteBuf buf);

	public abstract void handle(CustomPayloadEvent.Context context);

    public void sendTo(PacketTarget target)
    {
    	APNetwork.sendTo(this, target);
    }
}
