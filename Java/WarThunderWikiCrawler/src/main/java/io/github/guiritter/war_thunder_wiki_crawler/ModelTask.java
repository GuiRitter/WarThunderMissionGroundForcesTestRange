package io.github.guiritter.war_thunder_wiki_crawler;

import static java.lang.System.out;
import static java.lang.Thread.currentThread;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(SCOPE_PROTOTYPE)
public class ModelTask extends ColumnTask {

	@Autowired
	private Map<String, String> fixMap;

	WebElement modelElement;

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
		var blkImgList = img.findElements(By.cssSelector("img"));

		var blkBackground = fixMap.get(href);

		var isImgListEmpty = blkImgList.isEmpty();

		Boolean isImageReused = null;
		String blkImg = "";
		String treeAlt = "";

		if (!isImgListEmpty) {
			blkImg = blkImgList.get(0).getAttribute("alt").replace(".png", "");

			treeAlt = blkImg.substring(0, blkImg.indexOf("_"));

			isImageReused = tree.compareToIgnoreCase(treeAlt) != 0;
		}

		if (isImgListEmpty || ((isImageReused != null) && (isImageReused == true))) {
			var task = applicationContext.getBean("modelDetailTask", ModelDetailTask.class);

			task.setRowIndex(rowIndex);
			task.setColumnIndex(columnIndex);
			task.setModelIndex(modelIndex);
			task.setModelElement(modelElement);
			task.setHref(href);
			task.setSpan(span);
			task.setTitle(title);
			task.setBlkBackground(blkBackground);

			out.println("ModelTask started before taskExecutor.execute");
			try {
				taskExecutor.submit(task).get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			out.println("ModelTask started after taskExecutor.execute");
		} else {
			out.format("ModelTask %s %s %s %s %s %s, %s, %s, %s\n", tree, treeAlt, rowIndex, columnIndex, modelIndex, span, title,
					blkBackground, blkImg);

			var table = tableMap.get(treeAlt);

			if (table == null) {
				table = new Table();
				tableMap.put(treeAlt, table);
			}

			if (table.columnList.size() < (columnIndex + 1)) {
				table.columnList.add(new Column());
			}

			var column = table.columnList.get(columnIndex);

			column.cellList.add(new Cell(title, span, blkBackground, blkImg, href));
		}

		currentThread().interrupt();
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
