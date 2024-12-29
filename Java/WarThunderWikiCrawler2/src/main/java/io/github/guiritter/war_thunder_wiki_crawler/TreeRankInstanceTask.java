package io.github.guiritter.war_thunder_wiki_crawler;

import static java.lang.System.out;
import static java.lang.Thread.currentThread;
import static java.util.stream.IntStream.range;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * Represents either the Researchable or the Premium part of a rank.
 */
@Component
@Scope(SCOPE_PROTOTYPE)
public class TreeRankInstanceTask implements Callable<Integer> {

	@Autowired
	ApplicationContext applicationContext;

	Integer columnSizePrevious;

	Integer rank;

	@Autowired
	ThreadPoolTaskExecutor taskExecutor;

	String tree;

	String treeRankInstance;

	private WebElement treeRankInstanceElement;

	@Override
	public Integer call() throws Exception {
		out.format("TreeRankInstanceTask %s %s %s \n", tree, rank, treeRankInstance);

		var rowList = treeRankInstanceElement.findElements(By.cssSelector("tr"));

		AtomicReference<Integer> columnSizeReference = new AtomicReference<Integer>(null);

		range(0, rowList.size()).forEach(rowIndex -> {
			var task = applicationContext.getBean("rowTask", RowTask.class);

			task.setTree(tree);
			task.setRank(rank);
			task.setTreeRankInstance(treeRankInstance);
			task.setRowIndex(rowIndex);
			task.setColumnSizePrevious(columnSizePrevious);
			task.setRowElement(rowList.get(rowIndex));

			out.format("TreeRankInstanceTask %s %s %s %s started before taskExecutor.execute\n", tree, rank, treeRankInstance, rowIndex);
			try {
				columnSizeReference.set(taskExecutor.submit(task).get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			out.format("TreeRankInstanceTask %s %s %s %s started after taskExecutor.execute\n", tree, rank, treeRankInstance, rowIndex);
		});

		currentThread().interrupt();

		return columnSizeReference.get();
	}

	public static final String getTreeRankInstance(int treeRankInstanceIndex) {
		return (treeRankInstanceIndex == 0) ? "Researchable" : "Premium";
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

	public final void setTreeRankInstanceElement(WebElement treeRankInstanceElement) {
		if (this.treeRankInstanceElement == null) {
			this.treeRankInstanceElement = treeRankInstanceElement;
		}
	}
}
