package com.xuanyiying.bookstore.data.parse;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.util.StringUtils;

import com.xuanyiying.bookstore.data.parse.po.BaseInfo;
import com.xuanyiying.bookstore.data.parse.po.Book;
import com.xuanyiying.bookstore.util.BeanUtil;

import lombok.extern.slf4j.Slf4j;

/**
 *   京东书籍数据 爬取实现  {@link} https://book.jd.com/
 * @author
 *
 */
@Slf4j
public class JDHtmlParser extends AbstractHtmlParser {
	private static final String BOOK_STORE = "booksort.html";

	@Override
    protected List<String> parseDocument(Document doc) { 
        Elements plist = doc.select("div[id='J_searchWrap']")
        		.select("div[id='J_container']")
        		.select("div[id='J_main']")
        		.select("div[id='plist']")
        		.select("ul");
        Elements liList = plist.select("li");
        List<String> urls = new ArrayList<>();
        String goodsUrl;
        for (Element item : liList) {
        	 goodsUrl = item.select("div[class='gl-i-wrap j-sku-item']")
        			 .select("div[class='p-img']")
        			 .select("a").attr("href");
        	 if(!StringUtils.isEmpty(goodsUrl)) {
        		 urls.add(goodsUrl);     
        	 }	    
        }
        log.info("{}",urls.size());
        return urls;
    }

	@Override
	protected Book parseElements(Document doc) {
		BaseInfo baseInfo = new BaseInfo();
		Elements product = doc.select("div[id='p-box']");
		Elements content = product.select("div[id='product-intro']");
		Elements bookInfo = content.select("div[class='m-item-inner']")
				.select("div[id='itemInfo']");
		Elements name = bookInfo.select("div[id='name']");
		String bookName = name.select("div[class='sku-name']").text();
		String author = name.select("div[class='p-author']").select("a").text();
		String price = bookInfo.select("div[class='summary']")
				.select("div[id='summary-price']")
				.select("div[class='dd']")
				.select("span[class='pricing']")
				.select("del[id='page_maprice']")
				.text().replace("￥", "");
		Elements detail = doc.select("div[class='w']")
				.select("div[class='detail']")
				.select("div[id='detail']")
				.select("div[class='tab-con']");
		String imageUrl = content.select("div[id='spec-n1']").select("img").attr("src");
		Elements detailExtendInfo = detail.select("div[class='p-parameter']")
				.select("ul[id='parameter2']")
				.select("li");
		String publishingHouse = detailExtendInfo.get(0).attr("title");
		String isbn = detailExtendInfo.get(1).attr("title");
		String publishedTimes = detailExtendInfo.get(2).attr("title");
		String publishedTime = detailExtendInfo.get(7).attr("title");
		String detailHtml = detail.select("div[id='J-detail-content']").outerHtml();
		String detailImageUrl = detail.select("div[id='J-detail-content']").select("div[id='detail-tag-id-1']")
				.select("div[class='book-detail-content']")
				.select("p").select("img").attr("src");
		baseInfo.setPublishTimes(publishedTimes);
		baseInfo.setIsbn(isbn);
		baseInfo.setAuthor(author);
		baseInfo.setPublishingHouse(publishingHouse);
		baseInfo.setPublishedTime(publishedTime);
		baseInfo.setBookName(bookName);
		baseInfo.setPrice(price);
		Book book = new Book();
		BeanUtil.copyProperties(baseInfo, book);
		book.setTotal("#");
		book.setImageUrl(imageUrl);
		book.setDetailImageUrl(detailImageUrl);
		book.setDetailHtml(detailHtml);
		log.info("Book info: {} ", book.toString());
		return book;
	}
 
	@Override
	protected String buildUrl(HtmlParserContext ctx, String keywords) {
		String searchUrl = ctx.getBaseUrl();
		if(!keywords.startsWith("https:") 
				|| !keywords.startsWith("http:")){
			searchUrl = "https:" + keywords;
		}	
		return searchUrl;
	}
	
	@Override
	protected HtmlParserContext getKeyWords(HtmlParserContext ctx) {
		String baseUrl = ctx.getBaseUrl();
		if(!baseUrl.endsWith("/")) {
			baseUrl += "/";
		}
		String url = baseUrl + BOOK_STORE;
		List<Object> keywords = new ArrayList<>();
		Document doc = super.getDocument(url);
		Elements category = doc.select("div[class='w main']")
        		.select("div[class='mc']");
        Elements divList = category.select("dl");
		for (Element el : divList) {
			Elements es = el.select("dd").select("em");
			for (Element e :  es) {
				keywords.add(e.select("a").attr("href"));
			}
		}
		ctx.getCache().put("keywords", keywords);
		return ctx;
	}

	@Override
	protected WebElement getNextPage(WebDriver driver) {
		return driver.findElement(By.className("pn-next"));
	}



  
}
