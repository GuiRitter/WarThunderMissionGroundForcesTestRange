package io.github.guiritter.war_thunder_wiki_crawler;

import static io.github.guiritter.war_thunder_wiki_crawler.TreeRankInstanceTask.getTreeRankInstance;
import static java.lang.System.out;
import static java.lang.Thread.currentThread;
import static java.util.stream.IntStream.range;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

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
 * Represents a rank (I, II, III, etc), containing both Researchable and Premium, and composed of more rows containing the vehicles.
 */
@Component
@Scope(SCOPE_PROTOTYPE)
public class TreeRankTask implements Runnable {

	@Autowired
	ApplicationContext applicationContext;

	Integer rank;

	Integer rowIndex;

	@Autowired
	ThreadPoolTaskExecutor taskExecutor;

	String tree;

	Integer treeGroupIndex;

	private WebElement treeRankElement;

	String treeRankInstance;

	@Override
	public void run() {
		out.format("TreeRankTask %s %s\n", tree, rank);

		var treeRankInstanceList = treeRankElement.findElements(By.cssSelector(".wt-tree_rank-instance"));

		AtomicReference<Integer> columnSizeReference = new AtomicReference<Integer>(null);

		range(0, treeRankInstanceList.size()).forEach(treeRankInstanceIndex -> {
			var task = applicationContext.getBean("treeRankInstanceTask", TreeRankInstanceTask.class);

			var treeRankInstance = getTreeRankInstance(treeRankInstanceIndex);

			task.setTree(tree);
			task.setRank(rank);
			task.setTreeRankInstance(treeRankInstance);
			task.setColumnSizePrevious(columnSizeReference.get());
			task.setTreeRankInstanceElement(treeRankInstanceList.get(treeRankInstanceIndex));

			out.format("TreeRankTask %s %s %s started before taskExecutor.execute\n", tree, rank, treeRankInstance);
			try {
				columnSizeReference.set(taskExecutor.submit(task).get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			out.format("TreeRankTask %s %s %s started after taskExecutor.execute\n", tree, rank, treeRankInstance);
		});

		currentThread().interrupt();
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

	public final void setTreeRankElement(WebElement treeRankElement) {
		if (this.treeRankElement == null) {
			this.treeRankElement = treeRankElement;
		}
	}
}
