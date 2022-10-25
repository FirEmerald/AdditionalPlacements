package com.firemerald.additionalplacements.block;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.phys.AABB;

public class AdditionalPressurePlateBlock extends AdditionalBasePressurePlateBlock<PressurePlateBlock>
{
	public AdditionalPressurePlateBlock(PressurePlateBlock plate)
	{
		super(plate);
	}

	@Override
	protected int getSignalStrength(Level level, BlockPos pos)
	{
		AABB aabb = TOUCH_AABBS[level.getBlockState(pos).getValue(AdditionalBasePressurePlateBlock.PLACING).ordinal() - 1].move(pos);
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