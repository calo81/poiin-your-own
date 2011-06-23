package com.poiin.yourown;

import com.poiin.yourown.people.message.UserMessage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MessageReceivedActivity extends Activity{

	private EditText textOfMessageReceived;
	private EditText textOfResponseMessage;
	private Button responseButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_received);
		textOfMessageReceived = (EditText)findViewById(R.id.messageReceivedText);
		textOfResponseMessage = (EditText)findViewById(R.id.responseMessageText);
		responseButton = (Button)findViewById(R.id.sendResponseMessageButton);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		UserMessage message = (UserMessage)intent.getExtras().getSerializable("message");
		textOfMessageReceived.setText(message.getContent());		
	}

}
