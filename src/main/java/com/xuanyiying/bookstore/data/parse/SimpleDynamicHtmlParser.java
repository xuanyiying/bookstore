package com.xuanyiying.bookstore.data.parse;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleDynamicHtmlParser implements DynamicHtmlParser {
    private static final String driverPath =
            "D:/Google/Google/Chrome/Application/chromedriver.exe";
    private static ChromeDriverService service;
    private WebDriver driver;
    private AbstractHtmlParser parser;
    private HtmlParserContext context; 
    @Override
    public HtmlParserContext parse(String keywords) {
    	try {
            parse0(keywords);
        } finally {
			quitDriver();
		}
		return context;
    }

    @Override
    public HtmlParserContext parse() {
    	if (parser == null) {
         	parser = context.parser();
 		}
        try {	
        	List <?> keywords = parser.getKeyWords(context)
                    .get(DefaultHtmlParserContext.CACHE_KEYWORDS);
        	if(keywords == null || keywords.size() ==0) {
        		log.warn("Need to parser html pages is 0");
        		return context;
        	}
        	ThreadPoolExecutor pool = new ThreadPoolExecutor(100, 200, 60,
       				TimeUnit.SECONDS, new ArrayBlockingQueue<>(16));
            for (Object keyword : keywords) {
            	pool.execute(new ParseByKeyWordsTask((String)keyword));
               //parse0((String)keyword);
            }
            pool.shutdown();
            return context;
        } finally {
            quitDriver();
        }
    }
    private void parse0(String keywords) {
        if (parser == null) {
        	parser = context.parser();
		}
        String url = parser.buildUrl(context, keywords);
        driver.get(url);
        //first page
        String html = driver.getPageSource();
        parser.parse(context, html);
        //next page
        WebElement nextPage = parser.getNextPage(driver);
        while(null != nextPage) {
            nextPage.click();
        	driver.get(driver.getCurrentUrl());
        	html =  driver.getPageSource();
        	parser.parse(context, html);
            nextPage = parser.getNextPage(driver);
        }
    }
    private void init() throws IOException {
        log.info("Create chrome driver service, driverPath: {}", driverPath);
    	service = new ChromeDriverService.Builder()
                  .usingDriverExecutable(new File(driverPath))
                  .usingAnyFreePort()
                  .build();
        log.info("Start chrome driver service...");
        service.start();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        log.info("Create chrome driver, driverPath: {}", driverPath);
        driver = new ChromeDriver(service,chromeOptions);
    }

    private void quitDriver() {
        driver.quit();
        service.stop();
        log.info("Stop chrome driver...");
    }
    SimpleDynamicHtmlParser() {
        super();
        try {
            init();
        } catch (IOException e) {
            log.error("Init SimpleDynamicHtmlParser met exception, "
                    + "error info: {}", e);
        }
    }
    SimpleDynamicHtmlParser setContext(HtmlParserContext ctx){
        this.context = ctx;
        return this;
    }
	class ParseByKeyWordsTask implements Runnable{
		private String keyword;
		private ThreadLocal<String> local = new ThreadLocal<>();
		ParseByKeyWordsTask(String keyword){
			this.keyword = keyword;
		}
		@Override
		public void run() {
			local.set(keyword);
			parse0(local.get());
		}		
	}
}
