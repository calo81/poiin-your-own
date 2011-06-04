package com.poiin.yourown;

import com.google.android.maps.MapView;
import com.poiin.yourown.social.facebook.FacebookAuthentication;
import com.poiin.yourown.ui.PoiinPoller;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class PoiinBackgroundService extends Service {

	private PoiinPoller poiinPoller;
	private MapView mapView;
	private NotificationManager mNotificationManager;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		String ns = Context.NOTIFICATION_SERVICE;
		mNotificationManager = (NotificationManager) getSystemService(ns);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    super.onStartCommand(intent, flags, startId);
		return START_REDELIVER_INTENT ;
	}

	@Override
	public void onStart(Intent intent, int startId) {
			mapView = ((ApplicationState) getApplication()).getMapView();
			poiinPoller = ((ApplicationState) getApplication()).getPoiinPoller(mapView, this);
			poiinPoller.pollAndUpdate(notificatorNewPoiiners);
	}

	private final Handler notificatorNewPoiiners = new Handler() {

		@Override
		public void handleMessage(final Message msg) {
			int icon = R.drawable.icon;
			CharSequence tickerText = "New Poiin!!";
			long when = System.currentTimeMillis();
			Notification notification = new Notification(icon, tickerText, when);
			Context context = getApplicationContext();
			CharSequence contentTitle = "New Poiin!!";
			CharSequence contentText = "Hey there is somebody new around.. check out!";
			Intent notificationIntent = new Intent(PoiinBackgroundService.this, Main.class);
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
			notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
			mNotificationManager.notify(1, notification);
			
		}
	};
}
