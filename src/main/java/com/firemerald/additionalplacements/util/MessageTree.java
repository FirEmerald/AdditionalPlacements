package com.firemerald.additionalplacements.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

public class MessageTree {
	public final Component message;
	public final List<MessageTree> children;
	
	public MessageTree(Component message) {
		this.message = message;
		this.children = new ArrayList<>();
	}
	
	public MessageTree(Component error, Collection<MessageTree> children) {
		this.message = error;
		this.children = new ArrayList<>(children);
	}
	
	public MessageTree(FriendlyByteBuf buf) {
		this.message = buf.readComponent();
		this.children = buf.readList(MessageTree::new);
	}
	
	public void write(FriendlyByteBuf buf) {
		buf.writeComponent(message);
		buf.writeCollection(children, MessageTree::write);
	}
	
	@FunctionalInterface
	public static interface Action {
		public void apply(Component message, int level);
	}
	
	public void forEach(Action action, int level) {
		action.apply(message, level);
		children.forEach(child -> child.forEach(action, level + 1));
	}
	
	public void output(Consumer<String> out) {
		out.accept(message.getString());
		children.forEach(v -> v.output(s -> out.accept("  " + s)));
	}
	
	public static void write(FriendlyByteBuf buf, MessageTree errorList) {
		errorList.write(buf);
	}
}
