package com.poiin.yourown.social;

import java.util.List;

import com.poiin.yourown.people.Person;

import android.graphics.Bitmap;

public interface ProfilePictureRetriever {
	Bitmap retrieveBitmap();

	void retrieveToImageView();

	void fillPictureUrl(List<Person> people);
}
