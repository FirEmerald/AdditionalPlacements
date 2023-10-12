package com.firemerald.additionalplacements.block;

import com.firemerald.additionalplacements.block.interfaces.IWeightedPressurePlateBlock;

import net.minecraft.block.WeightedPressurePlateBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class AdditionalWeightedPressurePlateBlock extends AdditionalBasePressurePlateBlock<WeightedPressurePlateBlock> implements IWeightedPressurePlateBlock<WeightedPressurePlateBlock>
{
	public AdditionalWeightedPressurePlateBlock(WeightedPressurePlateBlock plate)
	{
		super(plate);
	}

	@Override
	protected int getSignalStrength(World level, BlockPos pos)
	{
		AxisAlignedBB aabb = TOUCH_AABBS[level.getBlockState(pos).getValue(AdditionalBasePressurePlateBlock.PLACING).ordinal() - 1].move(pos);
		int i = Math.min(level.getEntitiesOfClass(Entity.class, aabb).size(), parentBlock.maxWeight);
		if (i > 0) return MathHelper.ceil(15 * (float) Math.min(parentBlock.maxWeight, i) / (float)parentBlock.maxWeight);
		else return 0;
	}
}