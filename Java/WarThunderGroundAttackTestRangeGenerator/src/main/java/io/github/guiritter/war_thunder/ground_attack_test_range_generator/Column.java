package io.github.guiritter.war_thunder.ground_attack_test_range_generator;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a column of vehicles as they're laid out in a table arrangement.
 */
public class Column {

	/**
	 * List of vehicle cells.
	 */
	public final List<Cell> cellList = new LinkedList<>();

	/**
	 * Builds a Column containing the provided cells.
	 * @param cellList list with cells waiting to be laid out in a column.
	 */
	@JsonCreator
	public Column(@JsonProperty("cellList") List<Cell> cellList) {
		this.cellList.addAll(cellList);
	}
}
