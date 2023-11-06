package io.github.guiritter.war_thunder_wiki_tree_bridger;

import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class WikiColumn {

	@JsonProperty
	final LinkedList<WikiCell> cellList = new LinkedList<>();

	public WikiColumn() {}
}
