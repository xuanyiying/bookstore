package com.xuanyiying.bookstore.data.store;

public class DatabaseConfig {
	private String username;
	private String url;
	private String password;
	private String driverClassFullName;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDriverClassFullName() {
		return driverClassFullName;
	}
	public void setDriverClassFullName(String driverClassFullName) {
		this.driverClassFullName = driverClassFullName;
	}

}
