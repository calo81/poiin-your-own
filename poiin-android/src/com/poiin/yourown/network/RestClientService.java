package com.poiin.yourown.network;

import org.json.JSONArray;
import org.json.JSONObject;

import com.poiin.yourown.people.message.UserMessage;
import com.poiin.yourown.poiin.Poiin;

public interface RestClientService {

	void sendPoiin(Poiin poiin);

	JSONArray getPoiins(JSONObject user);

	void sendUserMessage(UserMessage message);

	public void acknowledgeMessage(String id,String userId);

}
