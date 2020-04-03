package io.github.guiritter.war_thunder.ground_attack_test_range_generator;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Column {

	public final List<Cell> cellList = new LinkedList<>();

	@JsonCreator
	public Column(@JsonProperty("cellList") List<Cell> cellList) {
		this.cellList.addAll(cellList);
	}
}
