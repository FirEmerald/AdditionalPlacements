package com.firemerald.additionalplacements.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

import static com.firemerald.additionalplacements.client.ClientEventHandler.toggleClientPlayerAP;

public class PacketSetAP
{
    private boolean ap;
    private UUID player;

    public PacketSetAP(boolean ap, Player player)
    {
        this.ap = ap;
        this.player = player.getUUID();
    }

    public PacketSetAP(FriendlyByteBuf buf)
    {
        this.ap = buf.readBoolean();
        this.player = buf.readUUID();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeBoolean(ap);
        buf.writeUUID(player);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> toggleClientPlayerAP(ap));
        return true;
    }
}
