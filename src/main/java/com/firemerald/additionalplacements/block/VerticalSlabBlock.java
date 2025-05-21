package com.firemerald.additionalplacements.block;

import java.util.function.Consumer;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.interfaces.ISimpleRotationBlock;
import com.firemerald.additionalplacements.block.interfaces.ISlabBlock;
import com.firemerald.additionalplacements.block.interfaces.IStateFixer;
import com.firemerald.additionalplacements.client.models.definitions.SlabModels;
import com.firemerald.additionalplacements.client.models.definitions.StateModelDefinition;
import com.firemerald.additionalplacements.config.APConfigs;
import com.firemerald.additionalplacements.util.BlockRotation;
import com.firemerald.additionalplacements.util.VoxelShapes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class VerticalSlabBlock extends AdditionalPlacementLiquidBlock<SlabBlock> implements ISlabBlock<SlabBlock>, ISimpleRotationBlock, IStateFixer
{
	public static final EnumProperty<Axis> AXIS = AdditionalBlockStateProperties.HORIZONTAL_AXIS;

	public static VerticalSlabBlock of(SlabBlock slab)
	{
		return new VerticalSlabBlock(slab);
	}

	public boolean rotateLogic = true, rotateModel = true, rotateTex = true;

	private VerticalSlabBlock(SlabBlock slab)
	{
		super(slab);
		this.registerDefaultState(copyProperties(getOtherBlockState(), this.stateDefinition.any()).setValue(AXIS, Axis.Z));
		((IVanillaSlabBlock) slab).setOtherBlock(this);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(AXIS);
		super.createBlockStateDefinition(builder);
	}

	@Override
	public VoxelShape getShapeInternal(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
        return switch (state.getValue(SlabBlock.TYPE)) {
            case DOUBLE -> VoxelShapes.BLOCK;
            case TOP -> state.getValue(AXIS) == Axis.Z ? VoxelShapes.SLAB_SOUTH : VoxelShapes.SLAB_EAST;
            case BOTTOM -> state.getValue(AXIS) == Axis.Z ? VoxelShapes.SLAB_NORTH : VoxelShapes.SLAB_WEST;
        };
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext context)
	{
		return enablePlacement(context.getClickedPos(), context.getLevel(), context.getClickedFace(), context.getPlayer()) && canBeReplacedImpl(state, context);
	}

	@Override
	public Direction getPlacing(BlockState blockState)
	{
		SlabType type = blockState.getValue(SlabBlock.TYPE);
		return type == SlabType.DOUBLE ? null : Direction.fromAxisAndDirection(blockState.getValue(VerticalSlabBlock.AXIS), type == SlabType.TOP ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE);
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
		return "slab";
	}

	@Override
	public String getTagTypeNamePlural()
	{
		return "slabs";
	}

	@Override
	public BlockState updateShapeImpl(BlockState state, Direction direction, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos)
	{
		return state;
	}

	@Override
	public boolean rotatesLogic(BlockState state) {
		return rotateLogic;
	}

	@Override
	public boolean rotatesTexture(BlockState state) {
		return rotateTex;
	}

	@Override
	public boolean rotatesModel(BlockState state) {
		return rotateModel;
	}

	@Override
	public BlockRotation getRotation(BlockState state) {
		return state.getValue(AXIS) == Axis.X ?
				BlockRotation.X_270_Y_270 :
					BlockRotation.X_270;
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
	public BlockState withUnrotatedPlacement(BlockState worldState, BlockState modelState) {
		return modelState; //no changes needed
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ResourceLocation getBaseModelPrefix() {
		return SlabModels.BASE_MODEL_FOLDER;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public StateModelDefinition getModelDefinition(BlockState state) {
		return SlabModels.getModel(state);
	}

	@Override
	public CompoundTag fix(CompoundTag properties, Consumer<Block> changeBlock) {
		if (APConfigs.common().fixOldStates.get()) {
			if (!IStateFixer.contains(properties, AXIS)) {
				if (IStateFixer.contains(properties, BlockStateProperties.HORIZONTAL_FACING) && !(
						IStateFixer.contains(properties, BlockStateProperties.HORIZONTAL_AXIS) &&
						IStateFixer.contains(properties, BlockStateProperties.SLAB_TYPE))) {
                    AdditionalPlacementsMod.LOGGER.debug("{} Fixing V1 slab block state: {}", this, properties);
					Direction facing = IStateFixer.getProperty(properties, BlockStateProperties.HORIZONTAL_FACING);
					if (facing != null) {
						IStateFixer.setProperty(properties, AXIS, facing.getAxis());
						IStateFixer.setProperty(properties, SlabBlock.TYPE, facing.getAxisDirection() == AxisDirection.POSITIVE ? SlabType.TOP : SlabType.BOTTOM);
						IStateFixer.remove(properties, BlockStateProperties.HORIZONTAL_FACING);
					}
				} else if (IStateFixer.contains(properties, BlockStateProperties.HORIZONTAL_AXIS)) {
                    AdditionalPlacementsMod.LOGGER.debug("{} Fixing V2 slab block state: {}", this, properties);
					IStateFixer.renameProperty(properties, BlockStateProperties.HORIZONTAL_AXIS, AXIS);
				}
			}
		}
		return properties;
	}
}
