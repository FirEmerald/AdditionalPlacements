package com.firemerald.additionalplacements.network;

import java.util.*;
import java.util.function.Consumer;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import com.firemerald.additionalplacements.generation.Registration;
import com.firemerald.additionalplacements.util.MessageTree;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkContext;

public class CheckDataServerPacket extends ServerPacket
{
	private final Map<ResourceLocation, Pair<CompoundTag, List<MessageTree>>> serverData;
	
	@SuppressWarnings("unchecked")
	public CheckDataServerPacket(Map<ResourceLocation, CompoundTag> serverData) {
		this.serverData = new HashMap<>();
		Registration.forEach((id, type) -> {
			CompoundTag clientTag = type.getClientCheckData();
			List<MessageTree> clientErrors = new ArrayList<>();
			CompoundTag serverTag = serverData.get(id);
			type.checkServerData(serverTag, (Consumer<MessageTree>) clientErrors::add);
			if (clientTag != null || !clientErrors.isEmpty()) this.serverData.put(id, Pair.of(clientTag, clientErrors));
		});
	}

	public CheckDataServerPacket(FriendlyByteBuf buf)
	{
		serverData = buf.readMap(FriendlyByteBuf::readResourceLocation, buf2 -> {
			CompoundTag clientTag = buf2.readNbt();
			List<MessageTree> clientErrors = buf2.readList(MessageTree::new);
			return Pair.of(clientTag, clientErrors);
		});
	}

	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeMap(serverData, FriendlyByteBuf::writeResourceLocation, (buf2, data) -> {
			buf2.writeNbt(data.getLeft());
			buf2.writeCollection(data.getRight(), MessageTree::write);
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleServer(CustomPayloadEvent.Context context)
	{
		List<Triple<ResourceLocation, List<MessageTree>, List<MessageTree>>> compiledErrors = new ArrayList<>();
		Registration.forEach((id, type) -> {
			Pair<CompoundTag, List<MessageTree>> clientData = this.serverData.get(id);
			CompoundTag clientTag;
			List<MessageTree> clientErrors;
			if (clientData != null) {
				clientTag = clientData.getLeft();
				clientErrors = clientData.getRight();
			} else {
				clientTag = null;
				clientErrors = Collections.emptyList();
			}
			List<MessageTree> serverErrors = new ArrayList<>();
			type.checkClientData(clientTag, (Consumer<MessageTree>) serverErrors::add);
			if (!clientErrors.isEmpty() || !serverErrors.isEmpty()) compiledErrors.add(Triple.of(id, clientErrors, serverErrors));
		});
		if (!compiledErrors.isEmpty()) {
			new ConfigurationCheckFailedPacket(compiledErrors).reply(context);
			//if it turns out this CAN prevent the above packet from being sent, move disconnect to client-side. It did not prevent it in testing.
			context.getConnection().disconnect(Component.translatableWithFallback("msg.additionalplacements.disconnected", "Additional Placements configuration conflict"));
		}
		NetworkContext.get(context.getConnection()).finishTask(CheckDataConfigurationTask.TYPE); //finish task, very important!
	}
}