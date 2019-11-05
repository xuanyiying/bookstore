package com.xuanyiying.bookstore.data.store.handler;

import java.util.List;

import com.xuanyiying.bookstore.data.store.handler.impl.StatementParameter;

public interface JdbcExecutorHandler{

	int execute(String sql, StatementParameter...values) throws Exception;

	<T> T executeSingleQuery(String sql, Class<T> entityClazz,
			StatementParameter...values) throws Exception;

	<T> List<T> executeQuery(String sql, Class<T> entityClazz,
			StatementParameter...values) throws Exception;
	
	
}
