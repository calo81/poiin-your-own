package com.poiin.yourown;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import com.poiin.yourown.social.GenericProfilePictureRetriever;

import android.app.TabActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

public class ProfileActivity extends TabActivity {
	
	private ImageView profilePicture;
	private TextView nameTextView;
	private TextView lastNameTextView;
	private TabHost tabHost;
	private JSONObject me;
	private LinearLayout personalViewLayout;
	private LinearLayout socialViewLayout;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.profile);
		me = ((ApplicationState)getApplication()).getMe();
		this.profilePicture =
			(ImageView) findViewById(R.id.profilePicture);	
		this.nameTextView=(TextView)findViewById(R.id.profileName);
		this.lastNameTextView=(TextView)findViewById(R.id.profileLastName);
		personalViewLayout = (LinearLayout)findViewById(R.id.personalViewLayout);
		socialViewLayout = (LinearLayout)findViewById(R.id.socialViewLayout);
		tabHost = getTabHost();
		setupTabs();
	}

	private void setupTabs() {
		// add views to tab host
		tabHost.addTab(tabHost.newTabSpec("Personal").setIndicator("Personal").setContent(new TabContentFactory() {
			
			@Override
			public View createTabContent(String tag) {
				// TODO Auto-generated method stub
				return personalViewLayout;
			}
		}));
		
		tabHost.addTab(tabHost.newTabSpec("Social").setIndicator("Social").setContent(new TabContentFactory() {
			public View createTabContent(String arg0) {
				return socialViewLayout;
			}
		}));
		
		tabHost.addTab(tabHost.newTabSpec("Privacy").setIndicator("Privacy").setContent(new TabContentFactory() {
			public View createTabContent(String arg0) {
				return socialViewLayout;
			}
		}));
	}

	@Override
	public void onStart(){
		super.onStart();
		startImageRetrieval();
		nameTextView.setText(getPropertyValue("name"));
		lastNameTextView.setText(getPropertyValue("last_name"));
	}
	private void startImageRetrieval() {
		GenericProfilePictureRetriever.retrieveToImageView(this.getApplication(), profilePicture, getPropertyValue("twitter_id"),getPropertyValue("facebook_id"));
	}

	private String getPropertyValue(String property) {
		try {
			return me.getString(property);
		} catch (JSONException e) {
			Log.w("ProfileActivity", "LA propiedad "+property+" no existe");
			return "";
		}
	}
}
