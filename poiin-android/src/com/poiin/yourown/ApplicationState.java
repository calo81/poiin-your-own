package com.poiin.yourown;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Twitter;

import android.app.Activity;
import android.app.Application;

import com.facebook.android.Facebook;
import com.google.android.maps.MapView;
import com.poiin.yourown.people.Person;
import com.poiin.yourown.people.message.ServerMessageListener;

public class ApplicationState extends Application{
	private Facebook facebook;
	private JSONObject me;
	private volatile MapView mapView;
	private ServerMessageListener userMessageListener;
	private List<Person> people;
	private Person lastPoiinPerson;
	private Class<? extends Activity> foregroundActivity;
	private String currentMessenger;
	private Twitter twitter;
	private boolean applicationFullyStarted;

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
	
	public void setMe(JSONObject me) {
		this.me=me;
	}

	public JSONObject getMe() {
		return this.me;
	}
	
	public Long getMyId() {
		try {
			return this.me.getLong("id");
		} catch (JSONException e) {
			//TODO: Fix this
			e.printStackTrace();
			return null;
		}
	}

	public void setMapView(MapView mapView) {
		this.mapView=mapView;
	}

	public MapView getMapView() {
		return mapView;
	}

	public List<Person> getLastPeopleReturned() {
		return this.people;
	}

	public void setLastPeopleReturned(List<Person> people) {
		this.people=people;
	}

	public ServerMessageListener getUserMessageListener() {
		if(userMessageListener==null){
			return ServerMessageListener.getInstance(this);
		}
		return userMessageListener;
	}

	public void setLastPoiinPerson(Person person) {
		this.lastPoiinPerson=person;
	}

	public Person getLastPoiinPerson() {
		return lastPoiinPerson;
	}

	public void setForegroundActivity(Class<? extends Activity> activeActivityClass) {
		foregroundActivity=activeActivityClass;
	}

	public Class<? extends Activity> getForegroundActivity() {
		return foregroundActivity;
	}

	public String getCurrentMessenger() {
		return this.currentMessenger;
	}

	public void setCurrentMessenger(String currentMessenger) {
		this.currentMessenger = currentMessenger;
	}

	public Twitter getTwitter() {
		return twitter;
	}

	public void setTwitter(Twitter twitter) {
		this.twitter = twitter;
	}

	public boolean isApplicationFullyStarted() {
		return applicationFullyStarted;
	}

	public void setApplicationFullyStarted(boolean applicationFullyStarted) {
		this.applicationFullyStarted = applicationFullyStarted;
	}
	
}
