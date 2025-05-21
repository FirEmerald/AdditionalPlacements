package com.firemerald.additionalplacements.network.client;

import java.util.HashMap;
import java.util.Map;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.generation.Registration;
import com.firemerald.additionalplacements.network.server.CheckDataServerPacket;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking.Context;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class CheckDataClientPacket extends ClientConfigurationPacket
{
	public static final Type<CheckDataClientPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(AdditionalPlacementsMod.MOD_ID, "check_data_client"));

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
		data = buf.readMap(FriendlyByteBuf::readResourceLocation, buffer -> buffer.readNbt());
	}

	@Override
	public Type<CheckDataClientPacket> type()
	{
		return TYPE;
	}

	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeMap(data, FriendlyByteBuf::writeResourceLocation, (buffer, tag) -> buffer.writeNbt(tag));
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void handleClient(Context context) {
		new CheckDataServerPacket(data).send(context.responseSender());
	}
}