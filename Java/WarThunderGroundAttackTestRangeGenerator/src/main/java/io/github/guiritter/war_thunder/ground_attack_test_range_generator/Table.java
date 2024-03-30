package io.github.guiritter.war_thunder.ground_attack_test_range_generator;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the vehicle cells laid out in columns and rows.
 */
public class Table {

	/**
	 * List of vehicle cell columns.
	 */
	public final List<Column> columnList = new LinkedList<>();

	/**
	 * Builds an empty Table with the provided columns.
	 * @param columnList optional list containing columns that will be added after the columns already here.
	 */
	@JsonCreator
	public Table(@JsonProperty("columnList") List<Column> columnList) {
		this.columnList.addAll(columnList);
	}
}
