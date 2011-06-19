package com.poiin.yourown;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class NewMessageActivity extends Activity {

	private Button sendMessageButton;
	private EditText messageText;
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
	}

}
