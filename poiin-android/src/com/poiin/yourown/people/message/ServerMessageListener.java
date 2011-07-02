package com.poiin.yourown.people.message;

import java.io.Serializable;
import java.util.List;

import com.poiin.yourown.ApplicationState;
import com.poiin.yourown.network.RestClientService;
import com.poiin.yourown.network.RestClientServiceImpl;
import com.poiin.yourown.network.SocketClient;
import com.poiin.yourown.people.Person;

import android.content.ContextWrapper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ServerMessageListener {
	private static ServerMessageListener instance;
	private ApplicationState context;
	private SocketClient socketClient;
	private RestClientService restClient = new RestClientServiceImpl();

	private ServerMessageListener(ApplicationState context) {
		this.context = context;
		this.socketClient = new SocketClient();
	}

	public static ServerMessageListener getInstance(ApplicationState context) {
		if (instance == null) {
			instance = new ServerMessageListener(context);
		}
		return instance;
	}

	public void start(final Handler messageReceivedHandler) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				socketClient.establishConnectionAndWaitForMessage(new MessageReceivedHandler() {
					@Override
					public void receiveMessages(List<UserMessage> messages) {
						sendMessageAcknowledge(messages);
						messageReceivedHandler.sendMessage(createMessageBundle((Serializable)messages,"USER_MESSAGE"));
					}

					@Override
					public void receivePoiins(List<Person> people) {
							messageReceivedHandler.sendMessage(createMessageBundle((Serializable)people,"POIIN"));
					}
					
					private void sendMessageAcknowledge(final List<UserMessage> messages) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								for (UserMessage message : messages) {
									restClient.acknowledgeMessage(message.getId(),context.getMyId().toString());
								}
							}
						}).start();
					}

					private Message createMessageBundle(Serializable data,String type) {
						Message message = new Message();
						Bundle bundle = new Bundle();
						bundle.putSerializable("messages", data);
						bundle.putString("type", type);
						message.setData(bundle);
						return message;
					}
					
				}, context);
			}
		}).start();
	}

}
