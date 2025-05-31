package com.firemerald.additionalplacements.block.stairs;

import java.util.function.Consumer;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.AdditionalPlacementLiquidBlock;
import com.firemerald.additionalplacements.block.interfaces.ISimpleRotationBlock;
import com.firemerald.additionalplacements.block.interfaces.IStairBlock;
import com.firemerald.additionalplacements.block.interfaces.IStateFixer;
import com.firemerald.additionalplacements.block.stairs.common.CommonStairShapeState;
import com.firemerald.additionalplacements.block.stairs.v1.V1StairPlacing;
import com.firemerald.additionalplacements.block.stairs.v1.V1StairShape;
import com.firemerald.additionalplacements.block.stairs.v1.V1StairShapeState;
import com.firemerald.additionalplacements.block.stairs.v2.V2StairFacing;
import com.firemerald.additionalplacements.block.stairs.v2.V2StairShape;
import com.firemerald.additionalplacements.block.stairs.v2.V2StairShapeState;
import com.firemerald.additionalplacements.block.stairs.vanilla.VanillaStairShapeState;
import com.firemerald.additionalplacements.client.models.definitions.StairModels;
import com.firemerald.additionalplacements.client.models.definitions.StateModelDefinition;
import com.firemerald.additionalplacements.config.APConfigs;
import com.firemerald.additionalplacements.util.*;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AdditionalStairBlock extends AdditionalPlacementLiquidBlock<StairsBlock> implements IStairBlock<StairsBlock>, ISimpleRotationBlock, IStateFixer
{
	private static StairConnectionsType connectionsTypeStatic;
	
	public static AdditionalStairBlock of(StairsBlock stairs, StairConnectionsType connectionsType)
	{
		connectionsTypeStatic = connectionsType;
		AdditionalStairBlock ret = new AdditionalStairBlock(stairs, connectionsType);
		connectionsTypeStatic = null;
		return ret;
	}

	public final StairConnectionsType connectionsType;
	public boolean rotateLogic = false, rotateModel = false, rotateTex = false;

	private AdditionalStairBlock(StairsBlock stairs, StairConnectionsType connectionsType)
	{
		super(stairs);
		this.connectionsType = connectionsType;
		this.registerDefaultState(copyProperties(getOtherBlockState(), this.stateDefinition.any()).setValue(connectionsType, connectionsType.defaultShapeState));
		((IVanillaStairBlock) stairs).setOtherBlock(this);
	}

	@Override
	public boolean isValidProperty(Property<?> prop) {
		return prop != StairsBlock.FACING && prop != StairsBlock.HALF && prop != StairsBlock.SHAPE;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(connectionsTypeStatic);
		super.createBlockStateDefinition(builder);
	}

	@Override
	public BlockState getDefaultVanillaState(BlockState currentState)
	{
		return currentState.is(parentBlock) ? currentState : copyProperties(currentState, parentBlock.defaultBlockState());
	}

	@Override
	public BlockState getDefaultAdditionalState(BlockState currentState)
	{
		return currentState.is(this) ? currentState : copyProperties(currentState, this.defaultBlockState());
	}

	@Override
	public String getTagTypeName()
	{
		return "stair";
	}

	@Override
	public String getTagTypeNamePlural()
	{
		return "stairs";
	}

	@Override
	protected BlockState mapState(BlockState ourState, BlockState theirStateWithProperties) {
		VanillaStairShapeState modelShapeState = getShapeState(ourState).model();
		return theirStateWithProperties
				.setValue(StairsBlock.FACING, modelShapeState.facing)
				.setValue(StairsBlock.HALF, modelShapeState.half)
				.setValue(StairsBlock.SHAPE, modelShapeState.shape);
	}
	
	public boolean canRotate(BlockState state) {
		return this.getShapeState(state).isRotatedModel();
	}

	@Override
	public BlockRotation getRotation(BlockState state) {
		return this.getShapeState(state).modelRotation();
	}

	@Override
	public boolean rotatesLogic(BlockState state) {
		return rotateLogic && canRotate(state);
	}

	@Override
	public boolean rotatesTexture(BlockState state) {
		return rotateTex && canRotate(state);
	}

	@Override
	public boolean rotatesModel(BlockState state) {
		return rotateModel && canRotate(state);
	}

	@Override
	public void setLogicRotation(boolean useLogicRotation) {
		this.rotateLogic = useLogicRotation;
	}

	@Override
	public void setModelRotation(boolean useTexRotation, boolean useModelRotation) {
		this.rotateTex = useTexRotation;
		this.rotateModel = useModelRotation;
	}
	
	@Override
	public VoxelShape getShapeInternal(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext context)
	{
		CommonStairShapeState shapeState = this.getShapeState(state);
		return shapeState.shape.getVoxelShape(shapeState.facing);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ResourceLocation getBaseModelPrefix() {
		return StairModels.BASE_MODEL_FOLDER;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public StateModelDefinition getModelDefinition(BlockState state) {
		return StairModels.getModelDefinition(getShapeState(state));
	}

	@Override
	public CompoundNBT fix(CompoundNBT properties, Consumer<Block> changeBlock) {
		CommonStairShapeState commonShape = getOldPropertyShapeState(properties);
		if (commonShape != null) applyState(commonShape, properties, changeBlock);
		return properties;
	}
	
	public CommonStairShapeState getOldPropertyShapeState(CompoundNBT properties) {
		if (APConfigs.common().fixStates.get()) {
			if (IStateFixer.contains(properties, connectionsType)) {
                AdditionalPlacementsMod.LOGGER.debug("{} Potentially fixing V3 stair block state: {}", this, properties);
				String shapeStateName = IStateFixer.getPropertyString(properties, connectionsType);
				if (!connectionsType.isValid(shapeStateName)) {
                    AdditionalPlacementsMod.LOGGER.debug("{} Fixing V3 stair block state", this);
					return CommonStairShapeState.get(shapeStateName);
				}
			} else if (APConfigs.common().fixOldStates.get()) {
				if (properties.contains("shape")) {
					if (properties.contains("facing")) { //potentially V2
                        AdditionalPlacementsMod.LOGGER.debug("{} Potentially fixing potential V2 stair block state: {}", this, properties);
						V2StairFacing facing = V2StairFacing.get(properties.getString("facing"));
						V2StairShape shape = V2StairShape.get(properties.getString("shape"));
						if (facing != null && shape != null) { //V2
                            AdditionalPlacementsMod.LOGGER.debug("{} Fixing V2 stair block state", this);
							properties.remove("facing");
							properties.remove("shape");
							return V2StairShapeState.toCommon(facing, shape);
						}
					} else if (properties.contains("placing")) { //potentially V1
                        AdditionalPlacementsMod.LOGGER.debug("{} Potentially fixing potential V1 stair block state: {}", this, properties);
						V1StairPlacing placing = V1StairPlacing.get(properties.getString("placing"));
						V1StairShape shape = V1StairShape.get(properties.getString("shape"));
						if (placing != null && shape != null) { //V1
                            AdditionalPlacementsMod.LOGGER.debug("{} Fixing V1 stair block state", this);
							properties.remove("placing");
							properties.remove("shape");
							return V1StairShapeState.toCommon(placing, shape);
						}
					}
				}
			}
		}
		return null;
	}
	
	public void applyState(CommonStairShapeState commonShapeState, CompoundNBT properties, Consumer<Block> changeBlock) {
		if (!connectionsType.allowFlipped && commonShapeState.isComplexFlipped) commonShapeState = commonShapeState.flipped();
		VanillaStairShapeState vanillaShapeState = commonShapeState.vanilla();
		if (vanillaShapeState == null && !connectionsType.isValid(commonShapeState)) { //not valid
			commonShapeState = commonShapeState.closestVanillaShape;
			vanillaShapeState = commonShapeState.vanilla();
		}
		if (vanillaShapeState != null) { //make vanilla
			changeBlock.accept(getOtherBlock());
			IStateFixer.setProperty(properties, StairsBlock.FACING, vanillaShapeState.facing);
			IStateFixer.setProperty(properties, StairsBlock.HALF, vanillaShapeState.half);
			IStateFixer.setProperty(properties, StairsBlock.SHAPE, vanillaShapeState.shape);
		} else {
			IStateFixer.setProperty(properties, connectionsType, commonShapeState);
		}
	}

	@Override
	public BlockState getBlockStateInternal(CommonStairShapeState commonShapeState, BlockState currentState)
	{
		if (!connectionsType.allowFlipped && commonShapeState.isComplexFlipped) return getBlockState(commonShapeState.flipped(), currentState);
		else {
			if (!connectionsType.isValid(commonShapeState)) return getBlockState(commonShapeState.closestVanillaShape, currentState);
			return getDefaultAdditionalState(currentState).setValue(connectionsType, commonShapeState);
		}
	}

	@Override
	public CommonStairShapeState getShapeState(BlockState blockState) {
		return blockState.getValue(connectionsType);
	}

	@Override
	public StairConnectionsType connectionsType() {
		return connectionsType;
	}
}
