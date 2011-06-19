package com.poiin.yourown.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.poiin.yourown.poiin.Poiin;

public class RestClientServiceImpl implements RestClientService {
	private static final String HTTP_POIIN_ENDPOINT = "http://192.168.0.5:3000/poiin";
	private HttpClient httpClient = new DefaultHttpClient();

	@Override
	public void sendPoiin(Poiin poiin) {
		Log.i("RestClient", poiin.toString());
		HttpPost post = new HttpPost(HTTP_POIIN_ENDPOINT);
		setPostJsonString(poiin, post);
		sendRequest(post);
	}

	@Override
	public JSONArray getPoiins(JSONObject user) {
		Log.i("RestClient", user.toString());
		HttpGet get = getUrlWithQueryString(user);
		return sendRequest(get);
	}

	private HttpGet getUrlWithQueryString(JSONObject user) {
		try {
			return new HttpGet(HTTP_POIIN_ENDPOINT + "?user_id=" + user.getLong("id"));
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	private JSONArray sendRequest(HttpRequestBase request) {
		try {
			HttpEntity entity = httpClient.execute(request).getEntity();
			String responseAsString = EntityUtils.toString(entity);
			return new JSONArray(responseAsString);
		} catch (Exception e) {
			Log.e("RestClient", "Error in sending request or parsing response, but continuing..", e);
			return new JSONArray();
		}
	}

	private void setPostJsonString(Poiin poiin, HttpPost post) {
		try {
			StringEntity entity = new StringEntity(poiin.toJsonString());
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			post.setEntity(entity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
