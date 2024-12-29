package io.github.guiritter.war_thunder_wiki_crawler;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static java.lang.System.out;
import static java.lang.Thread.currentThread;
import static java.nio.file.Files.newBufferedWriter;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.util.stream.IntStream.range;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Scope(SCOPE_PROTOTYPE)
public final class UnitTreeTask implements Runnable {

	@Autowired
	private ApplicationContext applicationContext;

	@Value("${folderPath}")
	private String folderPath;

	private String dataTreeId;

	private WebElement unitTreeElement;

	public static final Map<String, String> dataTreeIdMap = Map.ofEntries(
			buildEntry("usa", "us"),
			buildEntry("germany", "germ"),
			buildEntry("ussr", "ussr"),
			buildEntry("britain", "uk"),
			buildEntry("japan", "jp"),
			buildEntry("china", "cn"),
			buildEntry("italy", "it"),
			buildEntry("france", "fr"),
			buildEntry("sweden", "sw"),
			buildEntry("israel", "il")
			);

	public static final Map<String, String> jsonMap = Map.ofEntries(
			buildEntry("us", "US"),
			buildEntry("germ", "Germany"),
			buildEntry("ussr", "USSR"),
			buildEntry("uk", "UK"),
			buildEntry("jp", "Japan"),
			buildEntry("cn", "China"),
			buildEntry("it", "Italy"),
			buildEntry("fr", "France"),
			buildEntry("sw", "Sweden"),
			buildEntry("il", "Israel"));

	@Autowired
	private Map<String, Table> tableMap;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	public static final AbstractMap.SimpleEntry<String, String> buildEntry(String key, String value) {
		return new AbstractMap.SimpleEntry<String, String>(key, value);
	}

	@Override
	public void run() {
		dataTreeId = unitTreeElement.getAttribute("data-tree-id");

		// // TODO debug
		// if (dataTreeId.compareTo("germany") != 0) {
		// 	currentThread().interrupt();
		// 	return;
		// }

		out.println("UnitTreeTask " + dataTreeId);

		var treeRankList = unitTreeElement.findElements(By.cssSelector(".wt-tree_rank"));

		var tree = dataTreeIdMap.get(dataTreeId);

		range(0, treeRankList.size()).forEach(treeRankIndex -> {
			var task = applicationContext.getBean("treeRankTask", TreeRankTask.class);

			task.setTree(tree);
			task.setRank(treeRankIndex + 1);
			task.setTreeRankElement(treeRankList.get(treeRankIndex));

			out.println("UnitTreeTask started before taskExecutor.execute");
			try {
				taskExecutor.submit(task).get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			out.println("UnitTreeTask started after taskExecutor.execute");
		});

		try {
			var output = (new ObjectMapper()).enable(INDENT_OUTPUT).writeValueAsString(tableMap.get(tree));

			var writer = newBufferedWriter(
					Paths
							.get(folderPath)
							.resolve(jsonMap.get(tree) + ".json"),
					CREATE,
					TRUNCATE_EXISTING);

			writer.write(output);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		currentThread().interrupt();
	}

	public final void setUnitTreeElement(WebElement unitTreeElement) {
		if (this.unitTreeElement == null) {
			this.unitTreeElement = unitTreeElement;
		}
	}
}
