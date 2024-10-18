package com.firemerald.additionalplacements.generation;

import java.util.function.Function;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.config.GenerationBlacklist;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class SimpleGenerationType<T extends Block, U extends AdditionalPlacementBlock<T>> extends GenerationType<T, U> {
	public abstract static class BuilderBase<T extends Block, U extends AdditionalPlacementBlock<T>, V extends SimpleGenerationType<T, U>, W extends BuilderBase<T, U, V, W>> extends GenerationType.BuilderBase<T, U, V, W> {
		protected Function<T, U> constructor;
		
		public W constructor(Function<T, U> constructor) {
			this.constructor = constructor;
			return me();
		}
	}
	
	public static class Builder<T extends Block, U extends AdditionalPlacementBlock<T>> extends BuilderBase<T, U, SimpleGenerationType<T, U>, Builder<T, U>> {
		@Override
		public SimpleGenerationType<T, U> construct(Object protectionKey, ResourceLocation name, String description) {
			return new SimpleGenerationType<>(protectionKey, name, description, constructor, blacklist, placementEnabled);
		}
	}
	
	private final Function<T, U> constructor;

	protected SimpleGenerationType(Object protectionKey, ResourceLocation name, String description, Function<T, U> constructor, GenerationBlacklist blacklist, boolean placementEnabled) {
		super(protectionKey, name, description, blacklist, placementEnabled);
		this.constructor = constructor;
	}

	@Override
	public U construct(T block, ResourceLocation blockId) {
		return constructor.apply(block);
	}

}
