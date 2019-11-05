package com.xuanyiying.bookstore.data.parse.po;

public class BaseInfo {
    private String isbn;
    private String bookName;
    private String price;
    private String author;
    private String publishingHouse;
    private String publishedTime;
    private String publishTimes;
	@Override
	public String toString() {
		return "BaseInfo [isbn=" + isbn + ", bookName=" + bookName + ", price=" + price + ", author=" + author
				+ ", publishingHouse=" + publishingHouse + ", publishedTime=" + publishedTime + ", publishTimes="
				+ publishTimes + "]";
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPublishingHouse() {
		return publishingHouse;
	}
	public void setPublishingHouse(String publishingHouse) {
		this.publishingHouse = publishingHouse;
	}
	public String getPublishedTime() {
		return publishedTime;
	}
	public void setPublishedTime(String publishedTime) {
		this.publishedTime = publishedTime;
	}
	public String getPublishTimes() {
		return publishTimes;
	}
	public void setPublishTimes(String publishTimes) {
		this.publishTimes = publishTimes;
	}

}
