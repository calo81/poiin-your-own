package com.poiin.yourown;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class StartActivity extends Activity {

	private static final String CLASSTAG = StartActivity.class.getSimpleName();
	private ApplicationState appState;

	private final Handler handler = new Handler() {

		@Override
		public void handleMessage(final Message msg) {
			startActivity(new Intent(StartActivity.this,
					LoginSelectionActivity.class));
			finish();
		}
	};

	@Override
	public void onCreate(final Bundle icicle) {
		super.onCreate(icicle);
		appState = (ApplicationState) getApplication();
		Log.v(Constants.LOGTAG, " " + StartActivity.CLASSTAG + " onCreate");

		this.setContentView(R.layout.start_activity);
	}

	@Override
	public void onStart() {
		super.onStart();
		if (appState.isApplicationFullyStarted()) {
			startActivity(new Intent(StartActivity.this, Main.class));
		} else {
			startApplicationFresh();
		}
	}

	private void startApplicationFresh() {
		new Thread() {
			@Override
			public void run() {
				handler.sendMessageDelayed(handler.obtainMessage(), 3000);
			};
		}.start();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

}
