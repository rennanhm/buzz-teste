package com.buzzteste.jdbc;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	public static final String URL = "jdbc:mysql://localhost/buzzmonitor_db?useSSL=false";
	public static final String USER = "root";
	public static final String PASSWORD = "root";

	public Connection getConnection() {
		try {
			return DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}