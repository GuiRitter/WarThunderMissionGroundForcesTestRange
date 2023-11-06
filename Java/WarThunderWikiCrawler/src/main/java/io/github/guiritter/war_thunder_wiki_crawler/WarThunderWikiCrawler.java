package io.github.guiritter.war_thunder_wiki_crawler;

import static java.lang.System.out;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
public class WarThunderWikiCrawler implements ApplicationRunner {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	public static void main(String args[]) {
		System.setProperty("webdriver.chrome.driver", "C:\\desenvolvimento\\web\\Selenium\\driver\\chromedriver-win64\\chromedriver.exe");

		var application = new SpringApplication(WarThunderWikiCrawler.class);

		Properties properties = new Properties();
		properties.put("folderPath", args[0]);
		properties.put("fixMapPath", args[1]);

		var env = new StandardEnvironment();
		env.setActiveProfiles("war_thunder_wiki_crawler_profile");
		env.getPropertySources().addFirst(new PropertiesPropertySource("initProps", properties));

		application.setEnvironment(env);
		application.run(args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// out.format("getSourceArgs: %s\n", Arrays.toString(args.getSourceArgs()));
		// out.format("getNonOptionArgs: %s\n", args.getNonOptionArgs());
		// out.format("getOptionNames: %s\n", args.getOptionNames());
		out.println("WarThunderWikiCrawler started before taskExecutor.execute");
		taskExecutor.submit(applicationContext.getBean(RootTask.class)).get();
		out.println("WarThunderWikiCrawler started after taskExecutor.execute");
	}
}
