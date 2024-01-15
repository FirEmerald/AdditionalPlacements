package com.firemerald.additionalplacements.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;
import static com.firemerald.additionalplacements.AdditionalPlacementsMod.AP_TOGGLE;

public class PacketToggleAP
{
    private boolean ap;

    public PacketToggleAP(boolean ap)
    {
        this.ap = ap;
    }

    public PacketToggleAP(FriendlyByteBuf buf)
    {
        this.ap = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeBoolean(ap);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        ServerPlayer player = supplier.get().getSender();
        if (player == null) return false;
        player.getPersistentData().putBoolean(AP_TOGGLE, ap);
        return true;
    }

}
