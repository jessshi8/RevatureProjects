package com.bankapp.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class BankAppDBConnection {
	private static final Logger log = Logger.getLogger(BankAppDBConnection.class);
	ClassLoader classLoader = getClass().getClassLoader();
	InputStream is;
	Properties p = new Properties();
	
	public BankAppDBConnection() {
		is = classLoader.getResourceAsStream("connection.properties");
		try {
			p.load(is);
		} catch (IOException e) {
			log.fatal("An IOException occurred.");
			e.printStackTrace();
		}
	}
	
	public Connection getDBConnection() throws SQLException {
		final String URL = p.getProperty("url");
		final String USERNAME = p.getProperty("username");
		final String PASSWORD = p.getProperty("password");
		return DriverManager.getConnection(URL, USERNAME, PASSWORD);
	}
}
