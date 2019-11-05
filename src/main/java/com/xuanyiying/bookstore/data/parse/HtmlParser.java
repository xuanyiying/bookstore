package com.xuanyiying.bookstore.data.parse;

public interface HtmlParser {
    /**
     *
     * @param ctx
     * @param  html
     */
	HtmlParserContext parse(HtmlParserContext ctx,String html);
}
