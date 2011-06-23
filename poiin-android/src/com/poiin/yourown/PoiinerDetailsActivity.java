package com.poiin.yourown;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class PoiinerDetailsActivity extends Activity {

	private static final int POIIN_MESSAGE_MENU = 2;
	private TextView userIdView;
	private ImageView poiinerPicture;
	private String imageLink = "http://graph.facebook.com/{ID}/picture";
	private String poiinerId;
	private String poiinerName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poiiner_details);
		userIdView = (TextView) findViewById(R.id.poiinerName);
		poiinerPicture = (ImageView) findViewById(R.id.poiinerImage);
	}

	@Override
	public void onStart() {
		super.onStart();
		startImageRetrieval();
		poiinerId = getIntent().getData().getPath().substring(1);
		poiinerName = getIntent().getData().getQueryParameter("name");
		userIdView.setText(poiinerName);

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

	private void startImageRetrieval() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				loadImageHandler.sendEmptyMessage(0);
			}
		}).start();
	}

	private Handler loadImageHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				URL url = new URL(imageLink.replace("{ID}", poiinerId));
				URLConnection conn = url.openConnection();
				conn.connect();
				BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
				Bitmap bm = BitmapFactory.decodeStream(bis);
				bis.close();
				poiinerPicture.setImageBitmap(bm);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	};

}
