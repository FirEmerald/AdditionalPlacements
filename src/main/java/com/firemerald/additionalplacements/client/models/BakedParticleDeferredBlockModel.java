package com.firemerald.additionalplacements.client.models;

import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.BakedModelWrapper;

public class BakedParticleDeferredBlockModel extends BakedModelWrapper<BakedModel>
{
	private static final Map<Pair<BakedModel, ResourceLocation>, BakedParticleDeferredBlockModel> CACHE = new HashMap<>();
	
	public synchronized static BakedParticleDeferredBlockModel of(BakedModel model, TextureAtlasSprite particle)
	{
		return CACHE.computeIfAbsent(Pair.of(model, particle.atlasLocation()), k -> new BakedParticleDeferredBlockModel(model, particle));
	}
	
	public static void clearCache()
	{
		CACHE.clear();
	}
	
	public final TextureAtlasSprite particle;
	
	private BakedParticleDeferredBlockModel(BakedModel model, TextureAtlasSprite particle)
	{
		super(model);
		this.particle = particle;
	}
	
	@Override
	public TextureAtlasSprite getParticleIcon()
	{
		return particle;
	}
}
