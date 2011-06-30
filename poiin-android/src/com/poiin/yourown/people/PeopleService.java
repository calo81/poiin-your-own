package com.poiin.yourown.people;

import android.os.Handler;

public interface PeopleService {

	boolean isUserRegistered();

	void registerUser(Person person,Handler registrationHandler);

}
