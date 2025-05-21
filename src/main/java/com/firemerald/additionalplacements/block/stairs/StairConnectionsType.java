package com.firemerald.additionalplacements.block.stairs;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.firemerald.additionalplacements.block.stairs.common.CommonStairShapeState;

import net.minecraft.world.level.block.state.properties.Property;

public class StairConnectionsType extends Property<CommonStairShapeState> {
	public static final StairConnectionsType
	SIMPLE = new StairConnectionsType("no_vertical_connections", false, false, true),
	EXTENDED = new StairConnectionsType("no_mixed_connections", true, false, true),
	COMPLEX = new StairConnectionsType("all_connections", true, true, false);

	public final String tooltip;
	public final boolean allowVertical, allowMixed, allowFlipped;
	public final CommonStairShapeState defaultShapeState;
	private final List<CommonStairShapeState> values;
	private final Map<String, CommonStairShapeState> valueMap;

	public StairConnectionsType(String tooltip, boolean allowVertical, boolean allowMixed, boolean allowFlipped) {
		this("tooltip.additionalplacements.stair_connections." + tooltip, allowVertical, allowMixed, allowFlipped, Stream.of(CommonStairShapeState.values())
				.filter(state -> state.vanilla() == null &&
				(!state.shape.isVertical || allowVertical) &&
				(!state.shape.isMixed || allowMixed) &&
				(!state.isComplexFlipped || allowFlipped))
		.toList(), null);
	}

	public StairConnectionsType(String tooltip, boolean allowVertical, boolean allowMixed, boolean allowFlipped, CommonStairShapeState... allowedShapeStates) {
		this(tooltip, allowVertical, allowMixed, allowFlipped, Arrays.asList(allowedShapeStates));
	}

	public StairConnectionsType(String tooltip, boolean allowVertical, boolean allowMixed, boolean allowFlipped, Collection<CommonStairShapeState> allowedShapeStates) {
		this(tooltip, allowVertical, allowMixed, allowFlipped, new ArrayList<>(allowedShapeStates));
	}

	public StairConnectionsType(String tooltip, boolean allowVertical, boolean allowMixed, boolean allowFlipped, List<CommonStairShapeState> allowedShapeStates) {
		this(tooltip, allowVertical, allowMixed, allowFlipped, Collections.unmodifiableList(allowedShapeStates), null);
	}

	private StairConnectionsType(String tooltip, boolean allowVertical, boolean allowMixed, boolean allowFlipped, List<CommonStairShapeState> values, Void nil) {
		super("front_top_shape", CommonStairShapeState.class);
		this.tooltip = tooltip;
		this.allowVertical = allowVertical;
		this.allowMixed = allowMixed;
		this.allowFlipped = allowFlipped;
		this.defaultShapeState = values.getFirst();
		this.values = values;
		this.valueMap = values.stream().collect(Collectors.toMap(CommonStairShapeState::getSerializedName, state -> state));
	}

	@Override
	public Collection<CommonStairShapeState> getPossibleValues() {
		return values;
	}

	@Override
	public String getName(CommonStairShapeState value) {
		return value.getSerializedName();
	}

	@Override
	public Optional<CommonStairShapeState> getValue(String name) {
		return Optional.ofNullable(valueMap.get(name));
	}

	@Override
	public boolean equals(Object pOther) {
		if (this == pOther) return true;
		else if (pOther instanceof StairConnectionsType connectionsType) {
            return
					this.values.equals(connectionsType.values) &&
					this.valueMap.equals(connectionsType.valueMap) &&
					this.tooltip.equals(connectionsType.tooltip) &&
					this.allowVertical == connectionsType.allowVertical &&
					this.allowMixed == connectionsType.allowMixed &&
					this.allowFlipped == connectionsType.allowFlipped;
		}
		else return false;
	}

	@Override
	public int generateHashCode() {
		return 31 * (31 * super.generateHashCode() + this.values.hashCode()) + this.valueMap.hashCode();
	}

	public boolean isValid(CommonStairShapeState value) {
		return isValid(value.getSerializedName());
	}

	public boolean isValid(String name) {
		return valueMap.containsKey(name);
	}
}
