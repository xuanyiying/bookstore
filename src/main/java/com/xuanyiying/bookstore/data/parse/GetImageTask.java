package com.xuanyiying.bookstore.data.parse;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import com.xuanyiying.bookstore.data.parse.po.Book;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class GetImageTask implements Runnable{
    	private static HtmlParserContext ctx;
    	private Book book;
    	private static ThreadLocal<Book> local = new ThreadLocal<>();
    	private static final List<Book> books = new CopyOnWriteArrayList<>();
    	GetImageTask(HtmlParserContext ctx,Book book){
    		GetImageTask.ctx = ctx;
			this.book = book;
    	}
		@Override
		public void run() {	
			local.set(book);
			log.info("book info : {}", local.get().toString());
			if (Objects.nonNull(local.get())){
				Book newBook = ctx.parser().getAndWriteImage(ctx,local.get());
				synchronized(books) {
					books.add(newBook);
				}
			}
		}
		
		static class GetImageTaskHelper{
    		static void completed() {
				completedTask();
			}	
		}
		private static void completedTask() {
			ctx.getCache().put(DefaultHtmlParserContext.CACHE_BOOKS, books);		
		}
   
}
