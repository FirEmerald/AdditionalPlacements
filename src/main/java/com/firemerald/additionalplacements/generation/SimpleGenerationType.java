package com.firemerald.additionalplacements.generation;

import java.util.function.BiFunction;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class SimpleGenerationType<T extends Block, U extends AdditionalPlacementBlock<T>> extends GenerationType<T, U> {
	public abstract static class BuilderBase<T extends Block, U extends AdditionalPlacementBlock<T>, V extends SimpleGenerationType<T, U>, W extends BuilderBase<T, U, V, W>> extends GenerationType.BuilderBase<T, U, V, W> {
		protected BiFunction<? super T, ResourceKey<Block>, ? extends U> constructor;

		public W constructor(BiFunction<? super T, ResourceKey<Block>, ? extends U> constructor) {
			this.constructor = constructor;
			return me();
		}
	}

	public static class Builder<T extends Block, U extends AdditionalPlacementBlock<T>> extends BuilderBase<T, U, SimpleGenerationType<T, U>, Builder<T, U>> {
		@Override
		public SimpleGenerationType<T, U> construct(ResourceLocation name, String description) {
			return new SimpleGenerationType<>(name, description, this);
		}
	}

	private final BiFunction<? super T, ResourceKey<Block>, ? extends U> constructor;

	protected SimpleGenerationType(ResourceLocation name, String description, BuilderBase<T, U, ?, ?> builder) {
		super(name, description, builder);
		this.constructor = builder.constructor;
	}

	@Override
	public U construct(T block, ResourceKey<Block> key, ResourceLocation blockId) {
		return constructor.apply(block, key);
	}

}
