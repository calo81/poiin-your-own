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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.poiin.yourown.social.facebook.ProfilePictureRetriever;

public class PoiinerDetailsActivity extends Activity {

	private static final int POIIN_MESSAGE_MENU = 2;
	private TextView userIdView;
	private ImageView poiinerPicture;
	private TextView lastWallText;
	private TextView poiinTextView;
	private String poiinerId;
	private String poiinerName;
	private String poiinText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poiiner_details);
		userIdView = (TextView) findViewById(R.id.poiinerName);
		poiinerPicture = (ImageView) findViewById(R.id.poiinerImage);
		lastWallText = (TextView) findViewById(R.id.lastWallMessage);
		poiinTextView = (TextView)findViewById(R.id.poiinText);
	}

	@Override
	public void onStart() {
		super.onStart();
		poiinerId = getIntent().getData().getPath().substring(1);
		poiinerName = getIntent().getData().getQueryParameter("name");
		poiinText = getIntent().getData().getQueryParameter("poiinText");
		userIdView.setText(poiinerName);
		poiinTextView.setText(poiinText);
		startImageRetrieval();
		startWallRetrieval();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "SMS").setIcon(android.R.drawable.ic_menu_send);
		menu.add(0, 1, 0, "Write in Wall").setIcon(android.R.drawable.ic_menu_share);
		menu.add(0, POIIN_MESSAGE_MENU, 0, "Poiin Message").setIcon(android.R.drawable.ic_menu_more);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
		switch (item.getItemId()) {
		case POIIN_MESSAGE_MENU:
			Intent intent = new Intent(this, NewMessageActivity.class);
			intent.putExtra("messageReceiver", poiinerId);
			startActivity(intent);
			break;
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
					try{
						lastMessage =  lastWallEntry.getString("message");
					}catch(Exception e){
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
		new ProfilePictureRetriever(poiinerPicture, poiinerId).retrieve();
	}

}
