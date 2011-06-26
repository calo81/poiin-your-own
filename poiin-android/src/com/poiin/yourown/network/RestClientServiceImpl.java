package com.poiin.yourown.network;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
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
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.poiin.yourown.model.JsonStringSupport;
import com.poiin.yourown.people.message.UserMessage;
import com.poiin.yourown.poiin.Poiin;

public class RestClientServiceImpl implements RestClientService {
	private static final String SERVER_HOST = "http://192.168.0.5:3000/";
	private static final String HTTP_POIIN_ENDPOINT = SERVER_HOST+"poiin";
	private static final String HTTP_MESSAGE_ENDPOINT = SERVER_HOST+"message";
	private HttpClient httpClient;

	public RestClientServiceImpl(){
		configureHttpClient();
	}

	
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
	
	@Override
	public void sendUserMessage(UserMessage message) {
		Log.i("RestClient", message.toString());
		HttpPost post = new HttpPost(HTTP_MESSAGE_ENDPOINT);
		setPostJsonString(message, post);
		sendRequest(post);
	}
	
	@Override
	public void acknowledgeMessage(String id,String userId) {
		HttpDelete delete =new HttpDelete(HTTP_MESSAGE_ENDPOINT + "/"+id+"?user_id="+userId);
		sendRequest(delete);
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

	private void setPostJsonString(JsonStringSupport jsonString, HttpPost post) {
		try {
			StringEntity entity = new StringEntity(jsonString.toJsonString());
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
		HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);

		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

		ClientConnectionManager connman = new ThreadSafeClientConnManager(params,registry);
		httpClient = new DefaultHttpClient(connman,params);
	}

}
