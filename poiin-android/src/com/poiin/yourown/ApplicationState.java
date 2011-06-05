package com.poiin.yourown;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.Facebook;
import com.google.android.maps.MapView;
import com.poiin.yourown.people.MockPeople;
import com.poiin.yourown.people.People;
import com.poiin.yourown.people.Person;
import com.poiin.yourown.ui.PoiinPoller;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;

public class ApplicationState extends Application{
	private Facebook facebook;
	private JSONObject me;
	private volatile MapView mapView;
	private PoiinPoller poiinPoller;
	private List<Person> people;

	public Facebook getFacebook() {
		return facebook;
	}

	public void setFacebook(Facebook facebook) {
		this.facebook = facebook;
	}

	public void setMe(String me) {
		try {
			this.me=new JSONObject(me);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public JSONObject getMe() {
		return this.me;
	}

	public void setMapView(MapView mapView) {
		this.mapView=mapView;
	}

	public MapView getMapView() {
		return mapView;
	}

	public PoiinPoller getPoiinPoller(ContextWrapper context) {
		if(poiinPoller==null){
			return PoiinPoller.getInstance(context);
		}
		return poiinPoller;
	}

	public List<Person> getLastPeopleReturned() {
		return this.people;
	}

	public void setLastPeopleReturned(List<Person> people) {
		this.people=people;
	}
	
}
