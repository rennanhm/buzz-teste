package com.buzzteste.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import com.buzzteste.graphapi.Util;
import com.buzzteste.models.Post;

public class PostDao {

	private Connection connection;

	public PostDao() {
		this.connection = new ConnectionFactory().getConnection();
	}

	public void add(Post post) {
		String sql = "insert into posts (created_time, message, post_id) values (?,?,?)";
		try {
			PreparedStatement stmt = this.connection.prepareStatement(sql);

			stmt.setDate(1, new Date(post.getCreated_time().getTime()));
			stmt.setString(2, post.getMessage());
			stmt.setString(3, post.getPost_id());

			stmt.execute();
			stmt.close();

		} catch (SQLIntegrityConstraintViolationException e) {
			System.err.println("Entry not added to database " + e.getMessage());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public JSONArray getPostsBetweenDate(String since, String until) {
		String sql = "SELECT * FROM posts WHERE created_time BETWEEN \"" + since + "\" AND \"" + until + "\"";
		try {
			PreparedStatement stmt = this.connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			JSONArray jsonResponse = new JSONArray();
			while (rs.next()) {
				JSONObject itemDetailsJson = new JSONObject();
				itemDetailsJson.put("id", rs.getString("post_id"));
				itemDetailsJson.put("message", rs.getString("message"));
				itemDetailsJson.put("created_time", rs.getDate("created_time"));
				jsonResponse.put(itemDetailsJson);
			}
			rs.close();
			stmt.close();
			return jsonResponse;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public JSONArray getVolumeBetweenDate(String since, String until) {
		String sql = "select count(*) as sum_posts,created_time from posts where created_time between \"" + since
				+ "\" and \"" + until + "\" group by created_time";
		try {
			PreparedStatement stmt = this.connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			JSONArray jsonResponse = new JSONArray();
			while (rs.next()) {
				JSONObject itemDetailsJson = new JSONObject();
				itemDetailsJson.put("sum_posts", rs.getString("sum_posts"));
				String dateFormated = new SimpleDateFormat("yyyyMMdd").format(rs.getDate("created_time"));
				itemDetailsJson.put("date", dateFormated);
				jsonResponse.put(itemDetailsJson);
			}
			rs.close();
			stmt.close();
			return jsonResponse;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
}
