package com.firemerald.additionalplacements.util.stairs;

import java.util.HashMap;
import java.util.Map;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public enum StairConnections {
	ALL("all_connections", "all", true, true, CompressedStairShape.ALL_SHAPES),
	NO_MIXED("no_mixed_connections", "noMixed", true, false, CompressedStairShape.NO_MIXED_CONNECTIONS),
	NO_VERTICAL("no_vertical_connections", "noVertical", false, false, CompressedStairShape.NO_VERTICAL_CONNECTIONS);
	
	private static final Map<String, StairConnections> TYPES;
	
	static {
		TYPES = new HashMap<>(values().length);
		for (StairConnections type : values()) TYPES.put(type.shortName, type);
	}
	
	public static StairConnections getType(String name) {
		return TYPES.get(name);
	}

	public final String name, shortName, translationKey;
	public final boolean allowVertical, allowMixed;
	public final ResourceLocation dynamicBlockstateJson;
	public final EnumProperty<CompressedStairShape> shapeProperty;
	
	StairConnections(String name, String shortName, boolean allowVertical, boolean allowMixed, CompressedStairShape... validShapes) {
		this(name, shortName, allowVertical, allowMixed, new ResourceLocation(AdditionalPlacementsMod.MOD_ID, "blockstate_templates/stairs/" + name + ".json"), EnumProperty.create("shape", CompressedStairShape.class, validShapes));
	}
	
	StairConnections(String name, String shortName, boolean allowVertical, boolean allowMixed, ResourceLocation dynamicBlockstateJson, EnumProperty<CompressedStairShape> shapeProperty) {
		this(name, shortName, "tooltip.additionalplacements.stair_connections." + name, allowVertical, allowMixed, dynamicBlockstateJson, shapeProperty);
	}
	
	StairConnections(String name, String shortName, String translationKey, boolean allowVertical, boolean allowMixed, CompressedStairShape... validShapes) {
		this(name, shortName, translationKey, allowVertical, allowMixed, new ResourceLocation(AdditionalPlacementsMod.MOD_ID, "blockstate_templates/stairs/" + name + ".json"), EnumProperty.create("shape", CompressedStairShape.class, validShapes));
	}
	
	StairConnections(String name, String shortName, String translationKey, boolean allowVertical, boolean allowMixed, ResourceLocation dynamicBlockstateJson, EnumProperty<CompressedStairShape> shapeProperty) {
		this.name = name;
		this.shortName = shortName;
		this.translationKey = translationKey;
		this.allowVertical = allowVertical;
		this.allowMixed = allowMixed;
		this.dynamicBlockstateJson = dynamicBlockstateJson;
		this.shapeProperty = shapeProperty;
	}
}
