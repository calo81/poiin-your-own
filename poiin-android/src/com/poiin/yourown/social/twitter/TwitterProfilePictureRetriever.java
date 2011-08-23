package com.poiin.yourown.social.twitter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import twitter4j.ProfileImage;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.poiin.yourown.ApplicationState;
import com.poiin.yourown.people.Person;
import com.poiin.yourown.social.ProfilePictureRetriever;
import com.poiin.yourown.social.UrlBasedImageRetrieverHelper;

public class TwitterProfilePictureRetriever implements ProfilePictureRetriever {

	private ApplicationState appState;
	private ImageView imageView;
	private String twitterScreenName;
	private Handler loadImageHandler;

	public TwitterProfilePictureRetriever(ApplicationState appState, String twitterId) {
		this.appState = appState;
		setScreenNameToLookup(twitterId);
	}

	public TwitterProfilePictureRetriever(ApplicationState appState, ImageView imageView, String twitterId) {
		this.imageView = imageView;
		this.appState = appState;
		setScreenNameToLookup(twitterId);

	}

	public TwitterProfilePictureRetriever(ApplicationState appState) {
		this.appState = appState;
	}

	private void setScreenNameToLookup(String twitterId) {
		try {
			User twitterUser = appState.getTwitterOrGenericTwitter().lookupUsers(new long[] { Long.parseLong(twitterId) }).get(0);
			this.twitterScreenName = twitterUser.getScreenName();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Bitmap retrieveBitmap() {
		Twitter twitter = appState.getTwitterOrGenericTwitter();
		try {
			ProfileImage image = twitter.getProfileImage(twitterScreenName, ProfileImage.NORMAL);
			return UrlBasedImageRetrieverHelper.retrieveBitmapFromUrl(image.getURL());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void retrieveToImageView() {
		initHandler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				loadImageHandler.sendEmptyMessage(0);
			}
		}).start();
	}
	
	@Override
	public void fillPictureUrl(List<Person> people) {
		long[] ids = getPeopleTwitterIds(people);
		List<User> twitterUsers = getTwitterUsers(ids);
		//TODO: O(n2) Algorithm. Optimize if necessary.
		for (Person person : people) {
			for (User user : twitterUsers) {
				if(user.getId()==Long.parseLong(person.getTwitterId())){
					person.setProfilePictureUrl(user.getProfileImageURL().toString());
				}
			}
		}
	}


	private void initHandler() {
		if (loadImageHandler == null) {
			loadImageHandler = new Handler() {
				public void handleMessage(Message msg) {
					imageView.setImageBitmap(retrieveBitmap());
				}
			};
		}
	}

	
	private List<User> getTwitterUsers(long[] ids){
		ResponseList<User> twitterUsers;
		try {
			twitterUsers = appState.getTwitterOrGenericTwitter().lookupUsers(ids);
			return twitterUsers;
		} catch (TwitterException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}	
	}

	private long[] getPeopleTwitterIds(List<Person> people) {
		long[] ids = new long[people.size()];
		for (int i = 0; i < people.size(); i++) {
			if (people.get(i).getTwitterId() != null) {
				ids[i] = Long.parseLong(people.get(i).getTwitterId());
			}
		}
		return ids;
	}

}
