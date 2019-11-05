package com.xuanyiying.bookstore.data.parse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HtmlParserFactory {
    /**
     *
     * @param url
     * @param clazz
     * @return
     */
    public static DynamicHtmlParser create(String url,
    		Class<? extends AbstractHtmlParser> clazz){
        AbstractHtmlParser  parser = null;
        try {
            parser = clazz.newInstance();
        } catch (Exception e){
            log.error("Initialise HtmlParser failure........" );
        }
        return new SimpleDynamicHtmlParser().
        		setContext(new DefaultHtmlParserContext(parser,url));
    }

}
