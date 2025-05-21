package com.firemerald.additionalplacements.generation;

import java.util.function.Function;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.*;
import com.firemerald.additionalplacements.block.interfaces.ISimpleRotationBlock;
import com.firemerald.additionalplacements.block.stairs.AdditionalStairBlock;
import com.firemerald.additionalplacements.config.blocklist.Blocklist;
import com.firemerald.additionalplacements.config.blocklist.IDBlocklistEntry;
import com.firemerald.additionalplacements.generation.GenerationType.BuilderBase;

import net.minecraft.block.*;
import net.minecraft.util.ResourceLocation;

public class APGenerationTypes implements RegistrationInitializer {
	private static SimpleRotatableGenerationType<SlabBlock, VerticalSlabBlock> slab;
	private static VerticalStairsGenerationType<StairsBlock, AdditionalStairBlock> stairs;
	private static SimpleRotatableGenerationType<CarpetBlock, AdditionalCarpetBlock> carpet;
	private static SimpleRotatableGenerationType<PressurePlateBlock, AdditionalPressurePlateBlock> pressurePlate;
	private static SimpleRotatableGenerationType<WeightedPressurePlateBlock, AdditionalWeightedPressurePlateBlock> weightedPressurePlate;

	@Override
	public void onInitializeRegistration(IRegistration register) {
		slab                    = get(register, SlabBlock.class                 , "slab"                   , "Slabs"                   ,
				new SimpleRotatableGenerationType.Builder<SlabBlock, VerticalSlabBlock>()
				.modelRotationEnabled(new Blocklist(true, true,
						new IDBlocklistEntry(false, new ResourceLocation("minecraft", "sandstone_slab")),
						new IDBlocklistEntry(false, new ResourceLocation("minecraft", "cut_sandstone_slab")),
						new IDBlocklistEntry(false, new ResourceLocation("minecraft", "red_sandstone_slab")),
						new IDBlocklistEntry(false, new ResourceLocation("minecraft", "cut_red_sandstone_slab"))))
				.textureRotationEnabled(new Blocklist(true, true,
						new IDBlocklistEntry(false, new ResourceLocation("minecraft", "smooth_stone_slab"))))
				.constructor(VerticalSlabBlock::of)
				.addsProperties("axis"));
		stairs                  = get(register, StairsBlock.class                , "stairs"                 , "Stairs"                  ,
				new VerticalStairsGenerationType.Builder<StairsBlock, AdditionalStairBlock>()
				.modelRotationEnabled(new Blocklist(true, true,
						new IDBlocklistEntry(false, new ResourceLocation("minecraft", "sandstone_stairs")),
						new IDBlocklistEntry(false, new ResourceLocation("minecraft", "red_sandstone_stairs"))))
				.addsProperties("front_top_shape"));
		carpet                  = get(register, CarpetBlock.class               , "carpet"                 , "Carpets"                 , AdditionalCarpetBlock::of,                "facing");
		pressurePlate           = get(register, PressurePlateBlock.class        , "pressure_plate"         , "Regular pressure plates" , AdditionalPressurePlateBlock::of,         "facing");
		weightedPressurePlate   = get(register, WeightedPressurePlateBlock.class, "weighted_pressure_plate", "Weighted pressure plates", AdditionalWeightedPressurePlateBlock::of, "facing");
	}

	private static <T extends Block, U extends AdditionalPlacementBlock<T> & ISimpleRotationBlock> SimpleRotatableGenerationType<T, U> get(IRegistration register, Class<T> clazz, String name, String description, Function<? super T, ? extends U> constructor, String... addsProperties) {
		return register.registerType(clazz, new ResourceLocation(AdditionalPlacementsMod.MOD_ID, name), description, new SimpleRotatableGenerationType.Builder<T, U>().constructor(constructor).addsProperties(addsProperties));
	}

	private static <T extends Block, U extends AdditionalPlacementBlock<T>, V extends GenerationType<T, U>> V get(IRegistration register, Class<T> clazz, String name, String description, BuilderBase<T, U, V, ?> builder) {
		return register.registerType(clazz, new ResourceLocation(AdditionalPlacementsMod.MOD_ID, name), description, builder);
	}

	public static SimpleRotatableGenerationType<SlabBlock, VerticalSlabBlock> slab() {
		return slab;
	}

	public static VerticalStairsGenerationType<StairsBlock, AdditionalStairBlock> stairs() {
		return stairs;
	}

	public static SimpleRotatableGenerationType<CarpetBlock, AdditionalCarpetBlock> carpet() {
		return carpet;
	}

	public static SimpleRotatableGenerationType<PressurePlateBlock, AdditionalPressurePlateBlock> pressurePlate() {
		return pressurePlate;
	}

	public static SimpleRotatableGenerationType<WeightedPressurePlateBlock, AdditionalWeightedPressurePlateBlock> weightedPressurePlate() {
		return weightedPressurePlate;
	}

}
