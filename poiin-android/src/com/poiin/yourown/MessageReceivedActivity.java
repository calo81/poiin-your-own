package com.poiin.yourown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.poiin.yourown.people.message.UserMessage;
import com.poiin.yourown.people.message.UserMessageSender;
import com.poiin.yourown.social.facebook.ProfilePictureRetriever;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageReceivedActivity extends Activity{

	private LinearLayout layoutOfMessagesReceived;
	private EditText textOfResponseMessage;
	private Button responseButton;
	private UserMessageSender userMessageSender=new UserMessageSender();
	private UserMessage lastReceivedMessage;
	private Map<String, List<UserMessage>> mapOfUsersAndTheirMessages;
	private ApplicationState applicationState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_received);
		layoutOfMessagesReceived = (LinearLayout)findViewById(R.id.receivedMessagesLayout);
		textOfResponseMessage = (EditText)findViewById(R.id.responseMessageText);
		responseButton = (Button)findViewById(R.id.sendResponseMessageButton);
		mapOfUsersAndTheirMessages=new HashMap<String,  List<UserMessage>>();
		applicationState = (ApplicationState) getApplication();
	}

	@Override
	protected void onStart() {
		super.onStart();
		UserMessage message = (UserMessage)getIntent().getExtras().getSerializable("userMessage");	
		addMessageToUserMap(message);
		addAllUserMessages(message);
		addSendButtonListener();
		applicationState.setCurrentMessenger(message.getFrom());
	}

	
	
	@Override
	protected void onResume(){
		super.onResume();
		assertThatThisActivityIsInForeground();
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		assertThatThisActivityIsNotForeground();
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
		UserMessage message = (UserMessage)intent.getExtras().getSerializable("userMessage");
		addMessageToUserMap(message);
		addAllUserMessages(message);
		applicationState.setCurrentMessenger(message.getFrom());
	}
	
	private View createTextViewWithMessageAndCacheLastMessage(UserMessage message) {
		cacheLastReceivedMessage(message);
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		TextView textView = new TextView(this);
		textView.setText(message.getContent());
		ImageView imageView = new ImageView(this);
		new ProfilePictureRetriever(imageView, message.getFrom()).retrieve();
		linearLayout.addView(imageView);
		linearLayout.addView(textView);
		return linearLayout;
	}
	
	private void addMessageToUserMap(UserMessage message) {
		List<UserMessage> messageList;
		if(mapOfUsersAndTheirMessages.get(message.getFrom())==null){
			messageList = new ArrayList<UserMessage>();
			mapOfUsersAndTheirMessages.put(message.getFrom(), messageList);
		}else{
			messageList = mapOfUsersAndTheirMessages.get(message.getFrom());
		}
		messageList.add(message);		
	}

	
	private void cacheLastReceivedMessage(UserMessage message) {
		lastReceivedMessage =message;
	}
	
	private void addAllUserMessages(UserMessage message) {
		layoutOfMessagesReceived.removeAllViews();
		for(UserMessage msg: mapOfUsersAndTheirMessages.get(message.getFrom())){
			layoutOfMessagesReceived.addView(createTextViewWithMessageAndCacheLastMessage(msg));
		}
	}

	private void addSendButtonListener() {
		responseButton.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				UserMessage message = new UserMessage(textOfResponseMessage.getText().toString());
				ApplicationState app = (ApplicationState) getApplication();
				message.setFrom(app.getMyId().toString());
				message.setTo(lastReceivedMessage.getFrom());
				userMessageSender.sendMessage(message);
			}
		});
	}

}
