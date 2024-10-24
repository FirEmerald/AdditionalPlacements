package com.firemerald.additionalplacements.block;

import java.util.List;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.interfaces.IPressurePlateBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.phys.AABB;

public class AdditionalPressurePlateBlock extends AdditionalBasePressurePlateBlock<PressurePlateBlock> implements IPressurePlateBlock<PressurePlateBlock>
{
	static final ResourceLocation PRESSURE_PLATE_BLOCKSTATES = new ResourceLocation(AdditionalPlacementsMod.MOD_ID, "blockstate_templates/pressure_plate.json");
	
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
			throw new IncompatibleClassChangeError();
		}
		if (!list.isEmpty()) for(Entity entity : list) if (!entity.isIgnoringBlockTriggers()) return 15;
		return 0;
	}

	@Override
	public ResourceLocation getDynamicBlockstateJson() {
		return PRESSURE_PLATE_BLOCKSTATES;
	}
}