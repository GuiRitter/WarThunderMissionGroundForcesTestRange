package io.github.guiritter.war_thunder_wiki_crawler;

import static io.github.guiritter.war_thunder_wiki_crawler.Config.buildWebDriver;
import static java.lang.System.out;
import static java.lang.Thread.currentThread;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import java.util.Map;

import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(SCOPE_PROTOTYPE)
public final class ModelDetailTask extends ModelTask {

	private String blkBackground;

	private String href;

	private Integer modelIndex;

	private String span;

	@Autowired
	private Map<String, Table> tableMap;

	private String title;

	@Override
	public void run() {

		var webDriver = buildWebDriver();
		webDriver.get(href);

		var blkImg = webDriver.findElement(By.cssSelector(".specs_card_main")).getAttribute("data-code");

		var tree = blkImg.substring(0, blkImg.indexOf("_"));

		out.format("%s %s %s %s %s, %s, %s, %s\n", tree, rowIndex, columnIndex, modelIndex, span, title,
				blkBackground, blkImg);

		var table = tableMap.get(tree);

		if (table == null) {
			table = new Table();
			tableMap.put(tree, table);
		}

		if (table.columnList.size() < (columnIndex + 1)) {
			table.columnList.add(new Column());
		}

		var column = table.columnList.get(columnIndex);

		column.cellList.add(new Cell(title, span, blkBackground, blkImg, href));

		currentThread().interrupt();
		webDriver.quit();
	}

	public final void setHref(String href) {
		if (this.href == null) {
			this.href = href;
		}
	}

	public final void setSpan(String span) {
		if (this.span == null) {
			this.span = span;
		}
	}

	public final void setTitle(String title) {
		if (this.title == null) {
			this.title = title;
		}
	}

	public final void setBlkBackground(String blkBackground) {
		if (this.blkBackground == null) {
			this.blkBackground = blkBackground;
		}
	}
}
