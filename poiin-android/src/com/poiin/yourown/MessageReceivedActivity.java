package com.poiin.yourown;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poiin.yourown.people.message.UserMessage;
import com.poiin.yourown.people.message.UserMessageSender;
import com.poiin.yourown.social.GenericProfilePictureRetriever;

public class MessageReceivedActivity extends Activity {

	private LinearLayout layoutOfMessagesReceived;
	private EditText textOfResponseMessage;
	private Button responseButton;
	private UserMessageSender userMessageSender = new UserMessageSender();
	private UserMessage lastReceivedMessage;
	
	private ApplicationState applicationState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_received);
		layoutOfMessagesReceived = (LinearLayout) findViewById(R.id.receivedMessagesLayout);
		textOfResponseMessage = (EditText) findViewById(R.id.responseMessageText);
		responseButton = (Button) findViewById(R.id.sendResponseMessageButton);
		applicationState = (ApplicationState) getApplication();
	}

	@Override
	protected void onStart() {
		super.onStart();
		UserMessage message = (UserMessage) getIntent().getExtras()
				.getSerializable("userMessage");
		addMessageToUserMap(message);
		addAllUserMessages(message);
		addSendButtonListener();
		applicationState.setCurrentMessenger(message.getFrom());
	}

	@Override
	protected void onResume() {
		super.onResume();
		assertThatThisActivityIsInForeground();
	}

	@Override
	protected void onPause() {
		super.onPause();
		assertThatThisActivityIsNotForeground();
	}
	
	@Override
	public void onBackPressed() {
		startActivity(new Intent(this, Main.class));
	}

	private void assertThatThisActivityIsNotForeground() {
		ApplicationState app = (ApplicationState) getApplication();
		app.setForegroundActivity(null);
	}

	private void assertThatThisActivityIsInForeground() {
		ApplicationState app = (ApplicationState) getApplication();
		app.setForegroundActivity(MessageReceivedActivity.class);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		UserMessage message = (UserMessage) intent.getExtras().getSerializable(
				"userMessage");
		addMessageToUserMap(message);
		layoutOfMessagesReceived.addView(createTextViewWithMessageAndCacheLastMessage(message));
		applicationState.setCurrentMessenger(message.getFrom());
	}

	private View createTextViewWithMessageAndCacheLastMessage(
			UserMessage message) {
		cacheLastReceivedMessage(message);
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		TextView textView = new TextView(this);
		textView.setText(message.getContent());
		textView.setTextSize(20);
		textView.setTextColor(0xff660033);
		ImageView imageView = new ImageView(this);
		imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setMinimumHeight(60);
		imageView.setMinimumWidth(60);
		GenericProfilePictureRetriever.retrieveToImageView(
				this.getApplication(), imageView, message.getFromTwitterId(),
				message.getFromFacebookId());
		linearLayout.addView(imageView);
		linearLayout.addView(textView);
		linearLayout.setBackgroundColor(ColorSwapper.getNextColor());
		return linearLayout;
	}

	private void addMessageToUserMap(UserMessage message) {
		addMessageToUserMap(message.getFrom(), message);
	}
	
	private void addMessageToUserMap(String userId, UserMessage message) {
		List<UserMessage> messageList;
		if (applicationState.getMapOfUsersAndTheirMessages().get(userId) == null) {
			messageList = new ArrayList<UserMessage>();
			applicationState.getMapOfUsersAndTheirMessages().put(userId, messageList);
		} else {
			messageList = applicationState.getMapOfUsersAndTheirMessages().get(userId);
		}
		messageList.add(message);
	}

	private void cacheLastReceivedMessage(UserMessage message) {
		lastReceivedMessage = message;
	}

	private void addAllUserMessages(UserMessage message) {
		layoutOfMessagesReceived.removeAllViews();
		for (UserMessage msg : applicationState.getMapOfUsersAndTheirMessages()
				.get(message.getFrom())) {
			layoutOfMessagesReceived
					.addView(createTextViewWithMessageAndCacheLastMessage(msg));
		}
	}

	private void addSendButtonListener() {
		responseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UserMessage message = new UserMessage(textOfResponseMessage
						.getText().toString());
				ApplicationState app = (ApplicationState) getApplication();
				message.setFrom(app.getMyId().toString());
				message.setFromFacebookId(getMyJsonProperty(app,"facebook_id"));
				message.setFromTwitterId(getMyJsonProperty(app,"twitter_id"));
				message.setTo(lastReceivedMessage.getFrom());
				userMessageSender.sendMessage(message);
				textOfResponseMessage.setText("");
				addMessageToUserMap(message.getTo(),message);
				addMyMessageToView(message);
			}


			private String getMyJsonProperty(ApplicationState app,String property){
				try {
					return app.getMe().getString(property);
				} catch (JSONException e) {
					return null;
				}
			}
		});
	}
	
	private void addMyMessageToView(UserMessage message) {
		layoutOfMessagesReceived.addView(createTextViewWithMessageAndCacheLastMessage(message));
	}
	
	private static class ColorSwapper{
		private static int nextColor = Color.LTGRAY;
		public static int getNextColor(){
			if(nextColor == Color.LTGRAY){
				nextColor = Color.WHITE;
			}else{
				nextColor = Color.LTGRAY;
			}
			return nextColor;
		}
	}

}
