package com.firemerald.additionalplacements.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PressurePlateBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PressurePlateBlock.class)
public interface AccessorPressurePlate
{
    @Invoker("getSignalStrength")
    int additional_placements_getSignalStrength(Level pLevel, BlockPos pPos);
}
