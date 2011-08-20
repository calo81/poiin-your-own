package com.poiin.yourown.social.facebook;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.poiin.yourown.ApplicationState;
import com.poiin.yourown.people.PeopleService;
import com.poiin.yourown.people.PeopleServiceImpl;

public class FacebookAuthenticator {
	private Facebook facebook;
	private Activity activity;
	private ApplicationState appState;
	private PeopleService peopleService;

	public FacebookAuthenticator(Activity activity,Facebook facebook){
		this.facebook=facebook;
		this.activity=activity;
		appState=(ApplicationState)activity.getApplication();
		peopleService = new PeopleServiceImpl(appState);
	}
	
	public void authenticate(){

		facebook.authorize(activity, new String[] { "email", "read_stream","publish_stream", "user_interests", "user_status", "manage_friendlists", "user_photos" }, new DialogListener() {

			public void onFacebookError(FacebookError e) {
				e.printStackTrace();
			}

			public void onError(DialogError e) {
				e.printStackTrace();
			}

			public void onComplete(Bundle values) {
				appState.setFacebook(facebook);
				updateUserInfo();
			}

			private void updateUserInfo() {
				new Thread(new Runnable() {
					public void run() {
						setUserWithFacebookProfile();						
					}

					private void setUserWithFacebookProfile() {
						try {
							appState.getMe().put("facebook_id", getMyFacebookProfile().get("id"));
							peopleService.updateUser();
						} catch (JSONException e) {
							// TODO Nothing to odo
							e.printStackTrace();
						}
					}
				}).start();

			}

			private JSONObject getMyFacebookProfile() {
				try {
					return new JSONObject(facebook.request("me"));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

			public void onCancel() {
				// TODO Auto-generated method stub

			}
		});
	
	}
}
