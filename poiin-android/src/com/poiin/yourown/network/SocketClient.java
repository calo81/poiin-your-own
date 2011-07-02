package com.poiin.yourown.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
	private static final int FIVE_MINUTES = 10000;
	private static final String SERVER_URL_HOST = "192.168.0.5";
	private static final String MESSAGE_URL_PORT = "3001";
	private MessageReceivedHandler userMessageReceivedHandler;
	private ContextWrapper context;
	private Socket socket;
	private BufferedReader reader;

	public void establishConnectionAndWaitForMessage(MessageReceivedHandler userMessageReceivedHandler, ContextWrapper context) {
		this.userMessageReceivedHandler = userMessageReceivedHandler;
		this.context = context;
		try {
			watchDogSocket();
		} catch (Exception e) {
			Log.e(this.getClass().getName(), "Error in socket connection", e);
		}
	}

	private void watchDogSocket() throws UnknownHostException, IOException {
		final SocketAddress address = new InetSocketAddress(SERVER_URL_HOST, Integer.parseInt(MESSAGE_URL_PORT));		
		TimerTask task = configureWatchDogTask(address);
		Timer timer = new Timer();
		timer.schedule(task, 0, FIVE_MINUTES);
	}

	private TimerTask configureWatchDogTask(final SocketAddress address) {
		return new TimerTask() {
			boolean firstTime = true;

			@Override
			public void run() {
				if (!isSocketAlive() || firstTime) {
					firstTime = false;
					try {
						connectSocket(address);
						sendUserIdToSocket(socket, context);
						reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						readMessagesFromSocketAndHandle(userMessageReceivedHandler, reader);
					} catch (Exception e) {
						Log.e("SocketClient", "Problem in connection to socket.. will retry in 5 minutes ..", e);
					}
				}
			}

			private boolean isSocketAlive(){
				return socket!=null && canWriteToSocket();
			}

			private boolean canWriteToSocket() {
				try {
					writeToSocket("HEART_BEAT|0");
					return true;
				} catch (IOException e) {
					return false;
				}
			}
			
			private void connectSocket(final SocketAddress address) throws IOException{
				if(socket!=null){
					socket.close();
				}
				socket = new Socket(SERVER_URL_HOST, Integer.parseInt(MESSAGE_URL_PORT));
			}
		};
	}

	private void writeToSocket(String string)throws IOException{
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			writer.println(string);
			writer.flush();
			if(writer.checkError()){
				throw new IOException("Error in writing to socket.. socket unavailable ..");
			}
	}
	
	private void sendUserIdToSocket(Socket socket, ContextWrapper context) {
		try {	
			long userId = ((ApplicationState) context.getApplicationContext()).getMe().getLong("id");
			writeToSocket("USER_ID|" + userId);
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
		if (messageIsType(messageType, MessageTypes.USER_MESSAGE)) {
			userMessageReceivedHandler.receiveMessages(createUserMessageFromJsonMessage(message.split("\\|")[1]));
		} else if (messageIsType(messageType, MessageTypes.POIIN_MESSAGE)) {
			userMessageReceivedHandler.receivePoiins(createPoiinMessageFromJsonMessage(message.split("\\|")[1]));
		}
	}

	private boolean messageIsType(String incomingType, MessageTypes expectedType) {
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

	public static enum MessageTypes {
		USER_MESSAGE, POIIN_MESSAGE;
	}
}
