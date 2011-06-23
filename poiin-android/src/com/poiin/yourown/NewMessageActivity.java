package com.poiin.yourown;

import com.poiin.yourown.people.message.UserMessage;
import com.poiin.yourown.people.message.UserMessageSender;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NewMessageActivity extends Activity {

	private Button sendMessageButton;
	private EditText messageText;
	private String receiver;
	private UserMessageSender userMessageSender=new UserMessageSender();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_message);
		sendMessageButton=(Button)findViewById(R.id.sendMessageButton);
		messageText=(EditText)findViewById(R.id.messageText);
	}

	@Override
	protected void onStart() {
		super.onStart();
		receiver = getIntent().getExtras().getString("messageReceiver");
		addSendButtonListener();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		receiver = intent.getExtras().getString("messageReceiver");
	}
	
	private void addSendButtonListener() {
		sendMessageButton.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				UserMessage message = new UserMessage(messageText.getText().toString());
				ApplicationState app = (ApplicationState) getApplication();
				message.setFrom(app.getMyId().toString());
				message.setTo(receiver);
				userMessageSender.sendMessage(message);
			}
		});
	}
}
