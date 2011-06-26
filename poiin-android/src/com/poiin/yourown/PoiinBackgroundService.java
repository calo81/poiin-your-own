package com.poiin.yourown;

import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.poiin.yourown.people.Person;
import com.poiin.yourown.people.message.ServerMessageListener;
import com.poiin.yourown.people.message.UserMessage;
import com.poiin.yourown.ui.PeopleItemizedOverlay;

public class PoiinBackgroundService extends Service {

	private NotificationManager mNotificationManager;
	private ServerMessageListener messageListener;

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
		return START_REDELIVER_INTENT;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		startMessageListener();
	}

	private void startMessageListener() {
		messageListener = ((ApplicationState) getApplication()).getUserMessageListener();
		messageListener.start(decisionHandler);
	}

	private final Handler decisionHandler = new Handler(){
		@Override
		public void handleMessage(final Message msg) {
			Message message = new Message();
			message.copyFrom(msg);
			if(msg.getData().getString("type").equals("POIIN")){
				drawPoiins(msg);
				notificatorNewPoiiners.sendMessage(message);
			}else if(msg.getData().getString("type").equals("USER_MESSAGE")){
				messageReceivedHandler.sendMessage(message);	
			}
		}

		private void drawPoiins(Message msg) {
			Drawable marker = PoiinBackgroundService.this.getApplicationContext().getResources().getDrawable(R.drawable.buoy);
			marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
			List<Person> people = (List<Person>) msg.getData().getSerializable("messages");
			PeopleItemizedOverlay peopleOverlay = new PeopleItemizedOverlay(people, marker,PoiinBackgroundService.this.getApplicationContext());
			ApplicationState appState=(ApplicationState)PoiinBackgroundService.this.getApplication();
			appState.getMapView().getOverlays().add(peopleOverlay);
		}		
	};
	
	
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
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
			notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
			mNotificationManager.notify(1, notification);

		}
	};

	private final Handler messageReceivedHandler = new Handler() {

		@Override
		public void handleMessage(final Message msg) {
			List<UserMessage> messages = (List<UserMessage>) msg.getData().getSerializable("messages");
			for (UserMessage message : messages) {
				sendNotification(message);
			}
		}

		private void sendNotification(UserMessage message) {
			int icon = android.R.drawable.sym_action_email;
			CharSequence tickerText = "Poiin!! Message";
			long when = System.currentTimeMillis();
			Notification notification = new Notification(icon, tickerText, when);
			Context context = getApplicationContext();
			CharSequence contentTitle = "Poiin!! Message";
			CharSequence contentText = "";
			Intent notificationIntent = new Intent(context, MessageReceivedActivity.class);
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			notificationIntent.putExtra("userMessage", message);
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
			notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
			mNotificationManager.notify(2, notification);
		}
	};
}
