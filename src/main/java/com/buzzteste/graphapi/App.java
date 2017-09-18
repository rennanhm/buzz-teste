package com.buzzteste.graphapi;

import java.sql.SQLException;

import com.buzzteste.jdbc.DatabaseDao;

public class App {
	public static void main(String[] args) throws SQLException {
		System.out.println("creating tables");
		DatabaseDao dbDao = new DatabaseDao();
		dbDao.createTablePosts();

		System.out.println("starting...");

		MonitorClient mc = new MonitorClient();
		System.out.println("importing posts...");
		mc.importPosts(10, "santanderbrasil");

		String postsByDate = mc.getPosts("20110101", "20171212");
		System.out.println(postsByDate);

		String volumePostsByDate = mc.getVolume("20110101", "20171212");
		System.out.println(volumePostsByDate);

	}
}
