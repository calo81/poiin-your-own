package com.poiin.yourown.poiin;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.poiin.yourown.model.JsonStringSupport;

public class Poiin implements JsonStringSupport{
	
	private JSONObject user;
	private double latitude;
	private double longitude;
	private String text;
	public Poiin(GeoPoint lastKnownPoint, JSONObject me) {
		this.latitude=lastKnownPoint.getLatitudeE6()/1E6;
		this.longitude=lastKnownPoint.getLongitudeE6()/1E6;
		this.user = me;
	}
	
	
	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	@Override
	public String toJsonString() {
		try {
			return "{user_id:"+user.getLong("id")+",user_twitter_id:"+getJsonProperty("twitter_id")+",user_facebook_id:"+getJsonProperty("facebook_id")+",latitude:"+latitude+",longitude:"+longitude+",user_name:"+user.getString("name")+",text:"+text+"}";
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}


	private String getJsonProperty(String propertyName){
		try {
			return String.valueOf(user.getLong(propertyName));
		} catch (JSONException e) {
			return "";
		}
	}
}
