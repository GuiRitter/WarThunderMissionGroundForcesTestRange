package io.github.guiritter.war_thunder_wiki_crawler;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import static java.lang.System.out;

@Component
@Scope(SCOPE_PROTOTYPE)
public final class ModelTask extends ColumnTask {

	@Autowired
	private Map<String, String> fixMap;

	private WebElement modelElement;

	private Integer modelIndex;

	@Autowired
	private Map<String, Table> tableMap;

	@Override
	public void run() {
		var background = modelElement.findElement(By.cssSelector(".tree-item-background"));
		var text = modelElement.findElement(By.cssSelector(".tree-item-text"));
		var img = modelElement.findElement(By.cssSelector(".tree-item-img"));

		var a = background.findElement(By.cssSelector("a"));
		var title = a.getAttribute("title");
		var href = a.getAttribute("href");

		var span = text.findElement(By.cssSelector("span")).getAttribute("textContent");
		var blkImg = img.findElement(By.cssSelector("img")).getAttribute("alt").replace(".png", "");

		var tree = blkImg.substring(0, blkImg.indexOf("_"));

		var blkBackgound = fixMap.get(href);

		out.format("%s %s %s %s %s, %s, %s, %s\n", tree, rowIndex, columnIndex, modelIndex, span, title, blkBackgound, blkImg);

		var table = tableMap.get(tree);

		if (table == null) {
			table = new Table();
			tableMap.put(tree, table);
		}

		if (table.columnList.size() < (columnIndex + 1)) {
			table.columnList.add(new Column());
		}

		var column = table.columnList.get(columnIndex);

		column.cellList.add(new Cell(title, span, blkBackgound, blkImg, href));

		Thread.currentThread().interrupt();
	}

	public final void setModelElement(WebElement element) {
		if (modelElement == null) {
			modelElement = element;
		}
	}

	public final void setModelIndex(int index) {
		if (modelIndex == null) {
			modelIndex = index;
		}
	}
}
