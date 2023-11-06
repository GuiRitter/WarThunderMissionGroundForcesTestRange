package io.github.guiritter.war_thunder_wiki_tree_bridger;

import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class WikiTable {

	@JsonProperty
	public final LinkedList<WikiColumn> columnList = new LinkedList<>();

	public WikiTable() {}
}
