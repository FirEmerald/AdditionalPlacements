package com.firemerald.additionalplacements.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;

public class MessageTree {
	public final ITextComponent message;
	public final List<MessageTree> children;

	public MessageTree(ITextComponent message) {
		this.message = message;
		this.children = new ArrayList<>();
	}

	public MessageTree(ITextComponent error, Collection<MessageTree> children) {
		this.message = error;
		this.children = new ArrayList<>(children);
	}

	public MessageTree(PacketBuffer buf) {
		this.message = buf.readComponent();
		this.children = BufferUtils.readList(buf, MessageTree::new);
	}

	public void write(PacketBuffer buf) {
		buf.writeComponent(message);
		BufferUtils.writeCollection(buf, children, MessageTree::write);
	}

	@FunctionalInterface
	public interface Action {
		void apply(ITextComponent message, int level);
	}

	public void forEach(Action action, int level) {
		action.apply(message, level);
		children.forEach(child -> child.forEach(action, level + 1));
	}

	public void output(Consumer<String> out) {
		out.accept(message.getString());
		children.forEach(v -> v.output(s -> out.accept("  " + s)));
	}

	public static void write(PacketBuffer buf, MessageTree errorList) {
		errorList.write(buf);
	}
}
