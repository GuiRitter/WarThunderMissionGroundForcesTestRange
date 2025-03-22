package io.github.guiritter.war_thunder_q_a_generator;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class Cell {

	@JsonProperty("lowerField")
	public final String lowerField;

	@JsonProperty("upperField")
	public final String upperField;

	@Override
	public String toString() {
		return String.format("{ class: Cell, lowerField: %s, upperField: %s }", lowerField, upperField);
	}

	public Cell(String lowerField, String upperField) {
		this.lowerField = lowerField;
		this.upperField = upperField;
	}
}
