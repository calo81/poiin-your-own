package com.poiin.yourown.social.twitter;

import org.json.JSONObject;

import com.poiin.yourown.ApplicationState;
import com.poiin.yourown.FirstTimeProfileActivity;
import com.poiin.yourown.Main;
import com.poiin.yourown.people.PeopleService;
import com.poiin.yourown.people.PeopleServiceImpl;
import com.poiin.yourown.social.facebook.FacebookAuthentication;
import com.poiin.yourown.storage.Data;
import com.poiin.yourown.storage.PreferencesBackedData;

import oauth.signpost.OAuth;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class TwitterAuthenticated extends Activity {

	private static String ACCESS_KEY = null;
	private static String ACCESS_SECRET = null;
	private ApplicationState appState;
	private ProgressDialog progressDialog;
	private PeopleService peopleService;
	private Data data;

	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		appState = (ApplicationState) getApplication();
		peopleService = new PeopleServiceImpl(appState);
		data = new PreferencesBackedData(this);
	}

	@Override
	public void onStart() {
		super.onStart();
		Uri uri = this.getIntent().getData();
		if (uri != null
				&& uri.toString()
						.startsWith(TwitterAuthentication.CALLBACK_URL)) {
			String verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);
			if (verifier.equals("from_saved")) {
				configureFromSaved();
			} else {
				doKeyAndSecretRetrieval(verifier);
			}
		}
	}

	private void configureFromSaved() {
		String[] twitterDetails = data.getTwitterDetails();
		ACCESS_KEY = twitterDetails[0];
		ACCESS_SECRET = twitterDetails[1];
		configureApplicationTwitter();
	}

	private void doKeyAndSecretRetrieval(String verifier) {
		try {
			TwitterAuthentication.provider.retrieveAccessToken(
					TwitterAuthentication.consumer, verifier);
			ACCESS_KEY = TwitterAuthentication.consumer.getToken();
			ACCESS_SECRET = TwitterAuthentication.consumer.getTokenSecret();
			TwitterAuthentication.dataAccess.storeTwitterDetails(ACCESS_KEY,
					ACCESS_SECRET);
			configureApplicationTwitter();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void configureApplicationTwitter() {
		Configuration conf = new ConfigurationBuilder()
				.setOAuthConsumerKey(TwitterAuthentication.CONSUMER_KEY)
				.setOAuthConsumerSecret(TwitterAuthentication.CONSUMER_SECRET)
				.build();
		Twitter twitter = new TwitterFactory(conf).getInstance(new AccessToken(
				ACCESS_KEY, ACCESS_SECRET));
		appState.setTwitter(twitter);
		loginToPoiin(twitter);
	}

	private JSONObject createPersonFromTwitter(Twitter twitter) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", twitter.getId());
			jsonObject.put("name", twitter.getScreenName());
			jsonObject.put("twitter_id", twitter.getId());
			return jsonObject;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void loginToPoiin(final Twitter twitter) {
		progressDialog = ProgressDialog.show(TwitterAuthenticated.this,
				"Processing . . .", "Retrieving User Information ...", true,
				false);
		new Thread(new Runnable() {
			public void run() {
				JSONObject jsonObject = createPersonFromTwitter(twitter);
				((ApplicationState) getApplication()).setMe(jsonObject);
				boolean isUserAlreadyInSystem = peopleService
						.isUserRegistered();
				Message message = new Message();
				Bundle bundle = new Bundle();
				bundle.putBoolean("userRegistered", isUserAlreadyInSystem);
				message.setData(bundle);
				handler.sendMessage(message);
			}
		}).start();

	}

	private final Handler handler = new Handler() {

		@Override
		public void handleMessage(final Message msg) {
			progressDialog.dismiss();
			if (msg.getData().getBoolean("userRegistered")) {
				startActivity(new Intent(
						TwitterAuthenticated.this.getApplicationContext(),
						Main.class));
			} else {
				startActivity(new Intent(
						TwitterAuthenticated.this.getApplicationContext(),
						FirstTimeProfileActivity.class));
			}
			finish();
		}
	};
}
