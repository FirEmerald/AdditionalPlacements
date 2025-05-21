package com.firemerald.additionalplacements.commands;

import java.util.Set;

import com.firemerald.additionalplacements.block.interfaces.IStairBlock;
import com.firemerald.additionalplacements.block.stairs.common.CommonStairShape;
import com.firemerald.additionalplacements.util.ComplexFacing;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class CommandGenerateStairsDebugger
{
	public static void register(CommandDispatcher<CommandSourceStack> dispatch)
	{
		dispatch.register(Commands.literal("ap_stairs_state_debug")
				.requires(source -> source.hasPermission(2))
				.then(Commands.argument("pos", BlockPosArgument.blockPos())
						.then(Commands.argument("block", BlockStateArgument.block())
								.executes(context -> {
									BlockPos center = BlockPosArgument.getLoadedBlockPos(context, "pos");
									BlockInput blockInput = BlockStateArgument.getBlock(context, "block");
									if (blockInput.getState().getBlock() instanceof IStairBlock<?> stair && stair.hasAdditionalStates()) {
										ServerLevel serverLevel = context.getSource().getLevel();
										boolean allowMixed = stair.connectionsType().allowMixed;
										boolean allowVertical = stair.connectionsType().allowVertical;
										int minorOffset = allowMixed ? 16 : allowVertical ? 9 : 6;
										int majorOffset = minorOffset + 2;
										BlockPos.MutableBlockPos middle = new BlockPos.MutableBlockPos();
										BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
										for (int x = center.getX() - majorOffset - 1; x <= center.getX() + majorOffset + 1; ++x) {
											for (int y = center.getY() - majorOffset - 1; y <= center.getY() + majorOffset + 1; ++y) {
												for (int z = center.getZ() - majorOffset - 1; z <= center.getZ() + majorOffset + 1; ++z) {
													pos.set(x, y, z);
													serverLevel.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
												}
											}
										}
										for (ComplexFacing facing : ComplexFacing.ALL_FACING) {
											BlockState state = stair.getBlockState(facing, CommonStairShape.STRAIGHT, blockInput.getState());
											middle.set(
													center.getX() + facing.up.getStepX() * majorOffset + facing.forward.getStepX() * minorOffset,
													center.getY() + facing.up.getStepY() * majorOffset + facing.forward.getStepY() * minorOffset,
													center.getZ() + facing.up.getStepZ() * majorOffset + facing.forward.getStepZ() * minorOffset
													);
											CompoundTag tag = null; //TODO tag?
											Set<Property<?>> properties = blockInput.getDefinedProperties();
											new BlockInput(state, properties, tag).place(serverLevel, middle, 2);
											{
												int offset = 0;
												                   set(     serverLevel, stair, state, properties, tag, facing, middle, pos, offset += 2 ,  1,  0);
												if (allowVertical) set(     serverLevel, stair, state, properties, tag, facing, middle, pos, offset += 2 ,  0,  1);
												if (allowMixed)    set(     serverLevel, stair, state, properties, tag, facing, middle, pos, offset += 2 ,  1,  1);
												                   set(     serverLevel, stair, state, properties, tag, facing, middle, pos, offset += 2 , -1,  0);
												if (allowVertical) set(     serverLevel, stair, state, properties, tag, facing, middle, pos, offset += 2,  0, -1);
												if (allowMixed)    set(     serverLevel, stair, state, properties, tag, facing, middle, pos, offset += 2, -1, -1);
												if (allowMixed)    setTwist(serverLevel, stair, state, properties, tag, facing, middle, pos, offset += 2);
											}
										}
									}
									else throwInvalidBlock(blockInput.getState());
									return 1;
								}))));
	}

	private static void throwInvalidBlock(BlockState state) throws CommandSyntaxException {
		throw new SimpleCommandExceptionType(new TranslatableComponent("commands.ap_stairs_state_debug.wrong_block", state.getBlock().getRegistryName().toString())).create();
	}

	private static void set(ServerLevel serverLevel, IStairBlock<?> stair, BlockState rootState, Set<Property<?>> props, CompoundTag tag, ComplexFacing facing, BlockPos middle, BlockPos.MutableBlockPos pos, int offset, int offFront, int offTop) {
		new BlockInput(rootState, props, tag).place(serverLevel, pos.set(middle).move(facing.left, offset), 2);
		new BlockInput(rootState, props, tag).place(serverLevel, pos.set(middle).move(facing.right, offset), 2);
		if (offFront != 0) {
			new BlockInput(stair.getBlockState(ComplexFacing.forFacing(facing.left, facing.up),
					CommonStairShape.STRAIGHT, rootState), props, tag).place(serverLevel, pos.set(middle).move(facing.left, offset).move(facing.forward, offFront), 2);
			new BlockInput(stair.getBlockState(ComplexFacing.forFacing(facing.right, facing.up),
					CommonStairShape.STRAIGHT, rootState), props, tag).place(serverLevel, pos.set(middle).move(facing.right, offset).move(facing.forward, offFront), 2);
		}
		if (offTop != 0) {
			new BlockInput(stair.getBlockState(ComplexFacing.forFacing(facing.forward, facing.left),
					CommonStairShape.STRAIGHT, rootState), props, tag).place(serverLevel, pos.set(middle).move(facing.left, offset).move(facing.up, offTop), 2);
			new BlockInput(stair.getBlockState(ComplexFacing.forFacing(facing.forward, facing.right),
					CommonStairShape.STRAIGHT, rootState), props, tag).place(serverLevel, pos.set(middle).move(facing.right, offset).move(facing.up, offTop), 2);
		}
	}

	private static void setTwist(ServerLevel serverLevel, IStairBlock<?> stair, BlockState rootState, Set<Property<?>> props, CompoundTag tag, ComplexFacing facing, BlockPos middle, BlockPos.MutableBlockPos pos, int offset) {
		new BlockInput(rootState, props, tag).place(serverLevel, pos.set(middle).move(facing.left, offset), 2);
		new BlockInput(rootState, props, tag).place(serverLevel, pos.set(middle).move(facing.right, offset), 2);
		new BlockInput(stair.getBlockState(ComplexFacing.forFacing(facing.left, facing.up),
				CommonStairShape.STRAIGHT, rootState), props, tag).place(serverLevel, pos.set(middle).move(facing.left, offset).move(facing.backward, 1), 2);
		new BlockInput(stair.getBlockState(ComplexFacing.forFacing(facing.right, facing.up),
				CommonStairShape.STRAIGHT, rootState), props, tag).place(serverLevel, pos.set(middle).move(facing.right, offset).move(facing.backward, 1), 2);
		new BlockInput(stair.getBlockState(ComplexFacing.forFacing(facing.forward, facing.right),
				CommonStairShape.STRAIGHT, rootState), props, tag).place(serverLevel, pos.set(middle).move(facing.left, offset).move(facing.down, 1), 2);
		new BlockInput(stair.getBlockState(ComplexFacing.forFacing(facing.forward, facing.left),
				CommonStairShape.STRAIGHT, rootState), props, tag).place(serverLevel, pos.set(middle).move(facing.right, offset).move(facing.down, 1), 2);
	}
}