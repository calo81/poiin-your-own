package com.poiin.yourown.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.poiin.yourown.model.JsonStringSupport;
import com.poiin.yourown.people.message.UserMessage;
import com.poiin.yourown.poiin.Poiin;

public class RestClientServiceImpl implements RestClientService {
	private static final String SERVER_HOST = "http://192.168.1.133:3000/";
	private static final String HTTP_POIIN_ENDPOINT = SERVER_HOST + "poiin";
	private static final String HTTP_MESSAGE_ENDPOINT = SERVER_HOST + "message";
	private static final String HTTP_USER_ENDPOINT = SERVER_HOST + "user";
	private HttpClient httpClient;

	public RestClientServiceImpl() {
		configureHttpClient();
	}

	@Override
	public void sendPoiin(Poiin poiin) {
		Log.i("RestClient", poiin.toString());
		HttpPost post = new HttpPost(HTTP_POIIN_ENDPOINT);
		setPostJsonString(poiin, post);
		sendRequestAndGetJSONArray(post);
	}

	@Override
	public void sendUserMessage(UserMessage message) {
		Log.i("RestClient", message.toString());
		HttpPost post = new HttpPost(HTTP_MESSAGE_ENDPOINT);
		setPostJsonString(message, post);
		sendRequestAndGetJSONArray(post);
	}

	@Override
	public void acknowledgeMessage(String id, String userId) {
		HttpDelete delete = new HttpDelete(HTTP_MESSAGE_ENDPOINT + "/" + id
				+ "?user_id=" + userId);
		sendRequestAndGetJSONArray(delete);
	}

	@Override
	public JSONObject isUserRegistered(String userId) {
		HttpGet get = new HttpGet(HTTP_USER_ENDPOINT + "/" + userId);
		return sendRequestAndGetJSONObject(get);
	}

	@Override
	public void registerUser(JSONObject me) {
		HttpPost post = new HttpPost(HTTP_USER_ENDPOINT);
		setPostJsonString(me.toString(), post);
		sendRequestAndGetJSONArray(post);
	}

	private JSONArray sendRequestAndGetJSONArray(HttpRequestBase request) {
		try {
			String responseAsString = doRequestAndGetString(request);
			return new JSONArray(responseAsString);
		} catch (Exception e) {
			Log.e("RestClient",
					"Error in sending request or parsing response, but continuing..",
					e);
			return new JSONArray();
		}
	}

	private JSONObject sendRequestAndGetJSONObject(HttpRequestBase request) {
		try {
			String responseAsString = doRequestAndGetString(request);
			return new JSONObject(responseAsString);
		} catch (Exception e) {
			Log.e("RestClient",
					"Error in sending request or parsing response, but continuing..",
					e);
			return new JSONObject();
		}
	}

	private String doRequestAndGetString(HttpRequestBase request)
			throws IOException, ClientProtocolException {
		HttpEntity entity = httpClient.execute(request).getEntity();
		String responseAsString = EntityUtils.toString(entity);
		return responseAsString;
	}

	private void setPostJsonString(JsonStringSupport jsonString, HttpPost post) {
		setPostJsonString(jsonString.toJsonString(), post);
	}

	private void setPostJsonString(String jsonString, HttpPost post) {
		try {
			StringEntity entity = new StringEntity(jsonString);
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			post.setEntity(entity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void configureHttpClient() {
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params,
				HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);

		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		registry.register(new Scheme("https", SSLSocketFactory
				.getSocketFactory(), 443));

		ClientConnectionManager connman = new ThreadSafeClientConnManager(
				params, registry);
		httpClient = new DefaultHttpClient(connman, params);
	}

}
