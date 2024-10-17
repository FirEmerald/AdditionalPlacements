package com.firemerald.additionalplacements.network;

import java.util.function.Consumer;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraftforge.network.config.ConfigurationTaskContext;

public class CheckDataConfigurationTask implements ConfigurationTask {
	public static final Type TYPE = new Type(AdditionalPlacementsMod.MOD_ID + ":configuration_checks");

    @Override
    public void start(ConfigurationTaskContext ctx) {
    	new CheckDataClientPacket().send(ctx.getConnection());
    }

    @Override
    public void start(Consumer<Packet<?>> send) {
        throw new IllegalStateException("This should never be called");
    }

	@Override
	public Type type() {
		return TYPE;
	}
}
