package com.xuanyiying.bookstore.data.store;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JdbcUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JdbcUtil {
	private static Connection connect;
	public static Connection getConnection() { 
	    try {
	    	DruidDataSource datasource = createDataSourceFromResource("application.properties");
	    	connect = datasource.getConnection();
		} catch (SQLException | IOException e) {
			log.error("");
		}
		return connect;	
	}
	
	private static DruidDataSource createDataSourceFromResource(String resource) throws IOException {
        Properties properties = new Properties();

        InputStream configStream = null;
        try {
            configStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            properties.load(configStream);
        } finally {
            JdbcUtils.close(configStream);
        }
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.configFromPropety(properties);
        return dataSource;
    }

}
