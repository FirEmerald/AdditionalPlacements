package com.firemerald.additionalplacements.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

public class VerticalWeatheringStairBlock<T extends StairBlock & WeatheringCopper> extends VerticalStairBlock implements WeatheringCopper
{
	public VerticalWeatheringStairBlock(T stair)
	{
		super(stair);
	}
	
	@Override
	public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom)
	{
		this.onRandomTick(pState, pLevel, pPos, pRandom);
	}

	@Override
	public WeatherState getAge()
	{
		return ((WeatheringCopper) this.parentBlock).getAge();
	}
}
