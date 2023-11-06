package io.github.guiritter.war_thunder_wiki_crawler;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class Cell {

	@JsonProperty("blkBackground")
	public final String blkBackground;

	@JsonProperty("blkImg")
	public final String blkImg;

	@JsonProperty("span")
	public final String span;

	@JsonProperty("href")
	public final String href;

	@JsonProperty("title")
	public final String title;

	@Override
	public String toString() {
		return String.format("{ class: Cell, title: %s, span: %s, blkBackground: %s, blkImg: %s, href: %s }", title, span, blkBackground, blkImg, href);
	}

	public Cell(String title, String span, String blkBackground, String blkImg, String href) {
		this.title = title;
		this.span = span;
		this.blkBackground = blkBackground;
		this.blkImg = blkImg;
		this.href = href;
	}
}
