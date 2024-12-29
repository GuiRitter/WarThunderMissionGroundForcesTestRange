package io.github.guiritter.war_thunder_wiki_crawler;

import static java.lang.System.out;
import static java.lang.Thread.currentThread;
import static java.util.stream.IntStream.range;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import java.util.concurrent.ExecutionException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * Represents a cell in the table that might contain either a single vehicle (wt-tree_item) or a group of vehicles (wt-tree_group containing wt-tree_item).
  */
@Component
@Scope(SCOPE_PROTOTYPE)
public class TreeGroupTask implements Runnable {

	@Autowired
	ApplicationContext applicationContext;

	Integer rank;

	Integer rowIndex;

	@Autowired
	ThreadPoolTaskExecutor taskExecutor;

	String tree;

	private WebElement treeGroupElement;

	Integer treeGroupIndex;

	String treeRankInstance;

	@Override
	public void run() {
		out.format("TreeGroupTask %s %s %s %s %s\n", tree, rank, treeRankInstance, rowIndex, treeGroupIndex);

		var treeItemList = treeGroupElement.findElements(By.cssSelector(".wt-tree_item"));

		range(0, treeItemList.size()).forEach(treeItemIndex -> {

			var task = applicationContext.getBean("treeItemTask", TreeItemTask.class);

			task.setTree(tree);
			task.setRank(rank);
			task.setTreeRankInstance(treeRankInstance);
			task.setRowIndex(rowIndex);
			task.setTreeGroupIndex(treeGroupIndex);
			task.setTreeItemIndex(treeItemIndex);
			task.setTreeItemElement(treeItemList.get(treeItemIndex));

			out.format("TreeGroupTask %s %s %s %s %s %s started before taskExecutor.execute\n", tree, rank, treeRankInstance, rowIndex, treeGroupIndex, treeItemIndex);
			try {
				taskExecutor.submit(task).get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			out.format("TreeGroupTask %s %s %s %s %s %s started after taskExecutor.execute\n", tree, rank, treeRankInstance, rowIndex, treeGroupIndex, treeItemIndex);
		});

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

	public final void setTreeGroupElement(WebElement treeGroupElement) {
		if (this.treeGroupElement == null) {
			this.treeGroupElement = treeGroupElement;
		}
	}

	public final void setTreeGroupIndex(Integer treeGroupIndex) {
		if (this.treeGroupIndex == null) {
			this.treeGroupIndex = treeGroupIndex;
		}
	}

	public final void setTreeRankInstance(String treeRankInstance) {
		if (this.treeRankInstance == null) {
			this.treeRankInstance = treeRankInstance;
		}
	}
}
