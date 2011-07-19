package com.poiin.yourown.people.message;

import java.util.List;

import com.poiin.yourown.people.Person;

public interface MessageReceivedHandler {
	public void receiveMessages(List<UserMessage> messages);

	public void receivePoiins(List<Person> people);
}
