package com.poiin.yourown;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends Activity {
	
	private String imageLink= "http://graph.facebook.com/{ID}/picture";
	private ImageView profilePicture;
	private TextView nameTextView;
	private TextView lastNameTextView;
	private JSONObject me;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.profile);
		me = ((ApplicationState)getApplication()).getMe();
		this.profilePicture =
			(ImageView) findViewById(R.id.profilePicture);	
		this.nameTextView=(TextView)findViewById(R.id.profileName);
		this.lastNameTextView=(TextView)findViewById(R.id.profileLastName);
	}

	@Override
	public void onStart(){
		super.onStart();
		startImageRetrieval();
		nameTextView.setText(getMyFacebookProperty("name"));
		lastNameTextView.setText(getMyFacebookProperty("last_name"));
	}
	private void startImageRetrieval() {
		new Thread(new Runnable() {			
			@Override
			public void run() {
				loadImageHandler.sendEmptyMessage(0);
			}
		}).start();
	}

	private String getMyFacebookProperty(String property) {
		try {
			return me.getString(property);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Handler loadImageHandler = new Handler() {
		public void handleMessage(Message msg) {
				try {
					URL url = new URL(imageLink.replace("{ID}", getMyFacebookProperty("id")));
					URLConnection conn = url.openConnection();
					conn.connect();
					BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
					Bitmap bm = BitmapFactory.decodeStream(bis);
					bis.close();
					profilePicture.setImageBitmap(bm);
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	};
}
