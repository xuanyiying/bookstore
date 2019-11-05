package com.xuanyiying.bookstore.data.parse.po;

public class Book {
	private String imageUrl;
    private String imagePath;
    private String isbn;
    private String bookName;
    private String price;
    private String total;
    private String discount;
    private String author;
    private String publishingHouse;
    private String publishedTime;
	private String publishTimes;
    private String detailHtml;
    private String detailImageUrl;
    private BaseInfo info;
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
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
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
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
	public String getDetailHtml() {
		return detailHtml;
	}
	public void setDetailHtml(String detailHtml) {
		this.detailHtml = detailHtml;
	}
	public String getDetailImageUrl() {
		return detailImageUrl;
	}
	public void setDetailImageUrl(String detailImageUrl) {
		this.detailImageUrl = detailImageUrl;
	}
	public BaseInfo getInfo() {
		return info;
	}
	public void setInfo(BaseInfo info) {
		this.info = info;
	}
	@Override
	public String toString() {
		return "Book [imageUrl=" + imageUrl + ", imagePath=" + imagePath + ", isbn=" + isbn + ", bookName=" + bookName
				+ ", price=" + price + ", total=" + total + ", discount=" + discount + ", author=" + author
				+ ", publishingHouse=" + publishingHouse + ", publishedTime=" + publishedTime + ", publishTimes="
				+ publishTimes + ", detailHtml=" + detailHtml + ", detailImageUrl=" + detailImageUrl + "]";
	}
}
