package com.firemerald.additionalplacements.block;

import com.firemerald.additionalplacements.block.interfaces.IPressurePlateBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BasePressurePlateBlock;
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
		Class<? extends Entity> oclass1;
		switch (this.parentBlock.type.pressurePlateSensitivity()) {
		case EVERYTHING:
			oclass1 = Entity.class;
			break;
		case MOBS:
			oclass1 = LivingEntity.class;
			break;
		default:
			throw new IncompatibleClassChangeError();
		}
		
		Class<? extends Entity> oclass = oclass1;
		return BasePressurePlateBlock.getEntityCount(level, TOUCH_AABBS[level.getBlockState(pos).getValue(AdditionalBasePressurePlateBlock.PLACING).ordinal() - 1].move(pos), oclass) > 0 ? 15 : 0;
	}
}