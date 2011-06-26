package com.poiin.yourown.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContextWrapper;
import android.util.Log;

import com.poiin.yourown.ApplicationState;
import com.poiin.yourown.people.Person;
import com.poiin.yourown.people.message.UserMessage;
import com.poiin.yourown.people.message.MessageReceivedHandler;

public class SocketClient {
	private static final String MESSAGE_URL_HOST = "192.168.0.5";
	private static final String MESSAGE_URL_PORT = "3001";
	private Socket socket;

	public void establishConnectionAndWaitForMessage(MessageReceivedHandler userMessageReceivedHandler, ContextWrapper context) {
		BufferedReader reader = null;
		try {
			socket = new Socket(MESSAGE_URL_HOST, Integer.parseInt(MESSAGE_URL_PORT));
			sendUserIdToSocket(socket, context);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			readMessagesFromSocketAndHandle(userMessageReceivedHandler, reader);
		} catch (Exception e) {
			Log.e(this.getClass().getName(), "Error in socket connection", e);
		}
	}

	private void sendUserIdToSocket(Socket socket, ContextWrapper context) {
		try {
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			long userId = ((ApplicationState) context.getApplicationContext()).getMe().getLong("id");
			writer.println("USER_ID|"+userId);
			writer.flush();
		} catch (Exception e) {
			Log.e("SocketClient", "Unable to write User Id to socket", e);
		}
	}

	private void readMessagesFromSocketAndHandle(MessageReceivedHandler userMessageReceivedHandler, BufferedReader reader) throws IOException {
		while (true && socket.isConnected() && !socket.isInputShutdown()) {
			String message = reader.readLine();
			Log.i(SocketClient.class.getName(), "Received Message " + message);
			handleMessage(userMessageReceivedHandler, message);
		}
	}

	private void handleMessage(MessageReceivedHandler userMessageReceivedHandler, String message) {
		String messageType = message.split("\\|")[0];
		if(messageIsType(messageType,MessageTypes.USER_MESSAGE)){
			userMessageReceivedHandler.receiveMessages(createUserMessageFromJsonMessage(message.split("\\|")[1]));
		}else if(messageIsType(messageType,MessageTypes.POIIN_MESSAGE)){
			userMessageReceivedHandler.receivePoiins(createPoiinMessageFromJsonMessage(message.split("\\|")[1]));
		}
	}

	private boolean messageIsType(String incomingType,MessageTypes expectedType) {
		return expectedType.ordinal() == Integer.parseInt(incomingType);
	}

	
	private List<Person> createPoiinMessageFromJsonMessage(String poiins) {
		try {
			List<Person> people = new ArrayList<Person>();
			JSONArray jsonMessages = new JSONArray(poiins);
			for (int i = 0; i < jsonMessages.length(); i++) {
				JSONObject poiin = jsonMessages.getJSONObject(i);
				Person person = new Person();
				person.setName(poiin.getJSONObject("user").getString("user_name"));
				person.setId(poiin.getJSONObject("user").getString("id"));
				person.setLatitude(poiin.getJSONObject("poiin").getDouble("latitude"));
				person.setLongitude(poiin.getJSONObject("poiin").getDouble("longitude"));
				people.add(person);
			}
			return people;
		} catch (JSONException e) {
			// TODO: Do we need to handle this???
			Log.e(this.getClass().getName(), "Error parsing incoming Message " + poiins, e);
			return null;
		}
	}
	
	private List<UserMessage> createUserMessageFromJsonMessage(String messages) {
		try {
			List<UserMessage> userMessages = new ArrayList<UserMessage>();
			JSONArray jsonMessages = new JSONArray(messages);
			for (int i = 0; i < jsonMessages.length(); i++) {
				JSONObject jsonMessage = jsonMessages.getJSONObject(i);
				UserMessage userMessage = new UserMessage(jsonMessage.getString("message"));
				userMessage.setFrom(jsonMessage.getString("from"));
				userMessage.setId(jsonMessage.getString("id"));
				userMessages.add(userMessage);
			}
			return userMessages;
		} catch (JSONException e) {
			// TODO: Do we need to handle this???
			Log.e(this.getClass().getName(), "Error parsing incoming Message " + messages, e);
			return null;
		}

	}
	
	public static enum MessageTypes{
		USER_MESSAGE,
		POIIN_MESSAGE;
	}
}


