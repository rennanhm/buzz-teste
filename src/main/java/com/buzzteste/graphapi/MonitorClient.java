package com.buzzteste.graphapi;

import java.io.BufferedReader;


import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.buzzteste.jdbc.PostDao;
import com.buzzteste.models.Post;

public class MonitorClient {

	public String getPosts(String since, String until) {
		PostDao postDao = new PostDao();
		JSONArray response = postDao.getPostsBetweenDate(since, until);
		return response.toString();

	}

	public String getVolume(String since, String until) {
		PostDao postDao = new PostDao();
		JSONArray response = postDao.getVolumeBetweenDate(since, until);
		return response.toString();

	}

	public void importPosts(int days, String facebookPage) {
		String since = Util.dateDaysAgo(days);

		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			String url = Constants.GRAPH_API_URL + facebookPage + "/posts?since=" + since + "&access_token="
					+ Constants.ACCESS_TOKEN;
			boolean hasNext = false;
			do {
				String jsonResponse = requestGet(url);
				JSONObject jsonObject = new JSONObject(jsonResponse);
				JSONArray postsJson = (JSONArray) jsonObject.get("data");
				PostDao postDao = new PostDao();
				for (int i = 0; i < postsJson.length(); i++) {
					JSONObject postJson = postsJson.getJSONObject(i);
					Post post = new Post();
					try {
						post.setMessage(postJson.getString("message"));
					} catch (JSONException e) {
						post.setMessage("");
					}
					post.setPost_id(postJson.getString("id"));
					post.setCreated_time(Util.convertStrToDate(postJson.getString("created_time")));
					postDao.add(post);
				}

				boolean hasPagination = jsonObject.has("paging");
				if (hasPagination) {
					JSONObject pagination = jsonObject.getJSONObject("paging");
					hasNext = pagination.has("next");
					if (hasNext) {
						url = pagination.getString("next");
					}
				}
			} while (hasNext);

		} catch (IOException e) {

		}
	}

	private String requestGet(String url) {
		String response = null;
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			HttpGet request = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(request);
			BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			response = rd.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}
}
