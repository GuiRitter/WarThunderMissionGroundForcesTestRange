package io.github.guiritter.war_thunder.ground_attack_test_range_generator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a vehicle as it's laid out in a table arrangement.
 */
public class Cell {

	/**
	 * *.blk name.
	 */
	public final String lowerField;

	/**
	 * In game name.
	 */
	public final String upperField;

	/**
	 * Builds a new Cell.
	 * @param upperField in game name.
	 * @param lowerField *.blk name.
	 */
	@JsonCreator
	public Cell(@JsonProperty("upperField") String upperField, @JsonProperty("lowerField") String lowerField) {
		this.upperField = upperField;
		this.lowerField = lowerField;
	}
}
