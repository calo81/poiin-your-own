package com.poiin.yourown.social.twitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;

import twitter4j.Friendship;
import twitter4j.IDs;

import android.util.Log;

import com.poiin.yourown.ApplicationState;
import com.poiin.yourown.people.Person;
import com.poiin.yourown.social.SocialFriendRetriever;

public class TwitterFriendRetriever implements SocialFriendRetriever{

	private ApplicationState appState;

	public TwitterFriendRetriever(ApplicationState appState) {
		this.appState=appState;
	}

	@Override
	public List<Person> retrieveFriends() {	
		try {
			if(appState.getMe().getString("twitter_id")==null){
				return Collections.emptyList();
			}
		} catch (JSONException e) {
			// TODO I HAVE TO DO THE FREAKING JSON WRAPPER FOR NOT CAHCING THIS ALL THE TIME!!
			return Collections.emptyList();
		}
		return doRetrieveFriends();
		
	}

	private List<Person> doRetrieveFriends() {
		try {
			IDs ids = appState.getTwitter().getFriendsIDs(Long.parseLong(appState.getMe().getString("twitter_id")),-1l);
			List<Friendship> friends =  appState.getTwitter().lookupFriendships(ids.getIDs());
			List<Person> people = new ArrayList<Person>();
			for(Friendship friend:friends){
				Person person = new Person();
				person.setName(friend.getName());
				person.setId(String.valueOf(friend.getId()));
				person.setTwitterId(String.valueOf(friend.getId()));
				people.add(person);
			}
			new TwitterProfilePictureRetriever(appState).fillPictureUrl(people);
			return people;
		} catch (Exception e) {
			Log.e("TwitterFriendRetriever", "Error retrieveing friends",e);
			return Collections.emptyList();
		}
	}
	
}
