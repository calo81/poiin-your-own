package com.poiin.yourown;

import android.app.Activity;
import android.os.Bundle;
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
}
