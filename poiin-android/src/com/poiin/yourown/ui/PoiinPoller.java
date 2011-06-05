package com.poiin.yourown.ui;

import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.poiin.yourown.ApplicationState;
import com.poiin.yourown.R;
import com.poiin.yourown.people.People;
import com.poiin.yourown.people.RestPeople;

public class PoiinPoller {
	private static final int TIME_BETWEN_POLLS = 30000;
	private People people = null;
	private Drawable marker;
	private PeopleItemizedOverlay peopleOverlay;
	private Thread pollingThread;
	private volatile boolean keepPolling = true;
	private static PoiinPoller instance;
	private ContextWrapper context;

	public static PoiinPoller getInstance(ContextWrapper context){
		if(instance==null){
			instance = new PoiinPoller(context);
		}
		return instance;
	}
	private PoiinPoller(ContextWrapper context) {
		this.marker = context.getResources().getDrawable(R.drawable.buoy);
		this.marker.setBounds(0, 0, this.marker.getIntrinsicWidth(), this.marker.getIntrinsicHeight());
		this.context=context;
		people=new RestPeople(((ApplicationState)context.getApplicationContext()).getMe());
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
					ifPeopleChangeNotifyNewPoiiners(notificationHandler);
					sleepBeforeNextPoll();
				}
			}

			private void ifPeopleChangeNotifyNewPoiiners(final Handler notificationHandler) {
				//TODO: Temporal notification condition, checking on size of people returned
				if (lastPeopleRetrieved()==null || people.iterate().size() != lastPeopleRetrieved()) {
					handler.sendEmptyMessage(0);
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
			peopleOverlay = new PeopleItemizedOverlay(people, marker,context);
			((ApplicationState)context.getApplicationContext()).getMapView().getOverlays().add(peopleOverlay);
		}
	};

	public void setKeepPolling(boolean keepPolling) {
		this.keepPolling = keepPolling;
	}
	
}
