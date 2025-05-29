package com.firemerald.additionalplacements.datagen;

import java.util.Collections;
import java.util.function.Consumer;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.client.models.definitions.*;
import com.google.gson.JsonObject;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplate;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;

public abstract class BlockModelGenerator<T extends Block, U extends AdditionalPlacementBlock<T>, V extends BlockModelGenerator<T, U, V>> {
	private ResourceLocation parentFolder;
	private BetterTextureMapping textures;
	private Consumer<ExtendedModelTemplateBuilder> extraProperties;
	
	public BlockModelGenerator() {
		clear();
	}
	
	public MultiVariant variantOf(StateModelDefinition modelDef, ResourceLocation modelPrefix) {
		return new MultiVariant(WeightedList.of(new Variant(
				modelDef.location(modelPrefix),
				new Variant.SimpleModelState(modelDef.xRotation(), modelDef.yRotation(), true)
		)));
	}
	
	public abstract MultiVariantGenerator generator(U block, ResourceLocation modelPrefix);
	
	@SuppressWarnings("unchecked")
	public V me() {
		return (V) this;
	}
	
	public V clear() {
		return clearParentFolder()
				.clearTextures()
				.clearExtraProperties();
	}
	
	public V parentFolder(ResourceLocation parentFolder) {
		this.parentFolder = parentFolder;
		return me();
	}
	
	public V clearParentFolder() {
		return parentFolder(null);
	}
	
	public V textures(BetterTextureMapping textures) {
		this.textures = textures;
		return me();
	}
	
	public V clearTextures() {
		return parentFolder(null);
	}
	
	public V extraProperties(Consumer<ExtendedModelTemplateBuilder> extraProperties) {
		this.extraProperties = this.extraProperties.andThen(extraProperties);
		return me();
	}
	
	public V clearExtraProperties() {
		extraProperties = builder -> {};
		return me();
	}
	
	public V sideAll(U block) {
		return textures(BetterTextureMapping.sideAll(block));
	}
	
	public V sideAll(U block, String suffix) {
		return textures(BetterTextureMapping.sideAll(block, suffix));
	}
	
	public V sideAll(ResourceLocation texture) {
		return textures(BetterTextureMapping.sideAll(texture));
	}
	
	public V pillar(U block) {
		return textures(BetterTextureMapping.pillar(block));
	}
	
	public V pillar(U block, String suffix) {
		return textures(BetterTextureMapping.pillar(block, suffix));
	}
	
	public V pillar(ResourceLocation side, ResourceLocation end) {
		return textures(BetterTextureMapping.pillar(side, end));
	}
	
	public V complete(U block) {
		return textures(BetterTextureMapping.complete(block));
	}
	
	public V complete(U block, String suffix) {
		return textures(BetterTextureMapping.complete(block, suffix));
	}
	
	public V complete(ResourceLocation side, ResourceLocation top, ResourceLocation bottom) {
		return textures(BetterTextureMapping.complete(side, top, bottom));
	}
	
	public abstract String[] modelPaths();
	
	public String[] modelPaths(U block) {
		return modelPaths();
	}
	
	public V generate(final ResourceLocation modelFolder, String[] modelPaths, BlockModelGenerators generator) {
		ExtendedModelTemplateBuilder builder = new ExtendedModelTemplateBuilder();
		extraProperties.accept(builder);
		for (String model : modelPaths()) {
			if (parentFolder != null) builder.parent(parentFolder.withSuffix(model));
			final ResourceLocation modelLoc = modelFolder.withSuffix(model);
			final ExtendedModelTemplate template = builder.build();
			final BetterTextureMapping textures = this.textures;
			generator.modelOutput.accept(modelLoc, () -> {
				JsonObject root = template.createBaseTemplate(modelLoc, Collections.emptyMap());
				if (textures != null) root.add("textures", textures.toJson());
				return root;
			});
		}
		return me();
	}
	
	public V generate(ResourceLocation modelFolder, BlockModelGenerators generator) {
		return generate(modelFolder, modelPaths(), generator);
	}
	
	public V generate(U block, BlockModelGenerators generator) {
		ResourceLocation folder = ModelLocationUtils.getModelLocation(block);
		generate(folder, modelPaths(block), generator);
		generator.blockStateOutput.accept(generator(block, folder));
		return me();
	}
}
