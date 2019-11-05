package com.xuanyiying.bookstore.data.parse;

import java.util.List;
import java.util.Map;

public interface HtmlParserContext {
    AbstractHtmlParser parser();
	void clearCache(String key);
	Map<String, Object> getCache();
	List<?> get(String key);
	String getBaseUrl();
}
