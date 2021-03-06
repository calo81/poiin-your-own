package com.poiin.yourown;

import org.json.JSONException;

import com.poiin.yourown.people.message.UserMessage;
import com.poiin.yourown.people.message.UserMessageSender;
import com.poiin.yourown.social.facebook.FacebookAuthentication;

import android.app.Activity;
import android.app.ProgressDialog;
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
	private UserMessageSender userMessageSender = new UserMessageSender();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_message);
		sendMessageButton = (Button) findViewById(R.id.sendMessageButton);
		messageText = (EditText) findViewById(R.id.messageText);
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
				ProgressDialog progressDialog = ProgressDialog.show(NewMessageActivity.this, "Processing . . .", "Sending message ...", true, false);
				UserMessage message = new UserMessage(messageText.getText()
						.toString());
				ApplicationState app = (ApplicationState) getApplication();
				message.setFrom(app.getMyId().toString());
				setTwitterIdIfExistent(message, app);
				setFacebookIdIfExistent(message, app);
				message.setTo(receiver);
				userMessageSender.sendMessage(message);
				progressDialog.dismiss();
				finish();
			}

			private void setFacebookIdIfExistent(UserMessage message,
					ApplicationState app) {
				try {
					message.setFromFacebookId(app.getMe().getString(
							"facebook_id"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			private void setTwitterIdIfExistent(UserMessage message,
					ApplicationState app) {
				try {
					message.setFromTwitterId(app.getMe()
							.getString("twitter_id"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
