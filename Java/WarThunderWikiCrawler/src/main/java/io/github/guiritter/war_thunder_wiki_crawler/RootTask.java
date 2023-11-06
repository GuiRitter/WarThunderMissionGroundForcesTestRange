package io.github.guiritter.war_thunder_wiki_crawler;

import static java.lang.System.out;
import static java.lang.Thread.currentThread;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Scope(SCOPE_PROTOTYPE)
public final class RootTask implements Runnable {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private Map<String, String> fixMap;

	@Value("${fixMapPath}")
	private String fixMapPath;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	@Autowired
	private WebDriver webDriver;

	@Override
	public void run() {

		try {
			fixMap.putAll(
					(new ObjectMapper()).readValue(new File(fixMapPath), new TypeReference<Map<String, String>>() {
					}));
		} catch (IOException e) {
			e.printStackTrace();
		}

		webDriver.get("https://wiki.warthunder.com/Ground_vehicles");

		var anchorList = webDriver
				.findElements(By.cssSelector(".wt-class-table tr:first-of-type a[title^=\"Category\"]:first-of-type"));

		anchorList.stream().forEach(element -> {
			var task = applicationContext.getBean("treeTask", TreeTask.class);

			task.setHref(element.getAttribute("href"));

			out.println("RootTask started before taskExecutor.execute");
			try {
				taskExecutor.submit(task).get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			out.println("RootTask started after taskExecutor.execute");
		});

		currentThread().interrupt();
	}
}
