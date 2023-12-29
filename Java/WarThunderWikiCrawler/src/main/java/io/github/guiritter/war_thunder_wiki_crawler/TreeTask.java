package io.github.guiritter.war_thunder_wiki_crawler;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static io.github.guiritter.war_thunder_wiki_crawler.Config.buildWebDriver;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Scope(SCOPE_PROTOTYPE)
public final class TreeTask implements Runnable {

	@Autowired
	private ApplicationContext applicationContext;

	@Value("${folderPath}")
	private String folderPath;

	String href;

	public static final Map<String, String> hrefMap = Map.ofEntries(
			buildEntry("https://wiki.warthunder.com/Category:USA_ground_vehicles", "us"),
			buildEntry("https://wiki.warthunder.com/Category:Germany_ground_vehicles", "germ"),
			buildEntry("https://wiki.warthunder.com/Category:USSR_ground_vehicles", "ussr"),
			buildEntry("https://wiki.warthunder.com/Category:Britain_ground_vehicles", "uk"),
			buildEntry("https://wiki.warthunder.com/Category:Japan_ground_vehicles", "jp"),
			buildEntry("https://wiki.warthunder.com/Category:China_ground_vehicles", "cn"),
			buildEntry("https://wiki.warthunder.com/Category:Italy_ground_vehicles", "it"),
			buildEntry("https://wiki.warthunder.com/Category:France_ground_vehicles", "fr"),
			buildEntry("https://wiki.warthunder.com/Category:Sweden_ground_vehicles", "sw"),
			buildEntry("https://wiki.warthunder.com/Category:Israel_ground_vehicles", "il")
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
		out.println("TreeTask " + href);

		var webDriver = buildWebDriver();
		webDriver.get(href);

		var rowList = webDriver.findElements(By.cssSelector("tr"));

		var tree = hrefMap.get(href);

		range(0, rowList.size()).forEach(rowIndex -> {
			var task = applicationContext.getBean("rowTask", RowTask.class);

			task.setTree(tree);
			task.setRowIndex(rowIndex);
			task.setRowElement(rowList.get(rowIndex));

			out.println("TreeTask started before taskExecutor.execute");
			try {
				taskExecutor.submit(task).get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			out.println("TreeTask started after taskExecutor.execute");
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

		webDriver.quit();
		currentThread().interrupt();
	}

	public final void setHref(String href) {
		if (this.href == null) {
			this.href = href;
		}
	}
}
