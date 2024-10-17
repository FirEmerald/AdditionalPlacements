package com.firemerald.additionalplacements.client.gui;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.narration.NarrationSupplier;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class BetterObjectSelectionList<E extends BetterObjectSelectionList.Entry<E>> extends AbstractBetterSelectionList<E> {
	private static final Component USAGE_NARRATION = Component.translatable("narration.selection.usage");

	public BetterObjectSelectionList(Minecraft minecraft, int x, int y, int width, int height, int normalItemHeight) {
		super(minecraft, x, y, width, height, normalItemHeight);
	}

	/**
	 * Retrieves the next focus path based on the given focus navigation event.
	 * <p>
	 * 
	 * @return the next focus path as a ComponentPath, or {@code null} if there is
	 *         no next focus path.
	 * @param pEvent the focus navigation event.
	 */
	@Override
	@Nullable
	public ComponentPath nextFocusPath(FocusNavigationEvent event) {
		if (this.getItemCount() == 0) {
			return null;
		} else if (this.isFocused() && event instanceof FocusNavigationEvent.ArrowNavigation) {
			FocusNavigationEvent.ArrowNavigation focusnavigationevent$arrownavigation = (FocusNavigationEvent.ArrowNavigation) event;
			E e1 = this.nextEntry(focusnavigationevent$arrownavigation.direction());
			return e1 != null ? ComponentPath.path(this, ComponentPath.leaf(e1)) : null;
		} else if (!this.isFocused()) {
			E e = this.getSelected();
			if (e == null) {
				e = this.nextEntry(event.getVerticalDirectionForInitialFocus());
			}

			return e == null ? null : ComponentPath.path(this, ComponentPath.leaf(e));
		} else {
			return null;
		}
	}

	@Override
	public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
		E e = this.getHovered();
		if (e != null) {
			this.narrateListElementPosition(narrationElementOutput.nest(), e);
			e.updateNarration(narrationElementOutput);
		} else {
			E e1 = this.getSelected();
			if (e1 != null) {
				this.narrateListElementPosition(narrationElementOutput.nest(), e1);
				e1.updateNarration(narrationElementOutput);
			}
		}

		if (this.isFocused()) {
			narrationElementOutput.add(NarratedElementType.USAGE, USAGE_NARRATION);
		}

	}

	@OnlyIn(Dist.CLIENT)
	public abstract static class Entry<E extends BetterObjectSelectionList.Entry<E>> extends AbstractBetterSelectionList.Entry<E> implements NarrationSupplier {
		public abstract Component getNarration();

		/**
		 * Updates the narration output with the current narration information.
		 * 
		 * @param pNarrationElementOutput the output to update with narration
		 *                                information.
		 */
		@Override
		public void updateNarration(NarrationElementOutput narrationElementOutput) {
			narrationElementOutput.add(NarratedElementType.TITLE, this.getNarration());
		}
	}
}
