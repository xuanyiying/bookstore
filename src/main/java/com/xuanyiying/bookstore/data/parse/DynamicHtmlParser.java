package com.xuanyiying.bookstore.data.parse;

import java.io.IOException;

public interface DynamicHtmlParser  {	
	HtmlParserContext parse() throws IOException;

	HtmlParserContext parse(String keywords) throws IOException;

}
