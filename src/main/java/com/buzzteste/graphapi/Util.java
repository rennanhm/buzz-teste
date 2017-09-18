package com.buzzteste.graphapi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Util {

	public static Date convertStrToDate(String date) {
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date result = null;
		try {
			result = formatDate.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String dateDaysAgo(int days) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime then = now.minusDays(days);
		return then.format(format);
	}
}
