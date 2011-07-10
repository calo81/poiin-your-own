package com.poiin.yourown;

import com.poiin.yourown.social.facebook.FacebookAuthentication;
import com.poiin.yourown.social.twitter.TwitterAuthentication;
import com.poiin.yourown.storage.Data;
import com.poiin.yourown.storage.PreferencesBackedData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginSelectionActivity extends Activity {

	private ImageView facebookView;
	private ImageView twitterView;
	private ApplicationState appState;
	private Data data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_selection);
		facebookView = (ImageView) findViewById(R.id.facebookLogin);
		twitterView = (ImageView) findViewById(R.id.twitterLogin);
		data = new PreferencesBackedData(this);
		addListenerToFacebookLogin();
		addListenerToTwitterLogin();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (data.getLoginOption() == Data.LoginOption.FACEBOOK) {
			startActivity(new Intent(LoginSelectionActivity.this, FacebookAuthentication.class));
			finish();
		} else if (data.getLoginOption() == Data.LoginOption.TWITTER) {
			startActivity(new Intent(LoginSelectionActivity.this, TwitterAuthentication.class));
			finish();
		}
	}

	private void addListenerToFacebookLogin() {
		facebookView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				data.setLoginOption(Data.LoginOption.FACEBOOK);
				startActivity(new Intent(LoginSelectionActivity.this, FacebookAuthentication.class));
				finish();
			}
		});
	}

	private void addListenerToTwitterLogin() {
		twitterView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				data.setLoginOption(Data.LoginOption.TWITTER);
				startActivity(new Intent(LoginSelectionActivity.this, TwitterAuthentication.class));
				finish();
			}
		});
	}

}
