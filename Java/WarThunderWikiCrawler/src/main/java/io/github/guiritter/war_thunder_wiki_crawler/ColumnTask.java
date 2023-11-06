package io.github.guiritter.war_thunder_wiki_crawler;

import static java.lang.System.out;
import static java.lang.Thread.currentThread;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(SCOPE_PROTOTYPE)
public class ColumnTask extends RowTask {

	private WebElement columnElement;

	Integer columnIndex;

	@Override
	public void run() {
		out.format("%s %s\n", rowIndex, columnIndex);

		var modelList = columnElement.findElements(By.cssSelector(".tree-item"));

		IntStream.range(0, modelList.size()).forEach(modelIndex -> {
			var task = applicationContext.getBean("modelTask", ModelTask.class);

			task.setRowIndex(rowIndex);
			task.setColumnIndex(columnIndex);
			task.setModelIndex(modelIndex);
			task.setModelElement(modelList.get(modelIndex));

			out.println("ColumnTask started before taskExecutor.execute");
			try {
				taskExecutor.submit(task).get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			out.println("ColumnTask started after taskExecutor.execute");
		});

		currentThread().interrupt();
	}

	public final void setColumnElement(WebElement element) {
		if (columnElement == null) {
			columnElement = element;
		}
	}

	public final void setColumnIndex(int index) {
		if (columnIndex == null) {
			columnIndex = index;
		}
	}
}
