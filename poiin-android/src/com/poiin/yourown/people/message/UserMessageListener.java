package com.poiin.yourown.people.message;

import com.poiin.yourown.network.SocketClient;

import android.content.ContextWrapper;
import android.os.Handler;
import android.util.Log;

public class UserMessageListener {
	private static UserMessageListener instance;
	private ContextWrapper context;
	private SocketClient socketClient;
	
	private UserMessageListener(ContextWrapper context) {
		this.context=context;
		this.socketClient=new SocketClient();
	}
	
	public static UserMessageListener getInstance(ContextWrapper context){
		if(instance==null){
			instance = new UserMessageListener(context);		
		}
		return instance;
	}

	public void start(Handler messageReceivedHandler) {
		new Thread(new Runnable() {		
			@Override
			public void run() {
				socketClient.establishConnectionAndWaitForMessage(new UserMessageReceivedHandler() {					
					@Override
					public void receiveMessage(UserMessage message) {
						Log.i(this.getClass().getName(), "Received USer Message "+message.getContent());					
					}
				});
			}
		}).start();
	}
	

	
}
