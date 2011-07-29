package com.poiin.yourown.social.facebook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.poiin.yourown.ApplicationState;
import com.poiin.yourown.people.Person;
import com.poiin.yourown.social.SocialFriendRetriever;

public class FacebookFriendRetriever implements SocialFriendRetriever{

	private ApplicationState appState;

	public FacebookFriendRetriever(ApplicationState appState) {
		this.appState=appState;
	}
	
	@Override
	public List<Person> retrieveFriends() {
		if(appState.getFacebook()==null){
			return Collections.emptyList();
		}
		return doRetriveFriends();
	}

	private List<Person> doRetriveFriends() {
		try{
			String friendsFacebook = appState.getFacebook().request("me/friends");
			JSONObject data = new JSONObject(friendsFacebook);
			JSONArray friendArray = data.getJSONArray("data");
			List<Person> friends = new ArrayList<Person>();
			for(int i=0;i<friendArray.length();i++){
				Person person = new Person();
				person.setName(friendArray.getJSONObject(i).getString("name"));
				person.setId(friendArray.getJSONObject(i).getString("id"));
				friends.add(person);
			}
		return friends;
		}catch(Exception e){
			Log.e("FacebookFriendRetriever", "Error retrieveing friends",e);
			return Collections.emptyList();
		}
	}

}
