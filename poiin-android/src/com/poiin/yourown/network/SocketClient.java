package com.poiin.yourown.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.content.ContextWrapper;
import android.util.Log;

import com.poiin.yourown.ApplicationState;
import com.poiin.yourown.people.message.UserMessage;
import com.poiin.yourown.people.message.UserMessageReceivedHandler;

public class SocketClient {
	private static final String MESSAGE_URL_HOST = "192.168.0.5";
	private static final String MESSAGE_URL_PORT = "3001";

	public void establishConnectionAndWaitForMessage(UserMessageReceivedHandler userMessageReceivedHandler, ContextWrapper context) {
		Socket socket = null;
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
			writer.println(userId);
			writer.close();
		} catch (Exception e) {
			Log.e("SocketClient", "Unable to write User Id to socket", e);
		}
	}

	private void readMessagesFromSocketAndHandle(UserMessageReceivedHandler userMessageReceivedHandler, BufferedReader reader) throws IOException {
		while (true) {
			String message = reader.readLine();
			Log.i(SocketClient.class.getName(), "Received Message " + message);
			userMessageReceivedHandler.receiveMessage(new UserMessage(message));
		}
	}
}
