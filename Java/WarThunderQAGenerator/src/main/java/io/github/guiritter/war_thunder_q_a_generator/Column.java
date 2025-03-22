package io.github.guiritter.war_thunder_q_a_generator;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class Column {

	@JsonProperty
	final LinkedList<Cell> cellList = new LinkedList<>();

	public Column() {
	}

	public Column(Cell... cellList) {
		for (var cell : cellList) {
			this.cellList.add(cell);
		}
	}

	public Column(List<Cell> cellList) {
		this.cellList.addAll(cellList);
	}
}
