package io.github.guiritter.war_thunder_wiki_crawler;

import static java.lang.System.out;
import static java.lang.Thread.currentThread;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@Scope(SCOPE_PROTOTYPE)
public class RowTask implements Runnable {

	@Autowired
	ApplicationContext applicationContext;

	private WebElement rowElement;

	Integer rowIndex;

	@Autowired
	ThreadPoolTaskExecutor taskExecutor;

	String tree;

	@Override
	public void run() {
		out.format("RowTask %s %s\n", tree, rowIndex);

		var columnList = rowElement.findElements(By.cssSelector("td"));

		IntStream.range(0, columnList.size()).forEach(columnIndex -> {
			var task = applicationContext.getBean("columnTask", ColumnTask.class);

			task.setTree(tree);
			task.setRowIndex(rowIndex);
			task.setColumnIndex(columnIndex);
			task.setColumnElement(columnList.get(columnIndex));

			out.format("RowTask %s %s started before taskExecutor.execute\n", tree, rowIndex);
			try {
				taskExecutor.submit(task).get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			out.format("RowTask %s %s started after taskExecutor.execute\n", tree, rowIndex);
		});

		currentThread().interrupt();
	}

	public final void setRowElement(WebElement element) {
		if (rowElement == null) {
			rowElement = element;
		}
	}

	public final void setRowIndex(int index) {
		if (rowIndex == null) {
			rowIndex = index;
		}
	}

	public final void setTree(String tree) {
		if (this.tree == null) {
			this.tree = tree;
		}
	}
}
