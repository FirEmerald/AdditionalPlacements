package com.firemerald.additionalplacements.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public abstract class APPacket
{
	public abstract ResourceLocation getID();

	public abstract void write(FriendlyByteBuf buf);

	public FriendlyByteBuf getBuf()
	{
		FriendlyByteBuf buf = PacketByteBufs.create();
		write(buf);
		return buf;
	}
}