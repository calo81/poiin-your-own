package com.poiin.yourown.network;

import org.json.JSONObject;

import com.poiin.yourown.people.message.UserMessage;
import com.poiin.yourown.poiin.Poiin;

public interface RestClientService {

	void sendPoiin(Poiin poiin);

	void sendUserMessage(UserMessage message);

	void acknowledgeMessage(String id, String userId);

	JSONObject isUserRegistered(String userId);

	void registerUser(JSONObject me);

	void updateUser(JSONObject me);

}
