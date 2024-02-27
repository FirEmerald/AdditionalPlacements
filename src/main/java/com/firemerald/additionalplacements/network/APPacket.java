package com.firemerald.additionalplacements.network;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor.PacketTarget;

public abstract class APPacket
{
	public abstract void write(PacketBuffer buf);

	public abstract void handle(Supplier<NetworkEvent.Context> supplier);

    public void sendTo(PacketTarget target)
    {
    	APNetwork.sendTo(this, target);
    }
}
