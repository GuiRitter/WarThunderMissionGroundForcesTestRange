package io.github.guiritter.war_thunder_wiki_crawler;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class Cell {

	@JsonProperty("blk")
	public final String blk;

	@JsonProperty("title")
	public final String title;

	@Override
	public String toString() {
		return String.format("{ class: Cell, title: %s, blk: %s }", title, blk);
	}

	public Cell(String title, String blk) {
		this.title = title;
		this.blk = blk;
	}
}
