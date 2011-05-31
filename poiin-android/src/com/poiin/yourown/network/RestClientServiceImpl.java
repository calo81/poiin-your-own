package com.poiin.yourown.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.poiin.yourown.poiin.Poiin;

public class RestClientServiceImpl implements RestClientService{
	private static final String HTTP_POIIN_ENDPOINT = "http://localhost:3000/poiin";
	private HttpClient httpClient = new DefaultHttpClient();
	
	public void sendPoiin(Poiin poiin) {
		Log.i("RestClient", poiin.toString());
		HttpPost post = new HttpPost(HTTP_POIIN_ENDPOINT);
		setPostJsonString(poiin, post);
		sendRequest(post);
	}
	
	private void sendRequest(HttpPost post){
		try {
			httpClient.execute(post);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setPostJsonString(Poiin poiin, HttpPost post) {
		try {
			post.setEntity(new StringEntity(poiin.toJsonString()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
