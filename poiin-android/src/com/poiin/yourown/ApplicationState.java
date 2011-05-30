package com.poiin.yourown;

import com.facebook.android.Facebook;
import com.google.android.maps.MapView;

import android.app.Application;

public class ApplicationState extends Application{
	private Facebook facebook;
	private String me;
	private MapView mapView;

	public Facebook getFacebook() {
		return facebook;
	}

	public void setFacebook(Facebook facebook) {
		this.facebook = facebook;
	}

	public void setMe(String me) {
		this.me=me;
	}

	public String getMe() {
		return this.me;
	}

	public void setMapView(MapView mapView) {
		this.mapView=mapView;
	}

	public MapView getMapView() {
		return mapView;
	}
	
}
