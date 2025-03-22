package io.github.guiritter.war_thunder_q_a_generator;

import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class Table {

	@JsonProperty
	public final LinkedList<Column> columnList = new LinkedList<>();

	public Table() {
	}

	public Table(Column... columnList) {
		for (var column : columnList) {
			this.columnList.add(column);
		}
	}
}
