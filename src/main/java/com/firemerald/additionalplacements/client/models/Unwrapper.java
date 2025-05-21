package com.firemerald.additionalplacements.client.models;

import java.util.*;
import java.util.function.Function;

import net.minecraft.client.renderer.model.*;

public class Unwrapper {
	private static final List<Function<IBakedModel, IBakedModel>> UNWRAPPERS = new ArrayList<>();
	
	public static void registerUnwrapper(Function<IBakedModel, IBakedModel> unwrapper) {
		UNWRAPPERS.add(unwrapper);
	}
	
	public static IBakedModel unwrap(IBakedModel model) {
		Optional<IBakedModel> next;
		while ((next = unwrapSingle(model)).isPresent()) model = next.get();
		return model;
	}
	
	private static Optional<IBakedModel> unwrapSingle(IBakedModel model) {
		return UNWRAPPERS.stream().map(uw -> uw.apply(model)).filter(Objects::nonNull).findFirst();
	}
}
