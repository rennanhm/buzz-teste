package com.buzzteste.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseDao {
	
	private Connection connection;

	public DatabaseDao() {
		this.connection = new ConnectionFactory().getConnection();
	}
	
	public void createTablePosts() {
		String createTableSql = "CREATE TABLE IF NOT EXISTS posts (\n" + 
				"	id BIGINT NOT NULL AUTO_INCREMENT,\n" + 
				"	created_time datetime,\n" + 
				"	message mediumtext,\n" + 
				"        post_id varchar(40),\n" + 
				"	primary key (id),\n" + 
				"	UNIQUE (post_id)\n" + 
				");\n" + 
				"";
		try {
			PreparedStatement stmt = connection.prepareStatement(createTableSql);
			stmt.execute();
			stmt.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
