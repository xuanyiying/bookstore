package com.xuanyiying.bookstore.data.store.handler.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
/**
 * 	Use ResultHandler hanle the Jdbc query resultResult,
 *  the entity(PO) property name must be hump-style and 
 *  the column names of the table are separated by underscores("_"). 
 *  Support Docbase and Oracle.
 * @author wangying
 *
 */
@Slf4j
public final class ResultHandler {
	private ResultHandler(){
		
	}
	private static class Holder{
		private static ResultHandler instance = new ResultHandler();
	}
	public static ResultHandler getInstance(){
		return Holder.instance;		
	}
	public <T> T handleSingleResultSet(Connection connection,PreparedStatement statement, Class<T> entityClazz) throws Exception{
		List<T> results = null;
		try{
			results = handleResultSet(connection, statement, entityClazz);
			if(null == results || results.size() < 1){
				log.warn("The database is not match result.");
				return null;
			}	
			return results.get(0);
		} catch (Exception e) {
			log.error("");
		}
		return null;
		
	}
	
	public <T> List<T> handleResultSet(Connection connection,PreparedStatement statement, Class<T> entityClazz) throws Exception{
		log.debug("Start handle jdbc query resultSet.............");
		try {
			if(null == connection ){
				throw new NullPointerException("connection is null!");
			}
			if(null == statement ){
				throw new NullPointerException("statement is null!");
			}
			if(null == entityClazz){
				throw new NullPointerException("entityClazz is null!");
			}
			
			List<T> rsList = new ArrayList<T>();
			ResultSet result = statement.executeQuery();
			
			ResultSetMetaData mataData = ((ResultSet) result).getMetaData();
			int columnCount = mataData.getColumnCount();
			Field[] fields = entityClazz.getDeclaredFields();
			T obj = null;
			if(null == fields|| fields.length < 1) return null;
			while(result.next()){
				obj = entityClazz.newInstance();
				for (int i = 0; i < columnCount; i++) {
					int mod =  fields[i].getModifiers();
					fields[i].setAccessible(true);
					if(!Modifier.isFinal(mod) && !Modifier.isStatic(mod)){
						String columnLabel = getTableColumnName(fields[i]);
						String setMethodName = "set" + getUpperPropertyName(fields[i]);
						Method  method =  null;
						if(fields[i].getType().equals(String.class)){
							method = entityClazz.getMethod(setMethodName, String.class);		
							String value = result.getString(columnLabel);
							method.invoke(obj,value);
						} else if(fields[i].getType().equals(Integer.class) 
								|| fields[i].getType().equals(int.class)){
							int value = result.getInt(columnLabel);
							method = entityClazz.getMethod(setMethodName, Integer.class);
							method.invoke(obj, value);
						}					
					}				
				}
				rsList.add(obj);
			}
			log.debug("End handle jdbc query resultSet.............");
			return rsList;
		} catch (Exception e) {
			
			try {
				connection.rollback();
			} catch (SQLException e1) {
				
			}
			throw new Exception(e);			
		} finally {
			//closeAllConn(connection, statement, null);
		}	
	}
	
	public  String  getTableColumnName(Field field) throws NullPointerException {
		if(null == field){
			throw new NullPointerException("Field is null");
		}
		char[] chars = field.getName().toCharArray();
		String tem = "";
		for (char c : chars) {
			if(Character.isUpperCase(c)){
				tem = tem + "_" + String.valueOf(c).toLowerCase();
			}else{
				tem  = tem + String.valueOf(c);
			}
		}
		return tem.toString();
	}
	
	private  String  getUpperPropertyName(Field field) throws NullPointerException{
		if(null == field){
			throw new NullPointerException("Field is null");
		}
		String fieldName = field.getName();
		String propertyName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		return propertyName;
	}
	
}
