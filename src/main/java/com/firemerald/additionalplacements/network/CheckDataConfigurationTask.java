package com.firemerald.additionalplacements.network;

import java.util.function.Consumer;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.configuration.ICustomConfigurationTask;

public class CheckDataConfigurationTask implements ICustomConfigurationTask {
	public static final Type TYPE = new Type(new ResourceLocation(AdditionalPlacementsMod.MOD_ID, "configuration_checks"));

    @Override
    public void run(Consumer<CustomPacketPayload> sender) {
    	sender.accept(new CheckDataClientPacket());
    }

	@Override
	public Type type() {
		return TYPE;
	}
}
