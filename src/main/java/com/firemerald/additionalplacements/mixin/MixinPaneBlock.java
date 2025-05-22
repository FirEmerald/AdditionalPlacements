package com.firemerald.additionalplacements.mixin;

import com.firemerald.additionalplacements.block.interfaces.IPaneConnectable;
import net.minecraft.block.BlockState;
import net.minecraft.block.FourWayBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PaneBlock.class)
public class MixinPaneBlock {
    @Inject(method = "getStateForPlacement(Lnet/minecraft/item/BlockItemUseContext;)Lnet/minecraft/block/BlockState;", at = @At("RETURN"), cancellable = true)
    public void getStateForPlacement(BlockItemUseContext pContext, CallbackInfoReturnable<BlockState> cir) {
        IBlockReader level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        BlockState state = cir.getReturnValue();
        state = updateConnect(state, level.getBlockState(pos.north()), Direction.NORTH);
        state = updateConnect(state, level.getBlockState(pos.east()), Direction.EAST);
        state = updateConnect(state, level.getBlockState(pos.south()), Direction.SOUTH);
        state = updateConnect(state, level.getBlockState(pos.west()), Direction.WEST);
        cir.setReturnValue(state);
    }

    @Inject(method = "updateShape(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/Direction;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", at = @At("RETURN"), cancellable = true)
    public void updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, IWorld pLevel, BlockPos pCurrentPos, BlockPos pFacingPos, CallbackInfoReturnable<BlockState> cir) {
        if (pFacing.getAxis().isHorizontal()) cir.setReturnValue(updateConnect(cir.getReturnValue(), pFacingState, pFacing));
    }

    private BlockState updateConnect(BlockState currentState, BlockState theirState, Direction connectFace) {
        BooleanProperty prop = FourWayBlock.PROPERTY_BY_DIRECTION.get(connectFace);
        if (!currentState.getValue(prop) && connectOverride(theirState, connectFace)) {
            return currentState.setValue(prop, true);
        } else {
            return currentState;
        }
    }

    private boolean connectOverride(BlockState theirState, Direction connectFace) {
        return theirState.getBlock() instanceof IPaneConnectable && ((IPaneConnectable) theirState.getBlock()).paneConnectOverride(theirState, Direction.Axis.Y, connectFace);
    }
}
