package com.xuanyiying.bookstore.data.parse;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultHtmlParserContext implements HtmlParserContext {
	static final String CACHE_KEYWORDS="keywords";
	static final String CACHE_BOOKS="books";
    private final Map<String,Object> CACHE = new ConcurrentHashMap<>();
    private AbstractHtmlParser parser;
	private String baseUrl;
	DefaultHtmlParserContext(AbstractHtmlParser parser, String baseUrl) {
		super();
		this.baseUrl = baseUrl;
		this.parser = parser;
	}
	@Override
	public Map<String,Object> getCache() {
		synchronized (CACHE) {
			return CACHE;
		}	
	}
	@Override
	public void clearCache(String key) {
		synchronized (CACHE) {
			this.CACHE.remove(CACHE_BOOKS);
		}	
	}
	@Override
	public List<?> get(String key) {
		return (List<?>)CACHE.get(key);
	}
	@Override
	public AbstractHtmlParser parser() {
		return parser;
	}
	@Override
	public String getBaseUrl() {
		return baseUrl;
	}
	
}
