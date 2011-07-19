package com.poiin.yourown.social.twitter;

import twitter4j.ProfileImage;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.poiin.yourown.ApplicationState;
import com.poiin.yourown.social.ProfilePictureRetriever;
import com.poiin.yourown.social.UrlBasedImageRetrieverHelper;

public class TwitterProfilePictureRetriever implements ProfilePictureRetriever {

	private ApplicationState appState;
	private ImageView imageView;
	private String twitterScreenName;

	public TwitterProfilePictureRetriever(ApplicationState appState, String twitterId) {
		this.appState = appState;
		setScreenNameToLookup(twitterId);
	}

	public TwitterProfilePictureRetriever(ApplicationState appState, ImageView imageView, String twitterId) {
		this.imageView = imageView;
		this.appState = appState;
		setScreenNameToLookup(twitterId);

	}

	private void setScreenNameToLookup(String twitterId) {
		try {
			User twitterUser = appState.getGenericTwitter().lookupUsers(new long[] { Long.parseLong(twitterId) }).get(0);
			this.twitterScreenName = twitterUser.getScreenName();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Bitmap retrieveBitmap() {
		Twitter twitter = appState.getGenericTwitter();
		try {
			ProfileImage image = twitter.getProfileImage(twitterScreenName, ProfileImage.MINI);
			return UrlBasedImageRetrieverHelper.retrieveBitmapFromUrl(image.getURL());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void retrieveToImageView() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				loadImageHandler.sendEmptyMessage(0);
			}
		}).start();
	}

	private Handler loadImageHandler = new Handler() {
		public void handleMessage(Message msg) {
			imageView.setImageBitmap(retrieveBitmap());
		}
	};

}
