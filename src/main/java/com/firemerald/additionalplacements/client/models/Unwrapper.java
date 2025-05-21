package com.firemerald.additionalplacements.client.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import net.minecraft.client.resources.model.BakedModel;

public class Unwrapper {
	private static final List<Function<BakedModel, BakedModel>> UNWRAPPERS = new ArrayList<>();

	public static void registerUnwrapper(Function<BakedModel, BakedModel> unwrapper) {
		UNWRAPPERS.add(unwrapper);
	}

	public static BakedModel unwrap(BakedModel model) {
		Optional<BakedModel> next;
		while ((next = unwrapSingle(model)).isPresent()) model = next.get();
		return model;
	}

	private static Optional<BakedModel> unwrapSingle(BakedModel model) {
		return UNWRAPPERS.stream().map(uw -> uw.apply(model)).filter(Objects::nonNull).findFirst();
	}
}
