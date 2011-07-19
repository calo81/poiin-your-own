package com.poiin.yourown.social;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class UrlBasedImageRetrieverHelper {
	public static Bitmap retrieveBitmapFromUrl(String urlstring) {
		try {
			URL url = new URL(urlstring);
			URLConnection conn = url.openConnection();
			conn.connect();
			BufferedInputStream bis = new BufferedInputStream(
					conn.getInputStream());
			Bitmap bm = BitmapFactory.decodeStream(bis);
			bis.close();
			return bm;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
