package io.github.guiritter.war_thunder_wiki_crawler;

import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class Table {

	@JsonProperty
	public final LinkedList<Column> columnList = new LinkedList<>();

	public Table() {
	}
}
