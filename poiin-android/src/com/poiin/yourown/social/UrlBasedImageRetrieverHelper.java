package com.poiin.yourown.social;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class UrlBasedImageRetrieverHelper {
	//TODO The follwing Map is atemporal ineffiecient ALL IN MEMORY cache that must be replaced with a better, ore scalable solution
	private static Map<String, Bitmap> inneficientCache=new HashMap<String, Bitmap>();
	public static Bitmap retrieveBitmapFromUrl(String urlstring) {
		if(inneficientCache.get(urlstring)!=null){
			return inneficientCache.get(urlstring);
		}
		try {
			URL url = new URL(urlstring);
			URLConnection conn = url.openConnection();
			conn.connect();
			BufferedInputStream bis = new BufferedInputStream(
					conn.getInputStream());
			Bitmap bm = BitmapFactory.decodeStream(bis);
			bis.close();
			inneficientCache.put(urlstring, bm);
			return bm;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
