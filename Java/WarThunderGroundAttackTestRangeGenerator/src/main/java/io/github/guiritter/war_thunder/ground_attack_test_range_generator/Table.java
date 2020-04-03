package io.github.guiritter.war_thunder.ground_attack_test_range_generator;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Table {

	public final List<Column> columnList = new LinkedList<>();

	@JsonCreator
	public Table(@JsonProperty("columnList") List<Column> columnList) {
		this.columnList.addAll(columnList);
	}
}
