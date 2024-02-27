package com.firemerald.additionalplacements.client;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.firemerald.additionalplacements.block.interfaces.IPlacementBlock;
import com.firemerald.additionalplacements.client.models.BakedParticleDeferredBlockModel;
import com.firemerald.additionalplacements.client.models.PlacementBlockModelLoader;
import com.firemerald.additionalplacements.common.CommonModEvents;

import io.github.fabricators_of_create.porting_lib.event.client.RegisterGeometryLoadersCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ClientModEvents implements ClientModInitializer
{
	public static PlacementBlockModelLoader loader;
	public static final Pack GENERATED_RESOURCES_PACK = new Pack(
			"Additional Placements blockstate redirection pack",
			true,
			BlockstatesPackResources::new,
			Component.literal("title"),
			Component.literal("description"),
			PackCompatibility.COMPATIBLE,
			Pack.Position.BOTTOM,
			true,
			PackSource.BUILT_IN
			);

	@Override
	public void onInitializeClient()
	{
    	ItemTooltipCallback.EVENT.register(ClientModEvents::onItemTooltip);
    	WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register(ClientModEvents::onHighlightBlock);
    	ClientLifecycleEvents.CLIENT_STARTED.register(client -> CommonModEvents.init());
    	ClientLifecycleEvents.CLIENT_STARTED.register(ClientModEvents::init);
    	RegisterGeometryLoadersCallback.EVENT.register(loaders -> loaders.put(PlacementBlockModelLoader.ID, loader = new PlacementBlockModelLoader()));
	}

	private static boolean hasInit = false;

	public static void init(Minecraft client)
	{
		if (!hasInit)
		{
	    	Registry.BLOCK.forEach(block -> {
	    		if (block instanceof AdditionalPlacementBlock)
	    		{
	    			@SuppressWarnings("deprecation")
					BlockState modelState = ((AdditionalPlacementBlock<?>) block).getModelState();
	    			BlockRenderLayerMap.INSTANCE.putBlock(block, ItemBlockRenderTypes.getChunkRenderType(modelState));
	    		}
	    	});
	    	client.getBlockColors().register(new AdditionalBlockColor(), Registry.BLOCK.stream().filter(block -> block instanceof AdditionalPlacementBlock && !((AdditionalPlacementBlock<?>) block).hasCustomColors()).toArray(Block[]::new));
	    	((ReloadableResourceManager) client.getResourceManager()).registerReloadListener(ClientModEvents.loader);
			((ReloadableResourceManager) client.getResourceManager()).registerReloadListener((ResourceManagerReloadListener) (v -> BakedParticleDeferredBlockModel.clearCache()));
			hasInit = true;
		}
	}

	public static void onItemTooltip(ItemStack stack, TooltipFlag context, List<Component> lines)
	{
		if (stack.getItem() instanceof BlockItem)
		{
			Block block = ((BlockItem) stack.getItem()).getBlock();
			if (block instanceof IPlacementBlock)
			{
				IPlacementBlock<?> verticalBlock = ((IPlacementBlock<?>) block);
				if (verticalBlock.hasAdditionalStates()) verticalBlock.appendHoverTextImpl(stack, null, lines, context);
			}
		}
	}

	public static boolean onHighlightBlock(WorldRenderContext context, @Nullable HitResult hitResult)
	{
		if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK)
		{
			@SuppressWarnings("resource")
			Player player = Minecraft.getInstance().player;
			ItemStack stack = player.getMainHandItem();
			if (stack.isEmpty()) stack = player.getOffhandItem();
			if (stack.getItem() instanceof BlockItem)
			{
				Block block = ((BlockItem) stack.getItem()).getBlock();
				if (block instanceof IPlacementBlock)
				{
					IPlacementBlock<?> verticalBlock = ((IPlacementBlock<?>) block);
					if (verticalBlock.hasAdditionalStates()) verticalBlock.renderHighlight(context.matrixStack(), context.consumers().getBuffer(RenderType.LINES), player, (BlockHitResult) hitResult, context.camera(), context.tickDelta());
				}
			}
		}
		return true;
	}
}