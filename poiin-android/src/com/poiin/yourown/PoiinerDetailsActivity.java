package com.poiin.yourown;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.poiin.yourown.social.GenericProfilePictureRetriever;
import com.poiin.yourown.social.facebook.FacebookProfilePictureRetriever;
import com.poiin.yourown.social.twitter.TwitterProfilePictureRetriever;
import com.poiin.yourown.storage.Data;
import com.poiin.yourown.storage.Data.LoginOption;
import com.poiin.yourown.storage.PreferencesBackedData;

public class PoiinerDetailsActivity extends Activity {

	private static final int POIIN_MESSAGE_MENU = 2;
	private TextView userIdView;
	private ImageView poiinerPicture;
	private TextView lastWallText;
	private TextView poiinTextView;
	private String poiinerId;
	private String poiinerTwitterId;
	private String poiinerFacebookId;
	private String poiinerName;
	private String poiinText;
	private TextView categoriesList;
	private Button buttonSendMessage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poiiner_details);
		userIdView = (TextView) findViewById(R.id.poiinerName);
		poiinerPicture = (ImageView) findViewById(R.id.poiinerImage);
		lastWallText = (TextView) findViewById(R.id.lastWallMessage);
		poiinTextView = (TextView) findViewById(R.id.poiinText);
		categoriesList = (TextView) findViewById(R.id.poiinerCategoriesList);
		buttonSendMessage = (Button) findViewById(R.id.buttonEnviarMensaje);
		addListeners();
	}

	private void addListeners() {
		addSendMessageListener();
	}

	private void addSendMessageListener() {
		buttonSendMessage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PoiinerDetailsActivity.this, NewMessageActivity.class);
				intent.putExtra("messageReceiver", poiinerId);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
		poiinerId = getIntent().getData().getPath().substring(1);
		poiinerName = getIntent().getData().getQueryParameter("name");
		getPoiinText();
		poiinerTwitterId = getIntent().getData().getQueryParameter("twitter_id");
		poiinerFacebookId = getIntent().getData().getQueryParameter("facebook_id");
		userIdView.setText(poiinerName);
		poiinTextView.setText(poiinText);
		categoriesList.setText(getIntent().getData().getQueryParameter("categories"));
		startImageRetrieval();
		startWallRetrieval();
	}

	private void getPoiinText() {
		if (getIntent().getData().getQueryParameter("poiinText") == null || getIntent().getData().getQueryParameter("poiinText").equals("null")) {
			poiinText = "";
		} else {
			poiinText = getIntent().getData().getQueryParameter("poiinText");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "Follow in Twitter").setIcon(android.R.drawable.ic_menu_share);
		menu.add(0, 1, 0, "Follow in Poiin").setIcon(android.R.drawable.ic_menu_share);
		menu.add(0, 2, 0, "Fotos").setIcon(android.R.drawable.ic_menu_share);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
		switch (item.getItemId()) {

		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void startWallRetrieval() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				ApplicationState applicationState = (ApplicationState) getApplication();
				try {
					String wall = applicationState.getFacebook().request(poiinerId + "/feed");
					JSONObject wallJson = new JSONObject(wall);
					JSONArray array = wallJson.getJSONArray("data");
					findLastFeedWithMessageAndAddToProfile(array);
				} catch (Exception e) {
					// TODO: No Handling again?
					e.printStackTrace();
				}
			}

			private void findLastFeedWithMessageAndAddToProfile(JSONArray array) throws JSONException {
				for (int i = 0; i < array.length(); i++) {
					JSONObject lastWallEntry = array.getJSONObject(i);
					String lastMessage = null;
					try {
						lastMessage = lastWallEntry.getString("message");
					} catch (Exception e) {
						continue;
					}
					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("wallEntry", lastMessage);
					msg.setData(bundle);
					wallReceivedHandler.sendMessage(msg);
					break;
				}
			}
		}).start();
	}

	private Handler wallReceivedHandler = new Handler() {
		public void handleMessage(Message msg) {
			lastWallText.setText(msg.getData().getString("wallEntry"));
		}
	};

	private void startImageRetrieval() {
		GenericProfilePictureRetriever.retrieveToImageView(this.getApplication(), poiinerPicture, poiinerTwitterId, poiinerFacebookId);
	}

}
