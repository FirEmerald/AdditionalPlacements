package com.firemerald.additionalplacements.client.models;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;

public class ModelKey
{
	public final BlockState state;
	public final Direction side;

	public ModelKey(BlockState state, Direction side)
	{
		this.state = state;
		this.side = side;
	}

	@Override
	public int hashCode()
	{
		return (state.hashCode() << 3) | (side == null ? 7 : side.ordinal());
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == this) return true;
		else if (o == null || o.getClass() != ModelKey.class) return false;
		else
		{
			ModelKey m = (ModelKey) o;
			return m.state.equals(state) && m.side == side;
		}
	}
}