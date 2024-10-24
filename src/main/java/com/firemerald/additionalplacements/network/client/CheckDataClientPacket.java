package com.firemerald.additionalplacements.network.client;

import java.util.*;

import com.firemerald.additionalplacements.generation.Registration;
import com.firemerald.additionalplacements.network.server.CheckDataServerPacket;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent.Context;

public class CheckDataClientPacket extends ClientLoginPacket
{
	private final Map<ResourceLocation, CompoundTag> data;
	
	public CheckDataClientPacket() {
		data = new HashMap<>();
		Registration.forEach((id, type) -> {
			CompoundTag tag = type.getServerCheckData();
			if (tag != null) data.put(id, tag);
		});
	}

	public CheckDataClientPacket(FriendlyByteBuf buf)
	{
		data = buf.readMap(FriendlyByteBuf::readResourceLocation, FriendlyByteBuf::readNbt);
	}

	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeMap(data, FriendlyByteBuf::writeResourceLocation, FriendlyByteBuf::writeNbt);
	}

	@Override
	public void handle(Context context) {
		context.setPacketHandled(true);
		new CheckDataServerPacket(data).reply(context);
	}
}