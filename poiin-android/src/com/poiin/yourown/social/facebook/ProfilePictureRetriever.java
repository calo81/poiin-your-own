package com.poiin.yourown.social.facebook;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class ProfilePictureRetriever {
	private static String imageLink = "http://graph.facebook.com/{ID}/picture";
	private ImageView imageView;
	private String facebookUserId;

	public ProfilePictureRetriever(ImageView imageView, String facebookUserId) {
		this.imageView = imageView;
		this.facebookUserId = facebookUserId;
	}

	public ProfilePictureRetriever(String facebookUserId) {
		this.facebookUserId = facebookUserId;
	}

	public void retrieve() {
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
		try {
			URL url = new URL(imageLink.replace("{ID}", facebookUserId));
			URLConnection conn = url.openConnection();
			conn.connect();
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
			Bitmap bm = BitmapFactory.decodeStream(bis);
			bis.close();
			return bm;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
