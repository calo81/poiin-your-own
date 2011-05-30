package com.poiin.yourown.ui;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.maps.MapView;
import com.poiin.yourown.ApplicationState;
import com.poiin.yourown.R;
import com.poiin.yourown.people.MockPeople;
import com.poiin.yourown.people.People;

public class PoiinPoller {
	private static final int TIME_BETWEN_POLLS = 30000;
	private People people = new MockPeople();
	private Drawable marker;
	private MapView mapView;
	private PeopleItemizedOverlay peopleOverlay;
	private Thread pollingThread;
	private volatile boolean keepPolling = true;
	private static PoiinPoller instance;
	private ContextWrapper context;

	public static PoiinPoller getInstance(MapView mapView, ContextWrapper context){
		if(instance==null){
			instance = new PoiinPoller(mapView,context);
		}else if(mapView!=instance.mapView){
			instance.setKeepPolling(false);
			instance = new PoiinPoller(mapView,context);
		}
		return instance;
	}
	private PoiinPoller(MapView mapView, ContextWrapper context) {
		this.mapView = mapView;
		this.marker = context.getResources().getDrawable(R.drawable.buoy);
		this.marker.setBounds(0, 0, this.marker.getIntrinsicWidth(), this.marker.getIntrinsicHeight());
		this.context=context;
	}

	public void pollAndUpdate(final Handler notificationHandler) {
		pollingThread = new Thread() {
			public void run() {
				pollServerContinuously();
			}

			private void pollServerContinuously() {
				while (keepPolling) {
					people.retrieve();
					Log.i("PoiinPoller", "Polling server");
					handler.sendEmptyMessage(0);
					ifPeopleChangeNotifyNewPoiiners(notificationHandler);
					sleepBeforeNextPoll();
				}
			}

			private void ifPeopleChangeNotifyNewPoiiners(final Handler notificationHandler) {
				//TODO: Temporal notification condition, checking on size of people returned
				if (lastPeopleRetrieved()==null || people.iterate().size() != lastPeopleRetrieved()) {
					notificationHandler.sendEmptyMessage(0);
					((ApplicationState)context.getApplicationContext()).setLastPeopleReturned(people.iterate());
				}
			}

			private Integer lastPeopleRetrieved() {
				if(((ApplicationState)context.getApplicationContext()).getLastPeopleReturned()==null){
					return null;
				}
				return ((ApplicationState)context.getApplicationContext()).getLastPeopleReturned().size();
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

	public void setKeepPolling(boolean keepPolling) {
		this.keepPolling = keepPolling;
	}
	
}
