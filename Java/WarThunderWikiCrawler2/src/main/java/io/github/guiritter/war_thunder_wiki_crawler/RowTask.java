package io.github.guiritter.war_thunder_wiki_crawler;

import static java.lang.System.out;
import static java.lang.Thread.currentThread;
import static java.util.stream.IntStream.range;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * Inside either the Researchable or the Premium subdivision, represents a row in the table, where each column might be either a single vehicle or a group of vehicles.
 */
@Component
@Scope(SCOPE_PROTOTYPE)
public class RowTask implements Callable<Integer> {

	@Autowired
	ApplicationContext applicationContext;

	Integer columnSizePrevious;

	Integer rank;

	private WebElement rowElement;

	Integer rowIndex;

	@Autowired
	ThreadPoolTaskExecutor taskExecutor;

	String treeRankInstance;

	String tree;

	@Override
	public Integer call() throws Exception {
		out.format("RowTask %s %s %s %s\n", tree, rank, treeRankInstance, rowIndex);

		var treeGroupList = rowElement.findElements(By.cssSelector("td"));

		range(0, treeGroupList.size()).forEach(treeGroupIndex -> {

			var task = applicationContext.getBean("treeGroupTask", TreeGroupTask.class);
			
			task.setTreeGroupElement(treeGroupList.get(treeGroupIndex));

			treeGroupIndex = ((columnSizePrevious == null) ? 0 : columnSizePrevious) + treeGroupIndex;

			task.setTree(tree);
			task.setRank(rank);
			task.setTreeRankInstance(treeRankInstance);
			task.setRowIndex(rowIndex);
			task.setTreeGroupIndex(treeGroupIndex);

			out.format("RowTask %s %s %s %s %s started before taskExecutor.execute\n", tree, rank, treeRankInstance, rowIndex, treeGroupIndex);
			try {
				taskExecutor.submit(task).get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			out.format("RowTask %s %s %s %s %s started after taskExecutor.execute\n", tree, rank, treeRankInstance, rowIndex, treeGroupIndex);
		});

		currentThread().interrupt();

		return treeGroupList.size();
	}

	public final void setColumnSizePrevious(Integer columnSizePrevious) {
		if (this.columnSizePrevious == null) {
			this.columnSizePrevious = columnSizePrevious;
		}
	}

	public final void setRank(int rank) {
		if (this.rank == null) {
			this.rank = rank;
		}
	}

	public final void setRowElement(WebElement rowElement) {
		if (this.rowElement == null) {
			this.rowElement = rowElement;
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

	public final void setTreeRankInstance(String treeRankInstance) {
		if (this.treeRankInstance == null) {
			this.treeRankInstance = treeRankInstance;
		}
	}
}
