package com.firemerald.additionalplacements.network;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;

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
        registrar.play(PacketSetPlacementToggle.ID, PacketSetPlacementToggle::new, PacketSetPlacementToggle::handle);
    }

    public static void sendToServer(APPacket packet)
    {
    	sendTo(packet, PacketDistributor.SERVER.noArg());
    }

    public static void sendToClient(APPacket packet, ServerPlayer player)
    {
    	sendTo(packet, PacketDistributor.PLAYER.with(player));
    }

    public static void sendToAllClients(APPacket packet)
    {
    	sendTo(packet, PacketDistributor.ALL.noArg());
    }

    public static void sendTo(APPacket packet, PacketTarget target)
    {
    	target.send(packet);
    }
}