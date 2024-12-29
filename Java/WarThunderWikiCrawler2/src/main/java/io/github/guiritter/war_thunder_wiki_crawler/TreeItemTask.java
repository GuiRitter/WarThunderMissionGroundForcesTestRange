package io.github.guiritter.war_thunder_wiki_crawler;

import static java.lang.System.out;
import static java.lang.Thread.currentThread;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * Represents a vehicle.
 */
@Component
@Scope(SCOPE_PROTOTYPE)
public class TreeItemTask implements Runnable {

	@Autowired
	ApplicationContext applicationContext;

	Integer rank;

	Integer rowIndex;

	@Autowired
	private Map<String, Table> tableMap;

	@Autowired
	ThreadPoolTaskExecutor taskExecutor;

	String tree;
	
	Integer treeGroupIndex;

	private WebElement treeItemElement;
	
	Integer treeItemIndex;

	String treeRankInstance;

	@Override
	public void run() {
		out.format("TreeItemTask %s %s %s %s %s %s\n", tree, rank, treeRankInstance, rowIndex, treeGroupIndex, treeItemIndex);

		var text = treeItemElement.findElement(By.cssSelector(".wt-tree_item-text"));

		var title = text.findElement(By.cssSelector("span")).getAttribute("textContent");

		var blk = treeItemElement.getAttribute("data-unit-id");

		var table = tableMap.get(tree);

		if (table == null) {
			table = new Table();
			tableMap.put(tree, table);
		}

		if (table.columnList.size() < (treeGroupIndex + 1)) {
			table.columnList.add(new Column());
		}

		var column = table.columnList.get(treeGroupIndex);

		column.cellList.add(new Cell(title, blk));

		out.format("TreeItemTask %s %s %s %s %s %s %s %s\n", tree, rank, treeRankInstance, rowIndex, treeGroupIndex, treeItemIndex, blk, title);

		currentThread().interrupt();
	}

	public final void setRank(int rank) {
		if (this.rank == null) {
			this.rank = rank;
		}
	}

	public final void setRowIndex(int rowIndex) {
		if (this.rowIndex == null) {
			this.rowIndex = rowIndex;
		}
	}

	public final void setTree(String tree) {
		if (this.tree == null) {
			this.tree = tree;
		}
	}

	public final void setTreeGroupIndex(Integer treeGroupIndex) {
		if (this.treeGroupIndex == null) {
			this.treeGroupIndex = treeGroupIndex;
		}
	}

	public final void setTreeItemElement(WebElement treeItemElement) {
		if (this.treeItemElement == null) {
			this.treeItemElement = treeItemElement;
		}
	}

	public final void setTreeItemIndex(Integer treeItemIndex) {
		if (this.treeItemIndex == null) {
			this.treeItemIndex = treeItemIndex;
		}
	}

	public final void setTreeRankInstance(String treeRankInstance) {
		if (this.treeRankInstance == null) {
			this.treeRankInstance = treeRankInstance;
		}
	}
}
