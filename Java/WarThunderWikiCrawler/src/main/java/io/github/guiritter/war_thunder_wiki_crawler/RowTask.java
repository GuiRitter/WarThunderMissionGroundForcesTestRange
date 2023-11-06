package io.github.guiritter.war_thunder_wiki_crawler;

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
import static java.lang.System.out;

@Component
@Scope(SCOPE_PROTOTYPE)
public class RowTask implements Runnable {

	@Autowired
	ApplicationContext applicationContext;

	private WebElement rowElement;

	Integer rowIndex;

	@Autowired
	ThreadPoolTaskExecutor taskExecutor;

	@Override
	public void run() {
		out.format("%s\n", rowIndex);

		var columnList = rowElement.findElements(By.cssSelector("td"));

		IntStream.range(0, columnList.size()).forEach(columnIndex -> {
			var task = applicationContext.getBean("columnTask", ColumnTask.class);

			task.setRowIndex(rowIndex);
			task.setColumnIndex(columnIndex);
			task.setColumnElement(columnList.get(columnIndex));

			out.println("RowTask started before taskExecutor.execute");
			try {
				taskExecutor.submit(task).get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			out.println("RowTask started after taskExecutor.execute");
		});

		Thread.currentThread().interrupt();
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
}
