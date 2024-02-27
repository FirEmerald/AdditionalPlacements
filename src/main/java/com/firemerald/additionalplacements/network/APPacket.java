package com.firemerald.additionalplacements.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
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
}
