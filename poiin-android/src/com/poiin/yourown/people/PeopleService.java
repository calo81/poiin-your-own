package com.poiin.yourown.people;

import android.os.Handler;

public interface PeopleService {

	boolean checkUserRegisteredAndUpdateSocialIds();

	void registerUser(Person person, Handler registrationHandler);

	void updateUser();

	void inviteToPoiin(Person person);

	void getUserInfo(Person person);

}
