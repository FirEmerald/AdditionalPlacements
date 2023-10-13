package com.firemerald.additionalplacements.block;

import java.util.List;

import com.firemerald.additionalplacements.block.interfaces.IPressurePlateBlock;

import net.minecraft.block.PressurePlateBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AdditionalPressurePlateBlock extends AdditionalBasePressurePlateBlock<PressurePlateBlock> implements IPressurePlateBlock<PressurePlateBlock>
{
	public AdditionalPressurePlateBlock(PressurePlateBlock plate)
	{
		super(plate);
	}

	@Override
	protected int getSignalStrength(World level, BlockPos pos)
	{
		AxisAlignedBB aabb = TOUCH_AABBS[level.getBlockState(pos).getValue(AdditionalBasePressurePlateBlock.PLACING).ordinal() - 1].move(pos);
		List<? extends Entity> list;
		switch (this.parentBlock.sensitivity)
		{
		case EVERYTHING:
			list = level.getEntities(null, aabb);
			break;
		case MOBS:
			list = level.getEntitiesOfClass(LivingEntity.class, aabb);
			break;
		default:
			return 0;
		}
		if (!list.isEmpty()) for(Entity entity : list) if (!entity.isIgnoringBlockTriggers()) return 15;
		return 0;
	}
}