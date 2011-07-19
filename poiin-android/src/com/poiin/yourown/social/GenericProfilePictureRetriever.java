package com.poiin.yourown.social;

import com.poiin.yourown.ApplicationState;
import com.poiin.yourown.social.facebook.FacebookProfilePictureRetriever;
import com.poiin.yourown.social.twitter.TwitterProfilePictureRetriever;
import com.poiin.yourown.storage.Data;
import com.poiin.yourown.storage.Data.LoginOption;
import com.poiin.yourown.storage.PreferencesBackedData;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class GenericProfilePictureRetriever {

	public static void retrieveToImageView(Context context,
			ImageView imageView, String twitterId, String facebookId) {
		if (isValueString(facebookId)) {
			new FacebookProfilePictureRetriever(imageView, facebookId)
					.retrieveToImageView();
		} else if (isValueString(twitterId)) {
			new TwitterProfilePictureRetriever((ApplicationState) context,
					imageView, twitterId).retrieveToImageView();
		}
	}

	public static Bitmap retrieveBitmap(Context context, String twitterId,
			String facebookId) {
		if (isValueString(facebookId)) {
			return new FacebookProfilePictureRetriever(facebookId)
					.retrieveBitmap();
		} else if (isValueString(twitterId)) {
			return new TwitterProfilePictureRetriever(
					(ApplicationState) context, twitterId).retrieveBitmap();
		} else {
			return null;
		}
	}

	private static boolean isValueString(String string) {
		return string != null && !string.equals("") && !string.equals("null");
	}
}
