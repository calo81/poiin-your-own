package com.poiin.yourown.people;

import java.util.List;

public class Person {
	private double longitude;
	private double latitude;
	private String name;
	private String id;
	private String poiinText;
	private String twitterId;
	private String facebookId;
	private List<String> selectedCategories;
	
	
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public List<String> getSelectedCategories() {
		return selectedCategories;
	}
	public void setSelectedCategories(List<String> selectedCategories) {
		this.selectedCategories = selectedCategories;
	}
	public String getPoiinText() {
		return poiinText;
	}
	public void setPoiinText(String poiinText) {
		this.poiinText = poiinText;
	}
	public String getTwitterId() {
		return twitterId;
	}
	public void setTwitterId(String twitterId) {
		this.twitterId = twitterId;
	}
	public String getFacebookId() {
		return facebookId;
	}
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}	
}
