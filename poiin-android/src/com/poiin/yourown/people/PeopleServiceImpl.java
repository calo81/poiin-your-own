package com.poiin.yourown.people;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;

import com.poiin.yourown.ApplicationState;
import com.poiin.yourown.network.RestClientService;
import com.poiin.yourown.network.RestClientServiceImpl;

public class PeopleServiceImpl implements PeopleService {
	private ApplicationState applicationState;
	private RestClientService restClientService = new RestClientServiceImpl();
	
	public PeopleServiceImpl(ApplicationState application) {
		this.applicationState=application;
	}
	@Override
	public boolean isUserRegistered() {
		JSONObject json = restClientService.isUserRegistered(applicationState.getMyId().toString());
		try {
			return json.getString("registered").equals("true");
		} catch (JSONException e) {
			//TODO: Does this need Handling??
			throw new RuntimeException(e);
		}
	}
	@Override
	public void registerUser(Handler registrationHandler) {
		// TODO Auto-generated method stub
		
	}
}
