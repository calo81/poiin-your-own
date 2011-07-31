package com.poiin.yourown;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.poiin.yourown.social.GenericProfilePictureRetriever;
import com.poiin.yourown.social.facebook.FacebookAuthenticator;

public class ProfileActivity extends TabActivity {

	private ImageView profilePicture;
	private TextView nameTextView;
	private TextView lastNameTextView;
	private TabHost tabHost;
	private JSONObject me;
	private LinearLayout personalViewLayout;
	private LinearLayout socialViewLayout;
	private ImageView facebookLogo;
	private Facebook facebook = new Facebook("149212958485869");
	private ApplicationState appState;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appState = (ApplicationState) this.getApplication();
		this.setContentView(R.layout.profile);
		me = ((ApplicationState) getApplication()).getMe();
		this.profilePicture = (ImageView) findViewById(R.id.profilePicture);
		this.nameTextView = (TextView) findViewById(R.id.profileName);
		this.lastNameTextView = (TextView) findViewById(R.id.profileLastName);
		personalViewLayout = (LinearLayout) findViewById(R.id.personalViewLayout);
		socialViewLayout = (LinearLayout) findViewById(R.id.socialViewLayout);
		facebookLogo = (ImageView) findViewById(R.id.facebookLogoProfileButton);
		tabHost = getTabHost();
		setupTabs();
		setupInteractions();
	}

	private void setupInteractions() {
		setupFacebookLogin();
	}

	private void setupFacebookLogin() {
		facebookLogo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				authenticateToFacebook();
			}
		});

	}

	private void authenticateToFacebook() {
		new FacebookAuthenticator(this, facebook).authenticate();
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
	public void onStart() {
		super.onStart();
		startImageRetrieval();
		nameTextView.setText(getPropertyValue("name"));
		lastNameTextView.setText(getPropertyValue("last_name"));
	}

	/**
	 * Implemented for facebook requirements
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	private void startImageRetrieval() {
		GenericProfilePictureRetriever.retrieveToImageView(this.getApplication(), profilePicture, getPropertyValue("twitter_id"),
				getPropertyValue("facebook_id"));
	}

	private String getPropertyValue(String property) {
		try {
			return me.getString(property);
		} catch (JSONException e) {
			Log.w("ProfileActivity", "LA propiedad " + property + " no existe");
			return "";
		}
	}
}
