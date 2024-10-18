package com.firemerald.additionalplacements.client.gui;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public abstract class AbstractBetterSelectionList<E extends AbstractBetterSelectionList.Entry<E>> extends AbstractContainerEventHandler implements Renderable, NarratableEntry {
	protected static final int SCROLLBAR_WIDTH = 6;
	protected final Minecraft minecraft;
	protected final int normalItemHeight;
	private final List<E> children = new TrackedList();
	protected boolean centerListVertically = true;
	private double scrollAmount;
	private boolean renderHeader;
	protected int headerHeight;
	private boolean scrolling;
	@Nullable
	private E selected;
	private boolean renderBackground = true;
	@Nullable
	private E hovered;

	protected int x;
	protected int y;
	protected int width;
	protected int height;

	public AbstractBetterSelectionList(Minecraft minecraft, int x, int y, int width, int height, int normalItemHeight) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.minecraft = minecraft;
		this.normalItemHeight = normalItemHeight;
	}
	
	public int getX() { 
		return x;
	}
	
	public int getY() { 
		return y;
	}
	
	public int getRight() {
		return x + width;
	}
	
	public int getBottom() {
		return y + height;
	}

	protected void setRenderHeader(boolean renderHeader, int headerHeight) {
		this.renderHeader = renderHeader;
		this.headerHeight = headerHeight;
		if (!renderHeader) {
			this.headerHeight = 0;
		}

	}

	public int getRowWidth() {
		return width - SCROLLBAR_WIDTH * 2;
	}

	@Nullable
	public E getSelected() {
		return this.selected;
	}

	public void setSelected(@Nullable E pSelected) {
		this.selected = pSelected;
	}

	public E getFirstElement() {
		return this.children.get(0);
	}

	public void setRenderBackground(boolean renderBackground) {
		this.renderBackground = renderBackground;
	}

	/**
	 * Gets the focused GUI element.
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Nullable
	public E getFocused() {
		return (E) (super.getFocused());
	}

	/**
	 * {@return a List containing all GUI element children of this GUI element}
	 */
	@Override
	public final List<E> children() {
		return this.children;
	}

	protected void clearEntries() {
		this.children.clear();
		this.selected = null;
	}

	protected void replaceEntries(Collection<E> entries) {
		this.clearEntries();
		this.children.addAll(entries);
	}

	protected E getEntry(int index) {
		return this.children().get(index);
	}

	protected int addEntry(E entry) {
		this.children.add(entry);
		return this.children.size() - 1;
	}

	protected void addEntryToTop(E entry) {
		double d0 = this.getMaxScroll() - this.getScrollAmount();
		this.children.add(0, entry);
		this.setScrollAmount(this.getMaxScroll() - d0);
	}

	protected boolean removeEntryFromTop(E entry) {
		double scrollFromBottom = this.getMaxScroll() - this.getScrollAmount();
		boolean flag = this.removeEntry(entry);
		this.setScrollAmount(this.getMaxScroll() - scrollFromBottom);
		return flag;
	}

	protected int getItemCount() {
		return this.children().size();
	}

	protected boolean isSelectedItem(E entry) {
		return Objects.equals(this.getSelected(), entry);
	}

	@Nullable
	protected final E getEntryAtPosition(double mouseX, double mouseY) {
		if (mouseX >= this.getScrollbarPosition()) return null;
		int halfWidth = this.getRowWidth() / 2;
		int middleX = this.getX() + this.width / 2;
		int left = middleX - halfWidth;
		if (mouseX < left) return null;
		int right = middleX + halfWidth;
		if (mouseX > right) return null;
		int mousePosY = Mth.floor(mouseY - this.getY()) - this.headerHeight + (int) this.getScrollAmount() - 4;
		if (mousePosY < 0) return null;
		int itemY1 = 0;
		int itemY2;
		for (E entry : this.children()) {
			itemY2 = itemY1 + entry.getHeight();
			if (mousePosY >= itemY1 && mousePosY < itemY2) return entry;
			itemY1 = itemY2;
		}
		return null;
	}

	protected int getMaxPosition() {
		//TODO cache
		return children().stream().mapToInt(Entry::getHeight).sum() + this.headerHeight;
	}

	protected boolean clickedHeader(int clickedX, int clickedY) {
		return false;
	}

	protected void renderHeader(GuiGraphics guiGraphics, int x, int y) {
	}

	protected void renderDecorations(GuiGraphics guiGraphics, int mouseX, int mouseY) {
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		this.hovered = this.isMouseOver(mouseX, mouseY) ? this.getEntryAtPosition(mouseX, mouseY) : null;
		if (this.renderBackground) {
			guiGraphics.setColor(0.125F, 0.125F, 0.125F, 1.0F);
			int texSize = 32;
			guiGraphics.blit(Screen.BACKGROUND_LOCATION, this.getX(), this.getY(), this.getRight(), this.getBottom() + (int) this.getScrollAmount(), this.width, this.height, texSize, texSize);
			guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
		}

		this.enableScissor(guiGraphics);
		if (this.renderHeader) {
			int x = this.getRowLeft();
			int y = this.getY() + 4 - (int) this.getScrollAmount();
			this.renderHeader(guiGraphics, x, y);
		}

		this.renderList(guiGraphics, mouseX, mouseY, partialTick);
		guiGraphics.disableScissor();
		if (this.renderBackground) {
			int margin = 4;
			guiGraphics.fillGradient(RenderType.guiOverlay(), this.getX(), this.getY(), this.getRight(), this.getY() + margin, -16777216, 0, 0);
			guiGraphics.fillGradient(RenderType.guiOverlay(), this.getX(), this.getBottom() - margin, this.getRight(), this.getBottom(), 0, -16777216, 0);
		}

		int maxScroll = this.getMaxScroll();
		if (maxScroll > 0) {
			int scrollPos = this.getScrollbarPosition();
			int scrollSize = (int) ((float) (this.height * this.height) / (float) this.getMaxPosition());
			scrollSize = Mth.clamp(scrollSize, 32, this.height - 8);
			int scrollY = (int) this.getScrollAmount() * (this.height - scrollSize) / maxScroll + this.getY();
			if (scrollY < this.getY()) {
				scrollY = this.getY();
			}

			guiGraphics.fill(scrollPos, this.getY(), scrollPos + 6, this.getBottom(), -16777216);
			guiGraphics.fill(scrollPos, scrollY, scrollPos + 6, scrollY + scrollSize, -8355712);
			guiGraphics.fill(scrollPos, scrollY, scrollPos + 6 - 1, scrollY + scrollSize - 1, -4144960);
		}

		this.renderDecorations(guiGraphics, mouseX, mouseY);
		RenderSystem.disableBlend();
	}

	protected void enableScissor(GuiGraphics guiGraphics) {
		guiGraphics.enableScissor(this.getX(), this.getY(), this.getRight(), this.getBottom());
	}

	protected void centerScrollOn(E entry) {
		int y = 0;
		for (E item : children()) {
			if (item == entry) {
				y += item.getHeight() / 2;
				break;
			} else y += item.getHeight();
		}
		this.setScrollAmount(y - this.height / 2);
	}

	protected void ensureVisible(E entry) {
		int rowTop = this.getRowTop(entry);
		int obscuredTop = rowTop - this.getY() - 2;
		if (obscuredTop < 0) {
			this.scroll(obscuredTop);
		}

		int obscuredBottom = rowTop + entry.getHeight() - this.getBottom() + 2;
		if (obscuredBottom > 0) {
			this.scroll(obscuredBottom);
		}

	}

	private void scroll(int scroll) {
		this.setScrollAmount(this.getScrollAmount() + scroll);
	}

	public double getScrollAmount() {
		return this.scrollAmount;
	}

	public void setScrollAmount(double scroll) {
		this.scrollAmount = Mth.clamp(scroll, 0.0D, this.getMaxScroll());
	}

	public int getMaxScroll() {
		return Math.max(0, this.getMaxPosition() - (this.height - 4));
	}

	protected void updateScrollingState(double mouseX, double mouseY, int button) {
		this.scrolling = button == 0 && mouseX >= this.getScrollbarPosition() && mouseX < this.getScrollbarPosition() + 6;
	}

	protected int getScrollbarPosition() {
		return this.getX() + this.width - SCROLLBAR_WIDTH;
	}

	protected boolean isValidMouseClick(int pButton) {
		return pButton == 0;
	}

	/**
	 * Called when a mouse button is clicked within the GUI element.
	 * <p>
	 * 
	 * @return {@code true} if the event is consumed, {@code false} otherwise.
	 * @param mouseX the X coordinate of the mouse.
	 * @param mouseY the Y coordinate of the mouse.
	 * @param button the button that was clicked.
	 */
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (!this.isValidMouseClick(button)) {
			return false;
		} else {
			this.updateScrollingState(mouseX, mouseY, button);
			if (!this.isMouseOver(mouseX, mouseY)) {
				return false;
			} else {
				E clicked = this.getEntryAtPosition(mouseX, mouseY);
				if (clicked != null) {
					if (clicked.mouseClicked(mouseX, mouseY, button)) {
						E focused = this.getFocused();
						if (focused != clicked && focused instanceof ContainerEventHandler) {
							ContainerEventHandler containereventhandler = (ContainerEventHandler) focused;
							containereventhandler.setFocused((GuiEventListener) null);
						}

						this.setFocused(clicked);
						this.setDragging(true);
						return true;
					}
				} else if (this.clickedHeader((int) (mouseX - (this.getX() + this.width / 2 - this.getRowWidth() / 2)), (int) (mouseY - this.getY()) + (int) this.getScrollAmount() - 4)) {
					return true;
				}

				return this.scrolling;
			}
		}
	}

	/**
	 * Called when a mouse button is released within the GUI element.
	 * <p>
	 * 
	 * @return {@code true} if the event is consumed, {@code false} otherwise.
	 * @param mouseX the X coordinate of the mouse.
	 * @param mouseY the Y coordinate of the mouse.
	 * @param button the button that was released.
	 */
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (this.getFocused() != null) {
			this.getFocused().mouseReleased(mouseX, mouseY, button);
		}

		return false;
	}

	/**
	 * Called when the mouse is dragged within the GUI element.
	 * <p>
	 * 
	 * @return {@code true} if the event is consumed, {@code false} otherwise.
	 * @param mouseX the X coordinate of the mouse.
	 * @param mouseY the Y coordinate of the mouse.
	 * @param button the button that is being dragged.
	 * @param dragX  the X distance of the drag.
	 * @param dragY  the Y distance of the drag.
	 */
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		if (super.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
			return true;
		} else if (button == 0 && this.scrolling) {
			if (mouseY < this.getY()) {
				this.setScrollAmount(0.0D);
			} else if (mouseY > this.getBottom()) {
				this.setScrollAmount(this.getMaxScroll());
			} else {
				double maxScroll = Math.max(1, this.getMaxScroll());
				int height = this.height;
				int j = Mth.clamp((int) ((float) (height * height) / (float) this.getMaxPosition()), 32, height - 8);
				double scrollScale = Math.max(1.0D, maxScroll / (height - j));
				this.setScrollAmount(this.getScrollAmount() + dragY * scrollScale);
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollY) {
		this.setScrollAmount(this.getScrollAmount() - scrollY * this.normalItemHeight / 2.0D);
		return true;
	}

	/**
	 * Sets the focus state of the GUI element.
	 * 
	 * @param pFocused the focused GUI element.
	 */
	@Override
	public void setFocused(@Nullable GuiEventListener listener) {
		super.setFocused(listener);
		int i = this.children.indexOf(listener);
		if (i >= 0) {
			E e = this.children.get(i);
			this.setSelected(e);
			if (this.minecraft.getLastInputType().isKeyboard()) {
				this.ensureVisible(e);
			}
		}

	}

	@Nullable
	protected E nextEntry(ScreenDirection direction) {
		return this.nextEntry(direction, (unused) -> {
			return true;
		});
	}

	@Nullable
	protected E nextEntry(ScreenDirection direction, Predicate<E> predicate) {
		return this.nextEntry(direction, predicate, this.getSelected());
	}

	@Nullable
	protected E nextEntry(ScreenDirection direction, Predicate<E> predicate, @Nullable E selected) {
		byte temp;
		switch (direction) {
		case RIGHT:
		case LEFT:
			temp = 0;
			break;
		case UP:
			temp = -1;
			break;
		case DOWN:
			temp = 1;
			break;
		default:
			throw new IncompatibleClassChangeError();
		}

		int entryDirection = temp;
		if (!this.children().isEmpty() && entryDirection != 0) {
			int start;
			if (selected == null) {
				start = entryDirection > 0 ? 0 : this.children().size() - 1;
			} else {
				start = this.children().indexOf(selected) + entryDirection;
			}

			for (int index = start; index >= 0 && index < this.children.size(); index += entryDirection) {
				E entry = this.children().get(index);
				if (predicate.test(entry)) {
					return entry;
				}
			}
		}

		return null;
	}

	/**
	 * Checks if the given mouse coordinates are over the GUI element.
	 * <p>
	 * 
	 * @return {@code true} if the mouse is over the GUI element, {@code false}
	 *         otherwise.
	 * @param mouseX the X coordinate of the mouse.
	 * @param mouseY the Y coordinate of the mouse.
	 */
	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return mouseY >= this.getY() && mouseY <= this.getBottom() && mouseX >= this.getX() && mouseX <= this.getRight();
	}

	protected void renderList(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		int left = this.getRowLeft();
		int width = this.getRowWidth();
		int top = this.getY() + this.headerHeight - (int) this.scrollAmount;
		int bottom;
		for (E entry : children()) {
			bottom = top + entry.getHeight();
			if (bottom >= this.getY() && top <= this.getBottom()) {
				this.renderItem(guiGraphics, mouseX, mouseY, partialTick, entry, left, top, width, entry.getHeight());
				if (bottom > this.getBottom()) break; //no more should render
			}
			top = bottom;
		}

	}

	protected void renderItem(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, E entry, int left, int top, int width, int height) {
		entry.renderBack(guiGraphics, top, left, width, height, mouseX, mouseY, Objects.equals(this.hovered, entry), partialTick);
		if (this.isSelectedItem(entry)) {
			int outerColor = this.isFocused() ? -1 : 0xFF808080;
			this.renderSelection(guiGraphics, top, width, height, outerColor, 0xFF000000);
		}

		entry.render(guiGraphics, top, left, width, height, mouseX, mouseY, Objects.equals(this.hovered, entry), partialTick);
	}

	protected void renderSelection(GuiGraphics guiGraphics, int top, int width, int height, int outerColor, int innerColor) {
		int minX = this.getX() + (this.width - width) / 2;
		int maxX = this.getX() + (this.width + width) / 2;
		guiGraphics.fill(minX, top - 2, maxX, top + height + 2, outerColor);
		guiGraphics.fill(minX + 1, top - 1, maxX - 1, top + height + 1, innerColor);
	}

	public int getRowLeft() {
		return this.getX() + this.width / 2 - this.getRowWidth() / 2 + 2;
	}

	public int getRowRight() {
		return this.getRowLeft() + this.getRowWidth();
	}

	protected int getRowTop(E entry) {
		int y = 0;
		for (E item : children()) {
			if (item == entry) break;
			else y += item.getHeight();
		}
		return this.getY() - (int) this.getScrollAmount() + y + this.headerHeight;
	}

	protected int getRowBottom(E entry) {
		return this.getRowTop(entry) + entry.getHeight();
	}

	/**
	 * {@return the narration priority}
	 */
	@Override
	public NarratableEntry.NarrationPriority narrationPriority() {
		if (this.isFocused()) {
			return NarratableEntry.NarrationPriority.FOCUSED;
		} else {
			return this.hovered != null ? NarratableEntry.NarrationPriority.HOVERED : NarratableEntry.NarrationPriority.NONE;
		}
	}

	@Nullable
	protected E remove(int index) {
		E e = this.children.get(index);
		return this.removeEntry(this.children.get(index)) ? e : null;
	}

	protected boolean removeEntry(E entry) {
		boolean flag = this.children.remove(entry);
		if (flag && entry == this.getSelected()) {
			this.setSelected((E) null);
		}

		return flag;
	}

	@Nullable
	protected E getHovered() {
		return this.hovered;
	}

	void bindEntryToSelf(AbstractBetterSelectionList.Entry<E> entry) {
		entry.list = this;
	}

	protected void narrateListElementPosition(NarrationElementOutput narrationElementOutput, E entry) {
		List<E> list = this.children();
		if (list.size() > 1) {
			int index = list.indexOf(entry);
			if (index != -1) {
				narrationElementOutput.add(NarratedElementType.POSITION, Component.translatable("narrator.position.list", index + 1, list.size()));
			}
		}

	}

	protected abstract static class Entry<E extends AbstractBetterSelectionList.Entry<E>> implements GuiEventListener {
		/** @deprecated */
		@Deprecated
		protected AbstractBetterSelectionList<E> list;

		/**
		 * Sets the focus state of the GUI element.
		 * 
		 * @param focused {@code true} to apply focus, {@code false} to remove focus
		 */
		@Override
		public void setFocused(boolean focused) {
		}

		/**
		 * {@return {@code true} if the GUI element is focused, {@code false} otherwise}
		 */
		@Override
		public boolean isFocused() {
			return this.list.getFocused() == this;
		}

		public abstract int getHeight();

		public abstract void render(GuiGraphics guiGraphics, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick);

		public void renderBack(GuiGraphics guiGraphics, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
		}

		/**
		 * Checks if the given mouse coordinates are over the GUI element.
		 * <p>
		 * 
		 * @return {@code true} if the mouse is over the GUI element, {@code false}
		 *         otherwise.
		 * @param mouseX the X coordinate of the mouse.
		 * @param mouseY the Y coordinate of the mouse.
		 */
		@Override
		public boolean isMouseOver(double mouseX, double mouseY) {
			return Objects.equals(this.list.getEntryAtPosition(mouseX, mouseY), this);
		}
	}

	class TrackedList extends AbstractList<E> {
		private final List<E> delegate = Lists.newArrayList();

		@Override
		public E get(int index) {
			return this.delegate.get(index);
		}

		@Override
		public int size() {
			return this.delegate.size();
		}

		@Override
		public E set(int index, E entry) {
			E e = this.delegate.set(index, entry);
			AbstractBetterSelectionList.this.bindEntryToSelf(entry);
			return e;
		}

		@Override
		public void add(int index, E entry) {
			this.delegate.add(index, entry);
			AbstractBetterSelectionList.this.bindEntryToSelf(entry);
		}

		@Override
		public E remove(int index) {
			return this.delegate.remove(index);
		}
	}
}
