package io.github.guiritter.war_thunder.ground_attack_test_range_generator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Cell {

	public final String lowerField;

	public final String upperField;

	@JsonCreator
	public Cell(@JsonProperty("upperField") String upperField, @JsonProperty("lowerField") String lowerField) {
		this.upperField = upperField;
		this.lowerField = lowerField;
	}
}
