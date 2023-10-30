package com.firemerald.additionalplacements.block;

import com.firemerald.additionalplacements.block.interfaces.IAdditionalBeaconBeamBlock;
import com.firemerald.additionalplacements.block.interfaces.IWeightedPressurePlateBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.WeightedPressurePlateBlock;
import net.minecraft.world.phys.AABB;

public class AdditionalWeightedPressurePlateBlock extends AdditionalBasePressurePlateBlock<WeightedPressurePlateBlock> implements IWeightedPressurePlateBlock<WeightedPressurePlateBlock>
{
	public static AdditionalWeightedPressurePlateBlock of(WeightedPressurePlateBlock plate)
	{
		return plate instanceof BeaconBeamBlock ? new AdditionalBeaconBeamWeightedPressurePlateBlock(plate) : new AdditionalWeightedPressurePlateBlock(plate);
	}
	
	private static class AdditionalBeaconBeamWeightedPressurePlateBlock extends AdditionalWeightedPressurePlateBlock implements IAdditionalBeaconBeamBlock<WeightedPressurePlateBlock>
	{
		AdditionalBeaconBeamWeightedPressurePlateBlock(WeightedPressurePlateBlock plate)
		{
			super(plate);
		}
	}
	
	private AdditionalWeightedPressurePlateBlock(WeightedPressurePlateBlock plate)
	{
		super(plate);
	}

	@Override
	protected int getSignalStrength(Level level, BlockPos pos)
	{
		AABB aabb = TOUCH_AABBS[level.getBlockState(pos).getValue(AdditionalBasePressurePlateBlock.PLACING).ordinal() - 1].move(pos);
		int i = Math.min(level.getEntitiesOfClass(Entity.class, aabb).size(), parentBlock.maxWeight);
		if (i > 0) return Mth.ceil(15 * (float) Math.min(parentBlock.maxWeight, i) / (float)parentBlock.maxWeight);
		else return 0;
	}
}