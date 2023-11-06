package io.github.guiritter.war_thunder_wiki_tree_bridger;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class ManagerCell {

	@JsonProperty("lowerField")
	public String lowerField;

	@JsonProperty("upperField")
	public String upperField;

	public ManagerCell() {}
}
