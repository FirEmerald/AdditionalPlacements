package com.firemerald.additionalplacements.generation;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.block.interfaces.ISimpleRotationBlock;

import com.firemerald.additionalplacements.config.blocklist.Blocklist;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

public class SimpleRotatableGenerationType<T extends Block, U extends AdditionalPlacementBlock<T> & ISimpleRotationBlock> extends SimpleGenerationType<T, U> {
	public abstract static class BuilderBase<T extends Block, U extends AdditionalPlacementBlock<T> & ISimpleRotationBlock, V extends SimpleRotatableGenerationType<T, U>, W extends BuilderBase<T, U, V, W>> extends SimpleGenerationType.BuilderBase<T, U, V, W> {
		protected Blocklist
				logicRotationEnabled = new Blocklist(true, true),
				textureRotationEnabled = new Blocklist(true, true),
				modelRotationEnabled = new Blocklist(true, true);

		public W logicRotationEnabled(Blocklist enabled) {
			this.logicRotationEnabled = enabled;
			return me();
		}

		public W textureRotationEnabled(Blocklist enabled) {
			this.textureRotationEnabled = enabled;
			return me();
		}

		public W modelRotationEnabled(Blocklist enabled) {
			this.modelRotationEnabled = enabled;
			return me();
		}
	}

	public static class Builder<T extends Block, U extends AdditionalPlacementBlock<T> & ISimpleRotationBlock> extends BuilderBase<T, U, SimpleRotatableGenerationType<T, U>, Builder<T, U>> {
		@Override
		public SimpleRotatableGenerationType<T, U> construct(ResourceLocation name, String description) {
			return new SimpleRotatableGenerationType<>(name, description, this);
		}
	}

	private final Blocklist logicRotationEnabled, textureRotationEnabled, modelRotationEnabled;

	protected SimpleRotatableGenerationType(ResourceLocation name, String description, BuilderBase<T, U, ?, ?> builder) {
		super(name, description, builder);
		this.logicRotationEnabled = builder.logicRotationEnabled;
		this.textureRotationEnabled = builder.textureRotationEnabled;
		this.modelRotationEnabled = builder.modelRotationEnabled;
	}

	@Override
	public void buildClientConfig(ForgeConfigSpec.Builder builder) {
		super.buildClientConfig(builder);
		textureRotationEnabled.addToConfig(builder, "rotated_textures", "Blocklist to control which blocks will rotate the textures of their original blocks.");
		modelRotationEnabled.addToConfig(builder, "rotated_models", "Blocklist to control which blocks will use \"rotated models\" of their original blocks.");
	}

	@Override
	public void loadClientConfig() {
		super.loadClientConfig();
		textureRotationEnabled.loadListsFromConfig();
		modelRotationEnabled.loadListsFromConfig();
	}

	@Override
	public void updateClientSettings() {
		super.updateClientSettings();
		forEachCreated(entry -> entry.newBlock.setModelRotation(textureRotationEnabled.testOriginal(entry), modelRotationEnabled.testOriginal(entry)));
	}

	@Override
	public void buildServerConfig(ForgeConfigSpec.Builder builder) {
		super.buildServerConfig(builder);
		logicRotationEnabled.addToConfig(builder, "rotated_logic", "Blocklist to control which blocks will use \"rotated logic\" of their original blocks. Mainly affects bounding boxes.");
	}

	@Override
	public void loadServerConfig() {
		super.loadServerConfig();
		logicRotationEnabled.loadListsFromConfig();
	}

	@Override
	public void updateServerSettings() {
		super.updateServerSettings();
		forEachCreated(entry -> entry.newBlock.setLogicRotation(logicRotationEnabled.testOriginal(entry)));
	}
}
