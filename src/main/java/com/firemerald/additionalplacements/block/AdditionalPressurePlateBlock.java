package com.firemerald.additionalplacements.block;

import com.firemerald.additionalplacements.block.interfaces.IPressurePlateBlock;

import com.firemerald.additionalplacements.mixin.AccessorPressurePlate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PressurePlateBlock;

public class AdditionalPressurePlateBlock extends AdditionalBasePressurePlateBlock<PressurePlateBlock> implements IPressurePlateBlock<PressurePlateBlock>
{
	public static AdditionalPressurePlateBlock of(PressurePlateBlock plate)
	{
		return new AdditionalPressurePlateBlock(plate);
	}

	private AdditionalPressurePlateBlock(PressurePlateBlock plate)
	{
		super(plate);
	}

	@Override
	protected int getSignalStrength(Level level, BlockPos pos)
	{
		return ((AccessorPressurePlate) this.parentBlock).additional_placements_getSignalStrength(level, pos);
	}
}