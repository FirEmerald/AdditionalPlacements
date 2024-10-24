package com.firemerald.additionalplacements.block;

import com.firemerald.additionalplacements.block.interfaces.ISimpleRotationBlock;
import com.firemerald.additionalplacements.block.interfaces.IStairBlock;
import com.firemerald.additionalplacements.util.*;
import com.firemerald.additionalplacements.util.stairs.CompressedStairFacing;
import com.firemerald.additionalplacements.util.stairs.CompressedStairShape;
import com.firemerald.additionalplacements.util.stairs.StairConnections;
import com.firemerald.additionalplacements.util.stairs.StairShape;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VerticalStairBlock extends AdditionalPlacementLiquidBlock<StairBlock> implements IStairBlock<StairBlock>, ISimpleRotationBlock
{
	public static final EnumProperty<CompressedStairFacing> FACING = EnumProperty.create("facing", CompressedStairFacing.class, CompressedStairFacing.ALL_FACINGS);

	private static StairConnections staticAllowedConnections;
	
	public static VerticalStairBlock of(StairBlock stairs, StairConnections allowedConnections)
	{
		staticAllowedConnections = allowedConnections;
		VerticalStairBlock ret = new VerticalStairBlock(stairs, allowedConnections);
		staticAllowedConnections = null;
		return ret;
	}
	
	public final StairConnections allowedConnections;
	public boolean rotateLogic = false, rotateModel = false, rotateTex = false;

	@SuppressWarnings("deprecation")
	private VerticalStairBlock(StairBlock stairs, StairConnections allowedConnections)
	{
		super(stairs);
		this.registerDefaultState(copyProperties(getModelState(), this.stateDefinition.any()).setValue(FACING, CompressedStairFacing.SOUTH_UP_EAST).setValue(allowedConnections.shapeProperty, CompressedStairShape.VERTICAL_STRAIGHT));
		((IVanillaStairBlock) stairs).setOtherBlock(this);
		this.allowedConnections = allowedConnections;
	}

	@Override
	public EnumProperty<CompressedStairShape> shapeProperty() {
		return allowedConnections.shapeProperty;
	}

	@Override
	protected boolean isValidProperty(Property<?> prop) {
		return prop != StairBlock.FACING && prop != StairBlock.HALF && prop != StairBlock.SHAPE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, staticAllowedConnections.shapeProperty);
		super.createBlockStateDefinition(builder);
	}

	@Override
	public VoxelShape getShapeInternal(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return state.getValue(allowedConnections.shapeProperty).getVoxelShape(state.getValue(FACING));
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
	public BlockState withUnrotatedPlacement(BlockState worldState, BlockState modelState) {
		CompressedStairFacing compressedFacing = worldState.getValue(FACING);
		CompressedStairShape compressedShape = worldState.getValue(allowedConnections.shapeProperty);
		ComplexFacing facing = compressedShape.facingType.fromCompressedFacing(compressedFacing);
		StairShape shape = compressedShape.shape;
		return modelState
				.setValue(StairBlock.FACING, facing.vanillaStairsFacing)
				.setValue(StairBlock.HALF, facing.vanillaStairsHalf)
				.setValue(StairBlock.SHAPE, facing.vanillaStairsHalf == Half.TOP ? shape.vanillaTopShape : shape.vanillaBottomShape);
	}
	
	public boolean canRotate(BlockState state) {
		CompressedStairFacing compressedFacing = state.getValue(FACING);
		CompressedStairShape compressedShape = state.getValue(allowedConnections.shapeProperty);
		ComplexFacing facing = compressedShape.facingType.fromCompressedFacing(compressedFacing);
		StairShape shape = compressedShape.shape;
		return (facing.vanillaStairsHalf == Half.TOP ? shape.vanillaTopShape : shape.vanillaBottomShape) != null;
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
	public BlockRotation getRotation(BlockState state) {
		CompressedStairFacing compressedFacing = state.getValue(FACING);
		CompressedStairShape compressedShape = state.getValue(allowedConnections.shapeProperty);
		return compressedShape.facingType.fromCompressedFacing(compressedFacing).stairsModelRotation;
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
	public StairConnections allowedConnections() {
		return this.allowedConnections;
	}

	@Override
	public ResourceLocation getDynamicBlockstateJson() {
		return allowedConnections.dynamicBlockstateJson;
	}
}
