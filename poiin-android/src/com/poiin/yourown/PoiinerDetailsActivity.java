package com.poiin.yourown;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class PoiinerDetailsActivity extends Activity {
	
	private TextView userIdView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poiiner_details);
		userIdView = (TextView)findViewById(R.id.poiinerName);
	}

	@Override
	public void onStart() {
		super.onStart();
		String userId = getIntent().getData().getPath();
		userIdView.setText(userId);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "SMS").setIcon(android.R.drawable.ic_menu_send);
		menu.add(0, 1, 0, "Write in Wall").setIcon(android.R.drawable.ic_menu_share);
		menu.add(0, 2, 0, "Poiin Message").setIcon(android.R.drawable.ic_menu_more);
		return true;
	}
}
