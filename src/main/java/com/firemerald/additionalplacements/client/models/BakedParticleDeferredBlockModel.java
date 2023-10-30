package com.firemerald.additionalplacements.client.models;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

public class BakedParticleDeferredBlockModel extends ForwardingBakedModel
{
	private static final Map<Pair<BakedModel, ResourceLocation>, BakedParticleDeferredBlockModel> CACHE = new HashMap<>();
	
	public synchronized static BakedParticleDeferredBlockModel of(BakedModel model, TextureAtlasSprite particle)
	{
		return CACHE.computeIfAbsent(Pair.of(model, particle.getName()), k -> new BakedParticleDeferredBlockModel(model, particle));
	}
	
	public static void clearCache()
	{
		CACHE.clear();
	}
	
	public final TextureAtlasSprite particle;
	
	private BakedParticleDeferredBlockModel(BakedModel model, TextureAtlasSprite particle)
	{
		this.wrapped = model;
		this.particle = particle;
	}
	
	@Override
	public TextureAtlasSprite getParticleIcon()
	{
		return particle;
	}
}