package com.firemerald.additionalplacements.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import com.firemerald.additionalplacements.block.VerticalSlabBlock;
import com.firemerald.additionalplacements.block.VerticalStairBlock;
import com.firemerald.additionalplacements.util.stairs.*;

import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;

public class StateFixer {
	private static final Map<Class<?>, Function<CompoundTag, CompoundTag>> FIXERS = new HashMap<>();
	
	public static void register(Class<?> block, Function<CompoundTag, CompoundTag> fixer) {
		FIXERS.put(block, fixer);
	}
	
	public static Function<CompoundTag, CompoundTag> getFixer(Class<?> block) {
		return FIXERS.get(block);
	}
	
	static {
		register(VerticalSlabBlock.class, properties -> {
			if (properties.contains("facing") && !(properties.contains("axis") && properties.contains("type"))) {
				BlockStateProperties.HORIZONTAL_FACING.getValue(properties.getString("facing")).ifPresent(facing -> {
					properties.putString("axis", VerticalSlabBlock.AXIS.getName(facing.getAxis()));
					properties.putString("type", SlabBlock.TYPE.getName(facing.getAxisDirection() == AxisDirection.POSITIVE ? SlabType.TOP : SlabType.BOTTOM));
				});
				properties.remove("facing");
			}
			return properties;
		});
		register(VerticalStairBlock.class, properties -> {
			if (properties.contains("placing") && properties.contains("shape") && !properties.contains("facing")) {
				OldStairPlacing placing = OldStairPlacing.get(properties.getString("placing"));
				OldStairShape shape = OldStairShape.get(properties.getString("shape"));
				if (placing != null && shape != null) {
					Pair<CompressedStairFacing, StairFacingType> pair = CompressedStairFacing.getCompressedFacing(placing.equivalent(shape));
					properties.putString("facing", VerticalStairBlock.FACING.getName(pair.getLeft()));
					properties.putString("shape", StairConnections.ALL.shapeProperty.getName(CompressedStairShape.getCompressedShape(pair.getRight(), shape.equivalent)));
				}
				properties.remove("placing");
			}
			return properties;
		});
	}
}
