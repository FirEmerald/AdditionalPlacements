package com.firemerald.additionalplacements.network;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor.PacketTarget;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

public class APNetwork
{
	public static void register(RegisterPayloadHandlerEvent event)
	{
		IPayloadRegistrar registrar = event.registrar(AdditionalPlacementsMod.MOD_ID);
		registrar.play(SetPlacementTogglePacket.ID, SetPlacementTogglePacket::new, SetPlacementTogglePacket::handle);
		registrar.configuration(CheckDataClientPacket.ID, CheckDataClientPacket::new, CheckDataClientPacket::handle);
		registrar.configuration(CheckDataServerPacket.ID, CheckDataServerPacket::new, CheckDataServerPacket::handle);
		registrar.configuration(ConfigurationCheckFailedPacket.ID, ConfigurationCheckFailedPacket::new, ConfigurationCheckFailedPacket::handle);
	}

    public static void sendToServer(CustomPacketPayload packet)
    {
    	sendTo(packet, PacketDistributor.SERVER.noArg());
    }

    public static void sendToClient(CustomPacketPayload packet, ServerPlayer player)
    {
    	sendTo(packet, PacketDistributor.PLAYER.with(player));
    }

    public static void sendToAllClients(CustomPacketPayload packet)
    {
    	sendTo(packet, PacketDistributor.ALL.noArg());
    }

    public static void sendTo(CustomPacketPayload packet, PacketTarget target)
    {
    	target.send(packet);
    }
}