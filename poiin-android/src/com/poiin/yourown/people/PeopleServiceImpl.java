package com.poiin.yourown.people;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.poiin.yourown.ApplicationState;
import com.poiin.yourown.network.RestClientService;
import com.poiin.yourown.network.RestClientServiceImpl;

public class PeopleServiceImpl implements PeopleService {
	private ApplicationState applicationState;
	private RestClientService restClientService = RestClientServiceImpl.getInstance();

	public PeopleServiceImpl(ApplicationState application) {
		this.applicationState = application;
	}

	@Override
	public boolean checkUserRegisteredAndUpdateSocialIds() {
		JSONObject json = restClientService.isUserRegistered(applicationState.getMyId().toString());
		try {
			setId(json,"twitter_id");
			setId(json,"facebook_id");
			return json.getString("registered").equals("true");
		} catch (JSONException e) {
			// TODO: Does this need Handling??
			throw new RuntimeException(e);
		}
	}

	@Override
	public void updateUser() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				restClientService.updateUser(applicationState.getMe());
			}
		}).start();
	}

	@Override
	public void registerUser(final Person person, final Handler registrationHandler) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					JSONArray categories = new JSONArray(person.getSelectedCategories());
					applicationState.getMe().put("categories", categories);
					restClientService.registerUser(applicationState.getMe());
					registrationHandler.sendEmptyMessage(0);
				} catch (JSONException e) {
					// TODO: Does this need Handling??
					throw new RuntimeException(e);
				}
			}
		}).start();
	}

	@Override
	public void inviteToPoiin(Person person) {
		Log.i("PeopleServiceImpl", "inviting "+person+" to poiin");
		Bundle params = new Bundle();
		params.putString("message", "Hey, te invito a Poiin");
		postToFacebookWall(person, params);
	}

	private void postToFacebookWall(Person person, Bundle params){
		try{
			applicationState.getFacebook().request(person.getFacebookId()+"/feed", params, "POST");
		}catch(Exception e){
			Log.e("PeopleServiceImpl", "Error posting in User wall",e);
		}
	}

	@Override
	public void getUserInfo(Person person) {
		// TODO Auto-generated method stub
		
	}
	
	private void setId(JSONObject json,String idProperty) {
		try {
			this.applicationState.getMe().put(idProperty, json.getString(idProperty));
		} catch (JSONException e) {
			// TODO Nothing to do
			e.printStackTrace();
		}
	}
}
