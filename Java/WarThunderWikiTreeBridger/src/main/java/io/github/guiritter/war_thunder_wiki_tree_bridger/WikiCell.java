package io.github.guiritter.war_thunder_wiki_tree_bridger;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class WikiCell {

	@JsonProperty("blkBackground")
	public String blkBackground;

	@JsonProperty("blkImg")
	public String blkImg;

	@JsonProperty("span")
	public String span;

	@JsonProperty("title")
	public String title;

	@Override
	public String toString() {
		return String.format("{ class: CellWiki, title: %s, span: %s, blkBackground: %s, blkImg: %s }", title, span, blkBackground, blkImg);
	}

	public WikiCell() {}

	public WikiCell(String title, String span, String blkBackground, String blkImg) {
		this.title = title;
		this.span = span;
		this.blkBackground = blkBackground;
		this.blkImg = blkImg;
	}
}
