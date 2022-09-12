package com.firemerald.vsas.datagen;

public record StateModelDefinition(String model, int xRotation, int yRotation)
{
	public StateModelDefinition(String model, int yRotation)
	{
		this(model, 0, yRotation);
	}

	public StateModelDefinition(String model)
	{
		this(model, 0, 0);
	}
}