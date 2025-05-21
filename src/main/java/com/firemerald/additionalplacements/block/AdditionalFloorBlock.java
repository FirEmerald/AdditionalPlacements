package com.firemerald.additionalplacements.block;

import java.util.function.Consumer;

import com.firemerald.additionalplacements.AdditionalPlacementsMod;
import com.firemerald.additionalplacements.block.interfaces.IFloorBlock;
import com.firemerald.additionalplacements.block.interfaces.ISimpleRotationBlock;
import com.firemerald.additionalplacements.block.interfaces.IStateFixer;
import com.firemerald.additionalplacements.config.APConfigs;
import com.firemerald.additionalplacements.util.BlockRotation;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public abstract class AdditionalFloorBlock<T extends Block> extends AdditionalPlacementBlock<T> implements IFloorBlock<T>, ISimpleRotationBlock, IStateFixer
{
	private boolean rotateLogic = true, rotateTex = true, rotateModel = true;
	public static final EnumProperty<Direction> PLACING = AdditionalBlockStateProperties.HORIZONTAL_OR_UP_PLACING;

	public AdditionalFloorBlock(T block, ResourceKey<Block> id)
	{
		super(block, id);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(PLACING);
		super.createBlockStateDefinition(builder);
	}

	@Override
	public Direction getPlacing(BlockState blockState)
	{
		return blockState.getValue(PLACING);
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
	public BlockState withUnrotatedPlacement(BlockState worldState, BlockState modelState) {
		return modelState;
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
		return switch (state.getValue(PLACING)) {
			case NORTH -> BlockRotation.X_270;
			case EAST -> BlockRotation.X_270_Y_90;
			case SOUTH -> BlockRotation.X_270_Y_180;
			case WEST -> BlockRotation.X_270_Y_270;
			case UP -> BlockRotation.X_180;
			default -> BlockRotation.IDENTITY;
		};
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
	public CompoundTag fix(CompoundTag properties, Consumer<Block> changeBlock) {
		if (APConfigs.common().fixOldStates.get()) {
			if (!IStateFixer.contains(properties, PLACING)) {
				if (properties.contains("facing")) {
                    AdditionalPlacementsMod.LOGGER.debug("{} Fixing V1 floor block state: {}", this, properties);
					IStateFixer.renameProperty(properties, "facing", PLACING);
				}
			}
		}
		return properties;
	}
}
