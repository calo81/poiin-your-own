package com.poiin.yourown.people;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.poiin.yourown.network.RestClientService;
import com.poiin.yourown.network.RestClientServiceImpl;

public class RestPeople implements People {
	public RestClientService restClientService = new RestClientServiceImpl();
	private JSONObject user;
	private JSONObject retrievedPoiins;

	public RestPeople(JSONObject user) {
		this.user = user;
	}

	public void retrieve() {
		retrievedPoiins = restClientService.getPoiins(user);
	}

	public List<Person> iterate() {
		List<Person> persons = new ArrayList<Person>();
		JSONArray array = getJsonArray();
		for (int i = 0; i < array.length(); i++) {
			JSONObject poiin = array.optJSONObject(i);
			persons.add(createPersonObjectFromJSONPoiin(poiin));
		}
		return persons;
	}

	private JSONArray getJsonArray() {
		try {
			return retrievedPoiins.getJSONArray("poiins");
		} catch (JSONException e) {
			throw new RuntimeException("Tired of caching this bloody exception .." + e);
		}
	}

	private Person createPersonObjectFromJSONPoiin(JSONObject poiin) {
		try {
			Person person = new Person();
			person.setName(poiin.getString("id"));
			person.setId(poiin.getString("id"));
			person.setLatitude(poiin.getDouble("latitude"));
			person.setLongitude(poiin.getDouble("longitude"));
			return person;
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

}
