package com.xuanyiying.bookstore.data.store.handler.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import com.xuanyiying.bookstore.data.store.JdbcUtil;
import com.xuanyiying.bookstore.data.store.handler.JdbcExecutorHandler;

public class JdbcExecutorHandlerImpl implements JdbcExecutorHandler {
	@Override
	public int execute(String sql, StatementParameter... values) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		int count = 0;
		try {
			connection = getConnection();
			statement = getPreparedStatement(connection, sql, values);
			count = getRowCount(connection, statement);
		} catch (Exception e) {

		}
		return count;
	}

	@Override
	public <T> List<T> executeQuery(String sql, Class<T> entityClazz, StatementParameter... values) throws Exception {
		try {
			Connection connection = getConnection();
			PreparedStatement statement = getPreparedStatement(connection, sql, values);
			return ResultHandler.getInstance().handleResultSet(connection, statement, entityClazz);
		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public <T> T executeSingleQuery(String sql, Class<T> entityClazz, StatementParameter... values) throws Exception {
		T obj = null;
		try {
			Connection connection = getConnection();
			PreparedStatement statement = getPreparedStatement(connection, sql, values);
			obj = ResultHandler.getInstance().handleSingleResultSet(connection, statement, entityClazz);
		} catch (Exception e) {

		}
		return obj;
	}

	private PreparedStatement getPreparedStatement(Connection connection, String sql, StatementParameter... values)
			throws Exception {
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			if (values != null && values.length > 0) {
				for (int i = 0; i < values.length; i++) {
					Class<?> k = values[i].getType();
					Object v = values[i].getValue();
					if (v == null) {
						setNull(statement, i + 1, k.getClass().getSimpleName(), getSqlTpyeCode(k.getClass()));
					} else {
						if (int.class.equals(k) || Integer.class.equals(k)) {
							statement.setInt(i + 1, (int) v);
						} else if (String.class.equals(k)) {
							statement.setString(i + 1, (String) v);
						} else if (Date.class.equals(k)) {
							statement.setDate(i + 1, (Date) v);
						}
					}
				}
			}
		} catch (Exception e) {
		}
		return statement;
	}

	private int getSqlTpyeCode(Class<? extends Object> class1) {
		if (null != class1) {
			if (class1.equals(String.class)) {
				return Types.VARCHAR;
			} else if (int.class.equals(class1) || Integer.class.equals(class1)) {
				return Types.INTEGER;
			} else if (Date.class.equals(class1)) {
				return Types.DATE;
			}
		}
		return 0;
	}

	private void setNull(PreparedStatement statement, int parameterIndex, String typeName, int sqlType)
			throws SQLException {
		statement.setNull(parameterIndex, sqlType, typeName);
	}

	private Connection getConnection() throws Exception {
		Connection connection = null;
		try {
			connection = JdbcUtil.getConnection();
			connection.setAutoCommit(false);
		} catch (Exception e) {

		}
		return connection;
	}

	private int getRowCount(Connection connection, PreparedStatement statement) throws Exception {
		int rowCount = 0;
		try {
			rowCount = statement.executeUpdate();
			connection.commit();
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
			}
		} finally {
			// closeAllConn(connection, statement, null);
		}
		return rowCount;
	}

}
