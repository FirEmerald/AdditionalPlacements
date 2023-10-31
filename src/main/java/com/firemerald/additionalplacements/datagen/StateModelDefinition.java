package com.firemerald.additionalplacements.datagen;

public class StateModelDefinition
{
	public final String model;
	public final int xRotation, yRotation;

	public StateModelDefinition(String model, int xRotation, int yRotation)
	{
		this.model = model;
		this.xRotation = xRotation;
		this.yRotation = yRotation;
	}

	public StateModelDefinition(String model, int yRotation)
	{
		this(model, 0, yRotation);
	}

	public StateModelDefinition(String model)
	{
		this(model, 0, 0);
	}

	@Override
	public int hashCode()
	{
		return (model.hashCode() << 4) | (((yRotation / 90) & 3) << 2) | ((xRotation / 90) & 3);
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == this) return true;
		else if (o == null || o.getClass() != StateModelDefinition.class) return false;
		else
		{
			StateModelDefinition s = (StateModelDefinition) o;
			return s.model.equals(model) && (s.xRotation % 360) == (xRotation % 360) && (s.yRotation % 360) == (yRotation % 360);
		}
	}
}