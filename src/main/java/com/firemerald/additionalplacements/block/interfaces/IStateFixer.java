package com.firemerald.additionalplacements.block.interfaces;

import java.util.function.Consumer;

import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.state.Property;

public interface IStateFixer {
	CompoundNBT fix(CompoundNBT properties, Consumer<Block> changeBlock);
	
	static boolean contains(CompoundNBT tag, Property<?> property) {
		return tag.contains(property.getName());
	}
	
	static <T extends Comparable<T>> void setProperty(CompoundNBT tag, Property<T> property, T value) {
		tag.put(property.getName(), StringNBT.valueOf(property.getName(value)));
	}
	
	static <T extends Comparable<T>> T getProperty(CompoundNBT tag, Property<T> property) {
		return property.getValue(getPropertyString(tag, property)).orElse(null);
	}
	
	static String getPropertyString(CompoundNBT tag, Property<?> property) {
		return tag.getString(property.getName());
	}
	
	static void renameProperty(CompoundNBT tag, Property<?> from, Property<?> to) {
		renameProperty(tag, from.getName(), to);
	}
	
	static void renameProperty(CompoundNBT tag, String from, Property<?> to) {
		tag.put(to.getName(), tag.get(from));
		tag.remove(from);
	}
	
	static void remove(CompoundNBT tag, Property<?> property) {
		tag.remove(property.getName());
	}
}
