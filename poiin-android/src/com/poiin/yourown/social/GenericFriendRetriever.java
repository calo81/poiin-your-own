package com.poiin.yourown.social;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.poiin.yourown.ApplicationState;
import com.poiin.yourown.people.Person;
import com.poiin.yourown.social.facebook.FacebookFriendRetriever;
import com.poiin.yourown.social.twitter.TwitterFriendRetriever;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class GenericFriendRetriever implements FriendRetriever {

	private SocialFriendRetriever twitterFriendRetriever;
	private SocialFriendRetriever facebookFriendRetriever;
	public GenericFriendRetriever(ApplicationState appState){
		twitterFriendRetriever=new TwitterFriendRetriever(appState);
		facebookFriendRetriever=new FacebookFriendRetriever(appState);
	}
	@Override
	public void retrieveFriends(Handler handler) {
		List<Person> twitterFriends = twitterFriendRetriever.retrieveFriends();
		List<Person> facebookFriends = facebookFriendRetriever.retrieveFriends();
		List<Person> allFriends = new ArrayList<Person>(facebookFriends);
		allFriends.addAll(twitterFriends);
		Bundle bundle = new Bundle();
		bundle.putSerializable("friends", (Serializable) allFriends);
		Message msg = new Message();
		msg.setData(bundle);
		handler.sendMessage(msg);
		
	}

}
