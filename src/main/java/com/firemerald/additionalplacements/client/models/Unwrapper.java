package com.firemerald.additionalplacements.client.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import net.minecraft.client.renderer.block.model.BlockStateModel;

public class Unwrapper {
	private static final List<Function<BlockStateModel, BlockStateModel>> UNWRAPPERS = new ArrayList<>();

	public static void registerUnwrapper(Function<BlockStateModel, BlockStateModel> unwrapper) {
		UNWRAPPERS.add(unwrapper);
	}

	public static BlockStateModel unwrap(BlockStateModel model) {
		Optional<BlockStateModel> next;
		while ((next = unwrapSingle(model)).isPresent()) model = next.get();
		return model;
	}

	private static Optional<BlockStateModel> unwrapSingle(BlockStateModel model) {
		return UNWRAPPERS.stream().map(uw -> uw.apply(model)).filter(Objects::nonNull).findFirst();
	}
}
