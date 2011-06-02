package com.poiin.yourown.poiin;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;

public class Poiin {
	
	private JSONObject user;
	private double latitude;
	private double longitude;
	public Poiin(GeoPoint lastKnownPoint, JSONObject me) {
		this.latitude=lastKnownPoint.getLatitudeE6()/1E6;
		this.longitude=lastKnownPoint.getLongitudeE6()/1E6;
		this.user = me;
	}
	public String toJsonString() {
		try {
			return "{user_id:"+user.getLong("id")+",latitude:"+latitude+",longitude:"+longitude+"}";
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
}
