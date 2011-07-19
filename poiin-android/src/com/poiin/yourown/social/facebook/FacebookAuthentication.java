package com.poiin.yourown.social.facebook;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.R;
import com.poiin.yourown.ApplicationState;
import com.poiin.yourown.FirstTimeProfileActivity;
import com.poiin.yourown.Main;
import com.poiin.yourown.people.PeopleService;
import com.poiin.yourown.people.PeopleServiceImpl;

public class FacebookAuthentication extends Activity {

	private Facebook facebook = new Facebook("149212958485869");
	private ProgressDialog progressDialog;
	private PeopleService peopleService;
	private ApplicationState appState;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		peopleService = new PeopleServiceImpl((ApplicationState) this.getApplication());
		setContentView(R.layout.facebook_login);
		appState = (ApplicationState) getApplication();
		facebook.authorize(this, new String[] { "email", "read_stream", "user_interests", "user_status", "manage_friendlists", "user_photos" }, new DialogListener() {

			public void onFacebookError(FacebookError e) {
				e.printStackTrace();
			}

			public void onError(DialogError e) {
				e.printStackTrace();
			}

			public void onComplete(Bundle values) {
				((ApplicationState) getApplication()).setFacebook(facebook);
				getMyFacebookProofileAndLoginToPoiin();
			}

			private void getMyFacebookProofileAndLoginToPoiin() {
				progressDialog = ProgressDialog.show(FacebookAuthentication.this, "Processing . . .", "Retrieving User Information ...", true, false);
				new Thread(new Runnable() {
					public void run() {
						setUserWithFacebookProfile();
						boolean isUserAlreadyInSystem = peopleService.isUserRegistered();
						Message message = new Message();
						Bundle bundle = new Bundle();
						bundle.putBoolean("userRegistered", isUserAlreadyInSystem);
						message.setData(bundle);
						handler.sendMessage(message);
					}

					private void setUserWithFacebookProfile() {
						appState.setMe(getMyFacebookProfile());
						try {
							appState.getMe().put("facebook_id", appState.getMe().get("id"));
						} catch (JSONException e) {
							// TODO Nothing to odo
							e.printStackTrace();
						}
					}
				}).start();

			}

			private String getMyFacebookProfile() {
				try {
					return facebook.request("me");
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

			public void onCancel() {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	private final Handler handler = new Handler() {

		@Override
		public void handleMessage(final Message msg) {
			progressDialog.dismiss();
			if (msg.getData().getBoolean("userRegistered")) {
				startActivity(new Intent(FacebookAuthentication.this.getApplicationContext(), Main.class));
			} else {
				startActivity(new Intent(FacebookAuthentication.this.getApplicationContext(), FirstTimeProfileActivity.class));
			}
			finish();
		}
	};
}