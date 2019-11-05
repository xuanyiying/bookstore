package com.xuanyiying.bookstore.util;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BeanUtil {
	public static void copyProperties(Object source, Object target, boolean decode){
		if(null == source || null == target){
			return;
		}
		Field[] sourceFields = source.getClass().getDeclaredFields();
		if(sourceFields != null){
			for (int i=0; i < sourceFields.length; i++) {
				Object value = null;
				String property = null;
				try {
					sourceFields[i].setAccessible(true);
					value = sourceFields[i].get(source);
					property = sourceFields[i].getName();
					if(null == value || Modifier.isStatic(sourceFields[i].getModifiers())
							|| Modifier.isFinal(sourceFields[i].getModifiers())
							|| Modifier.isPublic(sourceFields[i].getModifiers())){
						continue;
					}
					Class<? extends Object> targetClass = target.getClass();
					Field targetField = null;
					try {
						 targetField = targetClass.getDeclaredField(property);
					} catch (NoSuchFieldException e) {
						log.warn("The Target class don't have [" + property + "] property. ");
					}
					if(null == targetField){
						continue;
					}
					String setMethodName = "set" + property.substring(0, 1).toUpperCase()
							+ property.substring(1);
					Method setMehtod = targetClass.getMethod(setMethodName, targetField.getType());
					setMehtod.setAccessible(true);
					if (null != targetField) {
							//vo convert entity, special character decode. 
						if(decode && targetField.getType().equals(String.class) 
								&& sourceFields[i].getType().equals(String.class)) {
							setMehtod.invoke(target, decode((String) value));
						}else{
							setMehtod.invoke(target,value);
						}
					}			
					  
				} catch (IllegalArgumentException | IllegalAccessException 
						| NoSuchMethodException| SecurityException
						| InvocationTargetException e) {
					log.error("Copy entity properties met error,error info "
							+ ":\n" + e.getStackTrace(), e);
				}				
			}
		}		
	}
	
	private static String decode(String value) {
			try {
				if(StringUtils.isNotEmpty(value)){
					value = URLDecoder.decode(value,"UTF-8");
					value = filterString(value);
					value = value.trim();
					return value;
				}
			} catch (UnsupportedEncodingException e) {
				log.error("{}",e);
			}
			return null;
		}
		    
	private static String filterString(String value) {
		return value.replace("&amp;", "&").replace("&#44;", ",")
				.replace("&#39;", "\'").replace("&quot;", "\"")
				.replace("&plussign;", "+");
	}

	public static void copyProperties(Object source, Class<?> target){
		try {
			copyProperties(source, target.newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			log.error("Copy entity properties met error,error info"
					+ " :{}", e);
		}
	}
	public static void copyProperties(Object source, Object target){
		copyProperties(source, target, false);
	}
	
}



