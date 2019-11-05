package com.xuanyiying.bookstore.data.parse;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jsoup.nodes.Document;

import com.xuanyiying.bookstore.data.parse.po.Book;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class ParserElementTask implements Runnable{
    	private static HtmlParserContext ctx;
    	private String el;
    	private static List<Book> books = new CopyOnWriteArrayList<>();
    	private static ThreadLocal<String> local = new ThreadLocal<String>();

		public ParserElementTask(String el, HtmlParserContext ctx) {
			ParserElementTask.ctx = ctx;
			this.el = el;
		}
		static HtmlParserContext completedTask() {
    		ctx.getCache().put(DefaultHtmlParserContext.CACHE_BOOKS, books);
			return ctx;	
    	}
		@Override
		public void run() {
			local.set(el);
			log.info(local.get());
			String goodsUrl = ctx.parser().buildUrl(ctx, local.get());	    	
	        log.info(goodsUrl);
		    Document goods = ctx.parser().getDocument(goodsUrl);
			Book book = ctx.parser().parseElements(goods);
			log.info(book.toString());
			synchronized (books) {
				books.add(book);
			}
		}
		
		static class ParserElementTaskHelper{
			public static void completed() {
				completedTask();
			}	
    	}
}
