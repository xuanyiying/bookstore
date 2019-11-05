package com.xuanyiying.bookstore.data.handle;

import com.xuanyiying.bookstore.data.parse.DSHtmlParser;
import com.xuanyiying.bookstore.data.parse.DynamicHtmlParser;
import com.xuanyiying.bookstore.data.parse.HtmlParserFactory;

public class MainClient {
	public static void main(String[] args) throws Exception {
        String url = "http://www.iisbn.com/";
        String keywords = "青春文学";
		// Create parser method one
		 DynamicHtmlParser parser = HtmlParserFactory.create(url,DSHtmlParser.class);
		//Parser html
		parser.parse(keywords);
     }  	
}
