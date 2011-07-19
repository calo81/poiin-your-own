package com.poiin.yourown.social.twitter;

import oauth.signpost.OAuth;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.poiin.yourown.R;
import com.poiin.yourown.storage.Data;
import com.poiin.yourown.storage.PreferencesBackedData;

public class TwitterAuthentication extends Activity {
	public static final String CONSUMER_KEY = "xwNHpAlrYxxz99NT9WmAg";
	public static final String CONSUMER_SECRET = "CswbLutGKrGK5afPbaojLczroLMCc76W1NqlQ1ywEs";

	private static final String REQUEST_URL = "http://twitter.com/oauth/request_token";
	private static final String ACCESS_TOKEN_URL = "http://twitter.com/oauth/access_token";
	private static final String AUTH_URL = "http://twitter.com/oauth/authorize";
	static final String CALLBACK_URL = "OauthTwitter://twitt";

	static CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(
			CONSUMER_KEY, CONSUMER_SECRET);
	static CommonsHttpOAuthProvider provider = new CommonsHttpOAuthProvider(
			REQUEST_URL, ACCESS_TOKEN_URL, AUTH_URL);

	static Data dataAccess;

	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		dataAccess = new PreferencesBackedData(this);
		setContentView(R.layout.twitter_login);
		if (!twitterData()) {
			goToTwitterForAuthentication();
		} else {
			authenticateWithSavedData();
		}
	}

	private void authenticateWithSavedData() {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CALLBACK_URL
				+ "?oauth_verifier=from_saved")));
	}

	private boolean twitterData() {
		return dataAccess.getTwitterDetails() != null;
	}

	private void goToTwitterForAuthentication() {
		try {
			String authURL = provider.retrieveRequestToken(consumer,
					CALLBACK_URL);
			Log.d("OAuthTwitter", authURL);
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authURL)));
		} catch (OAuthMessageSignerException e) {
			e.printStackTrace();
		} catch (OAuthNotAuthorizedException e) {
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			e.printStackTrace();
		}
	}
}
