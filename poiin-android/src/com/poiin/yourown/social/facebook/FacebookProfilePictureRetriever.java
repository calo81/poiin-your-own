package com.poiin.yourown.social.facebook;

import java.util.List;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.poiin.yourown.people.Person;
import com.poiin.yourown.social.ProfilePictureRetriever;
import com.poiin.yourown.social.UrlBasedImageRetrieverHelper;

public class FacebookProfilePictureRetriever implements ProfilePictureRetriever {
	private static String imageLink = "http://graph.facebook.com/{ID}/picture";
	private ImageView imageView;
	private String facebookUserId;
	private Handler loadImageHandler;

	public FacebookProfilePictureRetriever(ImageView imageView, String facebookUserId) {
		this.imageView = imageView;
		this.facebookUserId = facebookUserId;
	}

	public FacebookProfilePictureRetriever(String facebookUserId) {
		this.facebookUserId = facebookUserId;
	}

	public FacebookProfilePictureRetriever() {
	}

	public void retrieveToImageView() {
		loadImageHandler = new Handler() {
			public void handleMessage(Message msg) {
				imageView.setImageBitmap(retrieveFacebookImage());
			}
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				loadImageHandler.sendEmptyMessage(0);
			}
		}).start();
	}

	public Bitmap retrieveBitmap() {
		return retrieveFacebookImage();
	}

	public void fillPictureUrl(List<Person> people) {
		for (Person person : people) {
			if (person.getFacebookId() != null) {
				person.setProfilePictureUrl(imageLink.replace("{ID}", person.getFacebookId()));
			}
		}
	}

	private Bitmap retrieveFacebookImage() {
		return UrlBasedImageRetrieverHelper.retrieveBitmapFromUrl(imageLink.replace("{ID}", facebookUserId));
	}
}
