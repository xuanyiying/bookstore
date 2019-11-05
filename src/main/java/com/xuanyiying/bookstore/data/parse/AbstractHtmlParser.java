package com.xuanyiying.bookstore.data.parse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.xuanyiying.bookstore.data.parse.GetImageTask.GetImageTaskHelper;
import com.xuanyiying.bookstore.data.parse.ParserElementTask.ParserElementTaskHelper;
import com.xuanyiying.bookstore.data.parse.po.Book;
import com.xuanyiying.bookstore.data.store.DataWriter;
import com.xuanyiying.bookstore.data.store.DefaultDataWriter;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public abstract class AbstractHtmlParser implements HtmlParser {
	private final static String ROOT_PATH = System.getProperty("user.dir");
	private final String IMAGE_PATH = ROOT_PATH + "/output/image/";
	private final String EXCEL_PATH = ROOT_PATH + "/output/excel/";
	private DataWriter writer = new DefaultDataWriter();
	protected abstract List<String> parseDocument(Document doc);
  
	protected abstract Book parseElements(Document doc);
    protected abstract String buildUrl(HtmlParserContext ctx, String keywords);
	protected abstract HtmlParserContext getKeyWords(HtmlParserContext ctx);
	protected abstract WebElement getNextPage(WebDriver driver);
    @Override
    public HtmlParserContext parse(HtmlParserContext ctx,String html) {
        Document doc = Jsoup.parse(html);
        List<String> els = parseDocument(doc);
        ThreadPoolExecutor pool = new ThreadPoolExecutor(els.size(), els.size()*2, 60,
				TimeUnit.SECONDS, new ArrayBlockingQueue<>(16));
        for (String el: els) {
		    pool.execute(new ParserElementTask(el, ctx));
        }
		completed(pool);
		pool.shutdown();
        ParserElementTaskHelper.completed();
        getAndWriteImage(ctx);
        new Thread(() -> write(ctx)).start();
        return ctx;
    }

	private void completed(ThreadPoolExecutor pool) {
		while(pool.getActiveCount() != 0) {
			Thread.currentThread();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				log.error("Sleep thread met exception,"
						+ " error info:{}",e);
			}
		}
	}

	Document getDocument(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (FailingHttpStatusCodeException | IOException e) {
			log.error("Parse response html met exception,"
					+ " error info: {} ", e);
		}
		return doc;
	}
	
	private HttpEntity doGet(String url) {
    	CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = buildHttpGet(url);
        CloseableHttpResponse response;
        HttpEntity entity = null;
        try {
			response = httpclient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();     
            if (statusCode == 200){
            	entity = response.getEntity();
            } else {
                EntityUtils.consume(response.getEntity());
            }
        } catch (IOException e){
            log.info("" + e);
        } 
		return entity;
    }
   
    private void getAndWriteImage(HtmlParserContext ctx) {
    	List<?> data = ctx.get(DefaultHtmlParserContext.CACHE_BOOKS);
    	log.info("data size: {}" ,data.size());
    	ThreadPoolExecutor pool = new ThreadPoolExecutor(data.size(), data.size()*5, 60, 
 				TimeUnit.SECONDS, new ArrayBlockingQueue<>(16));
    	Book book;
    	for (Object obj : data) {
    		book =  (Book)obj;
    		if (Objects.nonNull(book) && StringUtils.isNotEmpty(book.getImageUrl())) {
				pool.execute(new GetImageTask(ctx, book));
    		}
		}
		completed(pool);
		pool.shutdown();
    	GetImageTaskHelper.completed();
    }
    
     Book getAndWriteImage(HtmlParserContext ctx, Book book) {
		InputStream is;
		String imageUrl = buildUrl(ctx, book.getImageUrl());
		try {
			log.info("Image url: {}",book.getImageUrl());
			is = doGet(imageUrl).getContent();
			String tmpFilePath = IMAGE_PATH + this.getClass().getSimpleName() 
					+ File.separator + book.getIsbn() +".jpg";
			File tempFile = new File(tmpFilePath);
			if(!tempFile.exists()) {
				tempFile.getParentFile().mkdirs();
			}
			FileUtils.copyInputStreamToFile(is, tempFile);
			book.setImagePath(tmpFilePath);
		} catch (UnsupportedOperationException | IOException e) {
			log.error("Close response met exception, error info: {} ",e);
		}
		return book;
    }
  
     private void write(HtmlParserContext ctx){
         List<?> data = ctx.get(DefaultHtmlParserContext.CACHE_BOOKS);
         String className = ctx.parser().getClass().getSimpleName();
         String fileName = EXCEL_PATH + File.separator  + className +".xlsx";
         boolean success = writer.writeToExcel(fileName, data);
         if (success) {
             ctx.clearCache(DefaultHtmlParserContext.CACHE_BOOKS);
         }
     }
     private HttpGet buildHttpGet(String url) {
 		if (StringUtils.isEmpty(url)) {
 			throw new NullArgumentException();
 		}
 		HttpGet httpGet = new HttpGet(url);
 		httpGet.setHeader("user-agent",
          "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                  "Chrome/70.0.3538.102 Safari/537.36");
 		return httpGet;
 	}
}
