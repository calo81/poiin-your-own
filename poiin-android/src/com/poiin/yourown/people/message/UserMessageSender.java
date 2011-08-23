package com.poiin.yourown.people.message;

import com.poiin.yourown.network.RestClientService;
import com.poiin.yourown.network.RestClientServiceImpl;

public class UserMessageSender {
	private RestClientService restService = RestClientServiceImpl.getInstance();

	public void sendMessage(final UserMessage message) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				restService.sendUserMessage(message);
			}
		}).start();

	}
}
