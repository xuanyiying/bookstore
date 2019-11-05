package com.xuanyiying.bookstore.data.store.handler.impl;

public final class StatementParameter {
	private Class<?> type;
	private Object value;
	public StatementParameter(Class<?> type, Object value) {
		super();
		this.type = type;
		this.value = value;
	}
	public Class<?> getType() {
		return type;
	}
	public void setType(Class<?> type) {
		this.type = type;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	

}
