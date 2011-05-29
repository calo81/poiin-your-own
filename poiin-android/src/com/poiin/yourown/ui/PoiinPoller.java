package com.poiin.yourown.ui;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.maps.MapView;
import com.poiin.yourown.people.MockPeople;
import com.poiin.yourown.people.People;

public class PoiinPoller {
	private static final String TAG = "PoiinPoller";
	private static final int TIME_BETWEN_POLLS = 30000;
	private People people = new MockPeople();
	private Drawable marker;
	private MapView mapView;
	private PeopleItemizedOverlay peopleOverlay;
	private Thread pollingThread;
	private volatile boolean keepPolling=true;

	public PoiinPoller(Drawable marker, MapView mapView) {
		this.marker = marker;
		this.mapView = mapView;
	}

	public void pollAndUpdate() {
		pollingThread = new Thread() {
			public void run() {
				pollServerContinuously();
			}

			private void pollServerContinuously() {
				while(keepPolling){
					people.retrieve();
					Log.i(TAG, "Polling server");
					handler.sendEmptyMessage(0);
					sleepBeforeNextPoll();
				}
			}

			private void sleepBeforeNextPoll() {
				try {
					Thread.sleep(TIME_BETWEN_POLLS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		pollingThread.start();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			peopleOverlay = new PeopleItemizedOverlay(people, marker);
			mapView.getOverlays().add(peopleOverlay);
		}
	};
}
