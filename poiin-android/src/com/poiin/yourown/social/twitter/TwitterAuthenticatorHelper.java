package com.poiin.yourown.social.twitter;

import android.content.Context;

import com.poiin.yourown.ApplicationState;
import com.poiin.yourown.storage.Data;
import com.poiin.yourown.storage.PreferencesBackedData;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterAuthenticatorHelper {
	private ApplicationState appState;
	private Data data;
	
	public TwitterAuthenticatorHelper(Context context,ApplicationState appState){
		this.appState=appState;
		data = new PreferencesBackedData(context);
	}
	
	public void configureApplicationTwitter() {
		String[] twitterDetails = data.getTwitterDetails();
		String accessKey = twitterDetails[0];
		String accessSecret = twitterDetails[1];
		Configuration conf = new ConfigurationBuilder().setOAuthConsumerKey(TwitterAuthentication.CONSUMER_KEY)
				.setOAuthConsumerSecret(TwitterAuthentication.CONSUMER_SECRET).build();
		Twitter twitter = new TwitterFactory(conf).getInstance(new AccessToken(accessKey, accessSecret));
		appState.setTwitter(twitter);
	}
}
