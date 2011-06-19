package com.poiin.yourown.people;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.poiin.yourown.network.RestClientService;
import com.poiin.yourown.network.RestClientServiceImpl;

public class RestPeople implements People {
	public RestClientService restClientService = new RestClientServiceImpl();
	private JSONObject user;
	private JSONArray retrievedPoiins;

	public RestPeople(JSONObject user) {
		this.user = user;
	}

	public void retrieve() {
		retrievedPoiins = restClientService.getPoiins(user);
	}

	public List<Person> iterate() {
		List<Person> persons = new ArrayList<Person>();
		JSONArray array = retrievedPoiins;
		for (int i = 0; i < array.length(); i++) {
			JSONObject poiin = array.optJSONObject(i);
			Person person = createPersonObjectFromJSONPoiin(poiin);
			if (person != null) {
				persons.add(person);
			}
		}
		return persons;
	}


	private Person createPersonObjectFromJSONPoiin(JSONObject poiin) {
		try {
			Person person = new Person();
			person.setName(poiin.getJSONObject("user").getString("user_name"));
			person.setId(poiin.getJSONObject("user").getString("id"));
			person.setLatitude(poiin.getJSONObject("poiin").getDouble("latitude"));
			person.setLongitude(poiin.getJSONObject("poiin").getDouble("longitude"));
			return person;
		} catch (JSONException e) {
			Log.e("RestPeople", e.getMessage());
			return null;
		}
	}

}
