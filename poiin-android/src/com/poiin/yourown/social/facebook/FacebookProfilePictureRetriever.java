package com.poiin.yourown.social.facebook;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.poiin.yourown.social.ProfilePictureRetriever;
import com.poiin.yourown.social.UrlBasedImageRetrieverHelper;

public class FacebookProfilePictureRetriever implements ProfilePictureRetriever {
	private static String imageLink = "http://graph.facebook.com/{ID}/picture";
	private ImageView imageView;
	private String facebookUserId;

	public FacebookProfilePictureRetriever(ImageView imageView,
			String facebookUserId) {
		this.imageView = imageView;
		this.facebookUserId = facebookUserId;
	}

	public FacebookProfilePictureRetriever(String facebookUserId) {
		this.facebookUserId = facebookUserId;
	}

	public void retrieveToImageView() {
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

	private Handler loadImageHandler = new Handler() {
		public void handleMessage(Message msg) {
			imageView.setImageBitmap(retrieveFacebookImage());
		}
	};

	private Bitmap retrieveFacebookImage() {
		return UrlBasedImageRetrieverHelper.retrieveBitmapFromUrl(imageLink
				.replace("{ID}", facebookUserId));
	}
}
