package com.poiin.yourown.poiin;

import com.google.android.maps.GeoPoint;

public class Poiin {
	
	private String user;
	private double latitude;
	private double longitude;
	public Poiin(GeoPoint lastKnownPoint, String me) {
		this.latitude=lastKnownPoint.getLatitudeE6()/1E6;
		this.longitude=lastKnownPoint.getLongitudeE6()/1E6;
		this.user = me;
	}
	public String toJsonString() {
		return "{user_id:"+user+",latitude:"+latitude+",longitude:"+longitude+"}";
	}
}
