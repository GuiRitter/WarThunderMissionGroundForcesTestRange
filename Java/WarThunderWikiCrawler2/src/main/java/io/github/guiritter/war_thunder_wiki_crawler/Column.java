package io.github.guiritter.war_thunder_wiki_crawler;

import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class Column {

	@JsonProperty
	final LinkedList<Cell> cellList = new LinkedList<>();
}
