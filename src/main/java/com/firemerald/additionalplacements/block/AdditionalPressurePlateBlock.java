package com.firemerald.additionalplacements.block;

import com.firemerald.additionalplacements.block.interfaces.IAdditionalBeaconBeamBlock;
import com.firemerald.additionalplacements.block.interfaces.IPressurePlateBlock;
import com.firemerald.additionalplacements.client.models.definitions.PressurePlateModels;
import com.firemerald.additionalplacements.client.models.definitions.StateModelDefinition;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.BlockState;

public class AdditionalPressurePlateBlock extends AdditionalBasePressurePlateBlock<PressurePlateBlock> implements IPressurePlateBlock<PressurePlateBlock>
{
	public static AdditionalPressurePlateBlock of(PressurePlateBlock plate)
	{
		return plate instanceof BeaconBeamBlock ? new AdditionalBeaconBeamPressurePlateBlock(plate) : new AdditionalPressurePlateBlock(plate);
	}

	private static class AdditionalBeaconBeamPressurePlateBlock extends AdditionalPressurePlateBlock implements IAdditionalBeaconBeamBlock<PressurePlateBlock>
	{
		AdditionalBeaconBeamPressurePlateBlock(PressurePlateBlock plate)
		{
			super(plate);
		}
	}

	private AdditionalPressurePlateBlock(PressurePlateBlock plate)
	{
		super(plate);
	}

	@Override
	protected int getSignalStrength(Level level, BlockPos pos)
	{

        return BasePressurePlateBlock.getEntityCount(level, TOUCH_AABBS[level.getBlockState(pos).getValue(AdditionalFloorBlock.PLACING).ordinal() - 1].move(pos), switch (this.parentBlock.type.pressurePlateSensitivity()) {
case EVERYTHING -> Entity.class;
case MOBS -> LivingEntity.class;
        }) > 0 ? 15 : 0;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public StateModelDefinition getModelDefinition(BlockState state) {
		return PressurePlateModels.getPressurePlateModel(state);
	}
}