package com.xuanyiying.bookstore.data.parse;

/**
 * @author 
 *
 */
public class MainClient {

	public static void main(String[] args) throws Exception {
        String url = "http://www.iisbn.com/";
        //String jdHurl = "https://book.jd.com/";
		///HtmlParserContext ctx = new DefaultHtmlParserContext(new DSHtmlParser(),url);
		//DynamicHtmlParser parser = HtmlParserFactory.create(jdHurl,JDHtmlParser.class);
		DynamicHtmlParser parser1 = HtmlParserFactory.create(url,DSHtmlParser.class);
		//String keywords = "青春文学";
		parser1.parse();
		//parser1.parse();
		//parser.parse(null ,keywords);
     }

 }
