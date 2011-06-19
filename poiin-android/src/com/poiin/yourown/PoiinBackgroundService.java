package com.poiin.yourown;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.poiin.yourown.people.message.UserMessageListener;
import com.poiin.yourown.ui.PoiinPoller;

public class PoiinBackgroundService extends Service {

	private PoiinPoller poiinPoller;
	private NotificationManager mNotificationManager;
	private UserMessageListener messageListener;

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
			startPoiinPoller();
			startMessageListener();
	}

	private void startMessageListener() {
		messageListener = ((ApplicationState) getApplication()).getUserMessageListener(this);
		messageListener.start(messageReceivedHandler);
	}

	private void startPoiinPoller() {
		poiinPoller = ((ApplicationState) getApplication()).getPoiinPoller(this);
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
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
			notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
			mNotificationManager.notify(1, notification);
			
		}
	};
	
	private final Handler messageReceivedHandler = new Handler() {

		@Override
		public void handleMessage(final Message msg) {}
	};
}
