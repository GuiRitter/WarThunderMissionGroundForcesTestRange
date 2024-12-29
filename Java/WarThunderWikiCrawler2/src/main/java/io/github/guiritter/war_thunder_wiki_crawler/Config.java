package io.github.guiritter.war_thunder_wiki_crawler;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class Config {

	public static final WebDriver buildWebDriver(){
		ChromeOptions chromeOptions = new ChromeOptions();

		chromeOptions.addArguments("--remote-allow-origins=*","ignore-certificate-errors");
		chromeOptions.setBinary("C:\\desenvolvimento\\web\\Selenium\\Chrome\\chrome-win64\\chrome.exe");

		return new ChromeDriver(chromeOptions);
	}

	@Bean
	public Map<String, String> fixMap () {
		return new HashMap<>();
	}

	@Bean
	public Map<String, Table> tableMap () {
		return new HashMap<>();
	}

	@Bean
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(Integer.MAX_VALUE);
		executor.setMaxPoolSize(Integer.MAX_VALUE);
		executor.setThreadNamePrefix("war_thunder_wiki_crawler_task_executor_thread");
		executor.setDaemon(true);
		executor.initialize();
		return executor;
	}

	@Bean
	public WebDriver webDriver(){
		return buildWebDriver();
	}
}
