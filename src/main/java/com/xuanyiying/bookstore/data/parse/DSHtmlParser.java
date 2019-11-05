package com.xuanyiying.bookstore.data.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
 *   丁书网数据 爬取实现  {@link} http://www.iisbn.com
 * @author 
 *
 */
@Slf4j
public class DSHtmlParser extends AbstractHtmlParser{
	private static final Pattern pattern = Pattern.compile("[^0-9]");
    @Override
    protected List<String> parseDocument(Document doc) { 
        Elements ulList = doc.select("div[class='main']")
        		.select("div[class='pro_list']");
        Elements liList = ulList.select("li");
        List<String> urls = new ArrayList<>();
        String goodsUrl;
        for (Element item : liList) {
        	 goodsUrl = item.select("span").select("a").attr("href");
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
	    Elements product = doc.select("body[class='root1200']");
	    Elements content = product.select("div[class='product-main-content w fix']");
	    Elements productInfo = content.select("div[class='product-info-box']");
	    String bookName = productInfo.select("h1").text();  
	    Elements messboxInfo = content.select("div[class='messbox_info']").select("span");
	    String author = spilt(messboxInfo.get(0).text());
	    String publishingHouse = spilt(messboxInfo.get(1).text());
	    String publishedTime = spilt(messboxInfo.get(2).text());
	    String productTotal = content.select("div[class='pro-choose-box fix cl']")
	    		.select("span[class='purchase-num']").text();
	    String total = pattern.matcher(productTotal).replaceAll("");
	    Elements detail = product.select("div[class='tab-box-a-a book-tab-detail mt10']");
	    Elements image = content.select("div[class='product-preview']").select("img");
	    String imageUrl = image.attr("src");
		String detailHtml = detail.outerHtml();
	    String isbn = spilt(productInfo.select("li[class='fix']").text());
	    Elements discountPrice = productInfo.select("span[class='snPrice fl pdr5 main-price']").select("em");
		String price = discountPrice.text();
	    baseInfo.setAuthor(author);
	    baseInfo.setPublishingHouse(publishingHouse);
	    baseInfo.setPublishedTime(publishedTime);
	    baseInfo.setBookName(bookName);
	    baseInfo.setIsbn(isbn);
		baseInfo.setPrice(price);
	    Book book = new Book();
	    BeanUtil.copyProperties(baseInfo, book);
	    book.setDetailHtml(detailHtml);
	    book.setImageUrl(imageUrl);
	    book.setTotal(total);
	    log.info("Book info: {} ", book.toString());
		return book;
	}
	 private String spilt(String value) {
		 if(StringUtils.isEmpty(value)) {
			 return "";
		 }
		 value = value.replace("：",":");
		return (value.substring(value.indexOf(":") + 1, 
						value.length())).trim();
	}
 
	@Override
	protected String buildUrl(HtmlParserContext ctx, String keywords) {
		String searchUrl;
		String baseUrl = ctx.getBaseUrl();
		if(!keywords.startsWith(DSWPageIndex.product)) {
			searchUrl = baseUrl + DSWPageIndex.search + "?keywords=" +keywords;
		} else {
			searchUrl = baseUrl + keywords;
		}
		return searchUrl;
	}

	@Override
	protected HtmlParserContext getKeyWords(HtmlParserContext ctx) {
		String url = ctx.getBaseUrl() + DSWPageIndex.booksort;
		List<Object> keywords = new ArrayList<>();
		Document doc = super.getDocument(url);
		Elements booksort = doc.select("div[id='booksort']").select("div[class='mc']");
		for (Element el : booksort) {
			Elements es =el.select("dd").select("em");
			for (Element e :  es) {
				keywords.add(e.text());
			}
		}
		ctx.getCache().put("keywords", keywords);
		return ctx;	
	}

	static class DSWPageIndex{
	   	static String search ="Search.aspx";
	   	static String product = "product.aspx";
	   	static String booksort = "booksort.aspx";
	 
	 }

	@Override
	protected WebElement getNextPage(WebDriver driver) {
		return driver.findElement(By.linkText("下一页"));
	}


}
